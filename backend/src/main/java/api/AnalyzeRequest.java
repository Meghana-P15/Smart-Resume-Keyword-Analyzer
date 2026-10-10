package api;

import java.util.List;

/**
 * The JSON body that React sends to POST /api/analyze.
 * Java records are just small classes that only hold data.
 */
public record AnalyzeRequest(CandidateData candidate, JobData job) {

    public record CandidateData(
            String name, String email, Integer age, String gender,
            String candidateType, String degree, String university,
            Integer graduationYear, Double cgpa, String summary,
            List<String> skills,
            List<ExperienceData> experiences,
            String internship,
            List<ProjectData> projects,
            List<CertificationData> certifications,
            String achievements) {
    }

    public record ExperienceData(String role, String company, Double years) {
    }

    public record ProjectData(String title, String description, String technologies) {
    }

    public record CertificationData(String name, String issuer) {
    }

    public record JobData(
            String jobTitle, String company, Double requiredExperience,
            List<String> requiredSkills, String description) {
    }
}
