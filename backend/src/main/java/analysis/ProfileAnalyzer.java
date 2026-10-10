package analysis;

import model.Candidate;
import model.JobDescription;

public class ProfileAnalyzer {

    public double calculateProfileStrength(Candidate candidate, JobDescription jobDescription) {
        if (candidate == null) return 0.0;

        if ("Fresher".equalsIgnoreCase(candidate.getCandidateType())) {
            return calculateFresherStrength(candidate);
        }

        return calculateExperiencedStrength(candidate);
    }

    private double calculateFresherStrength(Candidate candidate) {
        double cgpaScore = Math.min(candidate.getCgpa() / 10.0 * 20.0, 20.0);
        double skillScore = Math.min(candidate.getSkills().size() / 8.0 * 25.0, 25.0);
        double projectScore = Math.min(candidate.getProjects().size() / 4.0 * 30.0, 30.0);
        double certificationScore = Math.min(candidate.getCertifications().size() / 3.0 * 15.0, 15.0);
        double internshipScore = candidate.getInternship() != null
                && !candidate.getInternship().trim().isEmpty() ? 10.0 : 0.0;

        return Math.min(100.0,
                cgpaScore + skillScore + projectScore + certificationScore + internshipScore);
    }

    private double calculateExperiencedStrength(Candidate candidate) {
        ExperienceAnalyzer experienceAnalyzer = new ExperienceAnalyzer();
        double years = experienceAnalyzer.calculateTotalExperience(candidate);

        double experienceScore = Math.min(years / 5.0 * 35.0, 35.0);
        double skillScore = Math.min(candidate.getSkills().size() / 8.0 * 30.0, 30.0);
        double projectScore = Math.min(candidate.getProjects().size() / 3.0 * 15.0, 15.0);
        double certificationScore = Math.min(candidate.getCertifications().size() / 3.0 * 10.0, 10.0);
        double educationScore = Math.min(candidate.getCgpa() / 10.0 * 10.0, 10.0);

        return Math.min(100.0,
                experienceScore + skillScore + projectScore + certificationScore + educationScore);
    }
}
