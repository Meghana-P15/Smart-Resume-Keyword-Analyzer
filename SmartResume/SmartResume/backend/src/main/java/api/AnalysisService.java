package api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import analysis.JobMatchAnalyzer;
import analysis.JobMatchResult;
import model.Candidate;
import model.Certification;
import model.Experience;
import model.JobDescription;
import model.Project;

/**
 * Glue code between the REST API and the existing DSA analyzer:
 *   1. build Candidate + JobDescription from the request
 *   2. call the existing JobMatchAnalyzer (no scoring is done here)
 *   3. save the result in PostgreSQL
 *   4. return the result as JSON-friendly maps
 */
@Service
public class AnalysisService {

    private final AnalysisRecordRepository repository;
    private final ObjectMapper objectMapper;

    // The existing DSA analyzer - used exactly as it is
    private final JobMatchAnalyzer analyzer = new JobMatchAnalyzer();

    public AnalysisService(AnalysisRecordRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    // ---------------------------------------------------------------- POST /api/analyze
    public Map<String, Object> analyzeAndSave(AnalyzeRequest request) {
        if (request == null || request.candidate() == null || request.job() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate and job details are required.");
        }
        if (isBlank(request.candidate().name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Candidate name is required.");
        }
        if (isBlank(request.job().jobTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job title is required.");
        }

        Candidate candidate = buildCandidate(request.candidate());
        JobDescription job = buildJob(request.job());

        // ---- the DSA analysis (existing code) ----
        JobMatchResult result = analyzer.analyze(candidate, job);

        AnalysisRecord record = new AnalysisRecord(
                candidate.getName(), candidate.getEmail(), candidate.getCandidateType(),
                job.getJobTitle(), job.getCompany(), result,
                toJson(result.getExactMatches()),
                toJson(result.getNearMatches()),
                toJson(result.getMissingSkills()),
                toJson(result.getPrioritySkills()),
                toJson(result.getPriorityLevels()));

        return toResponse(repository.save(record));
    }

    // ---------------------------------------------------------------- GET /api/history
    public List<Map<String, Object>> getHistory() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AnalysisRecord record : repository.findAllByOrderByCreatedAtDesc()) {
            list.add(toResponse(record));
        }
        return list;
    }

    // ---------------------------------------------------------------- request -> DSA model classes
    private Candidate buildCandidate(AnalyzeRequest.CandidateData c) {
        String type = isBlank(c.candidateType()) ? "Fresher" : c.candidateType();

        Candidate candidate = new Candidate(
                text(c.name()), text(c.email()),
                c.age() == null ? 0 : c.age(),
                text(c.gender()), type,
                text(c.degree()), text(c.university()),
                c.graduationYear() == null ? 0 : c.graduationYear(),
                c.cgpa() == null ? 0.0 : c.cgpa());

        if (c.skills() != null) {
            for (String skill : c.skills()) {
                if (!isBlank(skill)) candidate.addSkill(skill.trim());
            }
        }

        // The analyzer reads years from text like "Software Engineer at Google for 2 years"
        if (c.experiences() != null) {
            for (AnalyzeRequest.ExperienceData e : c.experiences()) {
                candidate.addExperience(new Experience(experienceText(e)));
            }
        }

        if (c.projects() != null) {
            for (AnalyzeRequest.ProjectData p : c.projects()) {
                candidate.addProject(new Project(text(p.title()), text(p.description()), text(p.technologies())));
            }
        }

        if (c.certifications() != null) {
            for (AnalyzeRequest.CertificationData cert : c.certifications()) {
                candidate.addCertification(new Certification(text(cert.name()), text(cert.issuer())));
            }
        }

        candidate.setInternship(text(c.internship()));
        candidate.setAchievements(text(c.achievements()));
        candidate.setSummary(text(c.summary()));
        return candidate;
    }

    private JobDescription buildJob(AnalyzeRequest.JobData j) {
        JobDescription job = new JobDescription(
                text(j.jobTitle()), text(j.company()),
                j.requiredExperience() == null ? 0.0 : j.requiredExperience(),
                text(j.description()));

        if (j.requiredSkills() != null) {
            for (String skill : j.requiredSkills()) {
                if (!isBlank(skill)) job.addRequiredSkill(skill.trim());
            }
        }
        return job;
    }

    private String experienceText(AnalyzeRequest.ExperienceData e) {
        String role = text(e.role());
        String company = text(e.company());
        String sentence = role;
        if (!company.isEmpty()) sentence += " at " + company;

        if (e.years() != null && e.years() > 0) {
            double years = e.years();
            String yearsText = (years % 1 == 0) ? String.valueOf((int) years) : String.valueOf(years);
            sentence += " for " + yearsText + " years";
        }
        return sentence.trim();
    }

    // ---------------------------------------------------------------- database row -> JSON for React
    // "result" has the same fields as JobMatchResult, so React can show it directly.
    private Map<String, Object> toResponse(AnalysisRecord r) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("overallScore", r.getOverallScore());
        result.put("classification", r.getClassification());
        result.put("skillScore", r.getSkillScore());
        result.put("experienceScore", r.getExperienceScore());
        result.put("similarityScore", r.getSimilarityScore());
        result.put("projectScore", r.getProjectScore());
        result.put("certificationScore", r.getCertificationScore());
        result.put("roleRelevanceScore", r.getRoleRelevanceScore());
        result.put("roleRelevanceLabel", r.getRoleRelevanceLabel());
        result.put("exactMatches", fromJson(r.getMatchedSkills()));
        result.put("nearMatches", fromJson(r.getNearMatches()));
        result.put("missingSkills", fromJson(r.getMissingSkills()));
        result.put("prioritySkills", fromJson(r.getPrioritySkills()));
        result.put("priorityLevels", fromJson(r.getPriorityLevels()));
        result.put("kmpMatches", r.getKmpMatches());
        result.put("nGramMatches", r.getNgramMatches());
        result.put("trieRecognizedSkills", r.getTrieRecognizedSkills());
        result.put("relevantKeywordCount", r.getRelevantKeywordCount());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", r.getId());
        response.put("candidateName", r.getCandidateName());
        response.put("candidateEmail", r.getCandidateEmail());
        response.put("candidateType", r.getCandidateType());
        response.put("jobTitle", r.getJobTitle());
        response.put("company", r.getCompany());
        response.put("createdAt", r.getCreatedAt().toString());
        response.put("result", result);
        return response;
    }

    // ---------------------------------------------------------------- small helpers
    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not convert to JSON", e);
        }
    }

    private Object fromJson(String json) {
        try {
            return objectMapper.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not read JSON", e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String text(String s) {
        return s == null ? "" : s.trim();
    }
}
