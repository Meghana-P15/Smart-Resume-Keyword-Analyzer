package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import algorithms.CosineSimilarity;
import algorithms.GreedySkillPriority;
import algorithms.KMP;
import model.Candidate;
import model.JobDescription;
import processing.TextProcessor;

public class JobMatchAnalyzer {

    private final SkillMatchAnalyzer skillMatchAnalyzer = new SkillMatchAnalyzer();
    private final CosineSimilarity cosineSimilarity = new CosineSimilarity();
    private final GreedySkillPriority greedySkillPriority = new GreedySkillPriority();
    private final ExperienceAnalyzer experienceAnalyzer = new ExperienceAnalyzer();
    private final TextProcessor textProcessor = new TextProcessor();
    private final KMP kmp = new KMP();

    public JobMatchResult analyze(Candidate candidate, JobDescription jobDescription) {
        JobMatchResult result = skillMatchAnalyzer.analyze(candidate, jobDescription);

        int totalRequired = jobDescription.getRequiredSkills().size();
        int exact = result.getExactMatches().size();
        int near = result.getNearMatches().size();

        double skillScore = 0.0;
        if (totalRequired > 0) {
            skillScore = ((exact * 1.0) + (near * 0.60)) / totalRequired * 100.0;
        }
        result.setSkillScore(Math.min(skillScore, 100.0));

        double totalExperience = experienceAnalyzer.calculateTotalExperience(candidate);
        double requiredExperience = jobDescription.getRequiredExperience();
        double experienceScore;

        if (requiredExperience <= 0) {
            experienceScore = 100.0;
        } else {
            experienceScore = Math.min(totalExperience / requiredExperience * 100.0, 100.0);
        }
        result.setExperienceScore(experienceScore);

        double roleScore = experienceAnalyzer.calculateRoleRelevance(candidate, jobDescription.getJobTitle());
        String roleLabel = experienceAnalyzer.getRoleRelevanceLabel(candidate, jobDescription.getJobTitle());
        result.setRoleRelevanceScore(roleScore);
        result.setRoleRelevanceLabel(roleLabel);

        String candidateText = textProcessor.processText(buildCandidateText(candidate));
        String jobText = textProcessor.processText(buildJobText(jobDescription));
        double similarity = cosineSimilarity.calculate(candidateText, jobText) * 100.0;
        result.setSimilarityScore(similarity);

        ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer();
        result.setProjectScore(projectAnalyzer.calculateRelevance(
                candidate, jobDescription.getRequiredSkills()));

        CertificationAnalyzer certificationAnalyzer = new CertificationAnalyzer();
        result.setCertificationScore(certificationAnalyzer.calculateRelevance(
                candidate, jobDescription.getRequiredSkills()));

        buildGreedyPriorities(result, jobDescription);

        double overallScore;
        if ("Fresher".equalsIgnoreCase(candidate.getCandidateType())) {
            // Fresher matching does not penalize the absence of a prior role as heavily.
            overallScore = result.getSkillScore() * 0.45
                    + result.getExperienceScore() * 0.10
                    + result.getSimilarityScore() * 0.20
                    + result.getProjectScore() * 0.20
                    + result.getCertificationScore() * 0.05;
        } else {
            overallScore = result.getSkillScore() * 0.40
                    + result.getExperienceScore() * 0.20
                    + result.getRoleRelevanceScore() * 0.10
                    + result.getSimilarityScore() * 0.15
                    + result.getProjectScore() * 0.10
                    + result.getCertificationScore() * 0.05;
        }

        result.setOverallScore(Math.max(0.0, Math.min(100.0, overallScore)));

        if (overallScore >= 80) result.setClassification("STRONG MATCH");
        else if (overallScore >= 65) result.setClassification("GOOD MATCH");
        else if (overallScore >= 50) result.setClassification("MODERATE MATCH");
        else result.setClassification("LOW MATCH");

        return result;
    }

    private void buildGreedyPriorities(JobMatchResult result, JobDescription jobDescription) {
        Set<String> improvementSet = new LinkedHashSet<>();
        improvementSet.addAll(result.getNearMatches().values());
        improvementSet.addAll(result.getMissingSkills());

        List<String> improvements = new ArrayList<>(improvementSet);
        Map<String, Integer> weights = new HashMap<>();
        String jd = normalize(jobDescription.getDescription());

        for (String skill : improvements) {
            String normalized = normalize(skill);
            int weight = 3; // every required gap starts as at least medium value

            if (result.getNearMatches().containsValue(skill)) {
                weight += 2; // close-to-complete skills are high-value improvements
            }

            if (normalized.contains(" ")) {
                weight += 1; // multi-word technical skills tend to be more specific
            }

            int occurrences = kmp.countOccurrences(jd, normalized);
            weight += Math.min(occurrences, 2);

            weights.put(skill.toLowerCase(), weight);
        }

        List<String> prioritized = greedySkillPriority.prioritize(improvements, weights);
        for (String skill : prioritized) {
            result.getPrioritySkills().add(skill);
            int weight = weights.getOrDefault(skill.toLowerCase(), 1);
            result.getPriorityLevels().put(skill, greedySkillPriority.getPriorityLevel(weight));
        }
    }

    private String buildCandidateText(Candidate candidate) {
        StringBuilder text = new StringBuilder();

        for (String skill : candidate.getSkills()) text.append(skill).append(" ");
        if (candidate.getSummary() != null) text.append(candidate.getSummary()).append(" ");
        if (candidate.getAchievements() != null) text.append(candidate.getAchievements()).append(" ");
        if (candidate.getInternship() != null) text.append(candidate.getInternship()).append(" ");
        candidate.getExperiences().forEach(e -> text.append(e.getDescription()).append(" "));
        candidate.getProjects().forEach(p -> text.append(p.getTitle()).append(" ")
                .append(p.getDescription()).append(" ")
                .append(p.getTechnologies()).append(" "));
        candidate.getCertifications().forEach(c -> text.append(c.getName()).append(" ")
                .append(c.getIssuingOrganization()).append(" "));

        return text.toString();
    }

    private String buildJobText(JobDescription jobDescription) {
        StringBuilder text = new StringBuilder();
        text.append(jobDescription.getJobTitle()).append(" ")
                .append(jobDescription.getDescription()).append(" ");
        for (String skill : jobDescription.getRequiredSkills()) {
            text.append(skill).append(" ");
        }
        return text.toString();
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
