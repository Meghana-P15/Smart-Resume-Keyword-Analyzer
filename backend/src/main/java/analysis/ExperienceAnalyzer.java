package analysis;

import java.util.HashSet;
import java.util.Set;

import model.Candidate;
import model.Experience;
import processing.ExperienceExtractor;

public class ExperienceAnalyzer {

    private final ExperienceExtractor extractor = new ExperienceExtractor();

    public double calculateTotalExperience(Candidate candidate) {
        if (candidate == null) {
            return 0.0;
        }

        double total = 0.0;
        for (Experience experience : candidate.getExperiences()) {
            total += extractor.extractYears(experience.getDescription());
        }
        return total;
    }

    public String getExperienceStatus(double totalExperience, double requiredExperience) {
        if (requiredExperience <= 0) {
            return "No Experience Requirement";
        }
        if (totalExperience >= requiredExperience) {
            return "Requirement Exceeded";
        }
        return "Requirement Not Met";
    }

    public String getExperienceStrength(double totalExperience, double requiredExperience) {
        if (requiredExperience <= 0) {
            if (totalExperience >= 5) return "STRONG";
            if (totalExperience >= 2) return "MODERATE";
            return "DEVELOPING";
        }

        double ratio = totalExperience / requiredExperience;
        if (ratio >= 1.5) return "STRONG";
        if (ratio >= 1.0) return "MODERATE";
        return "DEVELOPING";
    }

    public String getCareerStage(Candidate candidate) {
        if (candidate == null) {
            return "Unknown";
        }

        if ("Fresher".equalsIgnoreCase(candidate.getCandidateType())) {
            return "Fresher / Early Career";
        }

        double experience = calculateTotalExperience(candidate);
        int age = candidate.getAge();

        if (experience <= 0) return "Early Career";

        // Age provides context, but never directly adds or subtracts score.
        if ((age <= 25 && experience <= 4.0) || experience < 4.0) {
            return "Early Professional";
        }

        if (experience < 9.0) {
            return "Mid-Level Professional";
        }

        return "Senior Professional";
    }

    public double calculateRoleRelevance(Candidate candidate, String targetRole) {
        if (candidate == null || targetRole == null || targetRole.trim().isEmpty()) {
            return 0.0;
        }

        if (candidate.getExperiences().isEmpty()) {
            return 0.0;
        }

        String normalizedTarget = normalizeRole(targetRole);
        Set<String> targetTokens = tokenSet(normalizedTarget);
        double bestScore = 0.0;

        for (Experience experience : candidate.getExperiences()) {
            String role = extractor.extractRole(experience.getDescription());
            String normalizedRole = normalizeRole(role + " " + experience.getDescription());
            Set<String> roleTokens = tokenSet(normalizedRole);

            if (roleTokens.isEmpty() || targetTokens.isEmpty()) {
                continue;
            }

            int overlap = 0;
            for (String token : targetTokens) {
                if (roleTokens.contains(token)) {
                    overlap++;
                }
            }

            double overlapScore = (double) overlap / targetTokens.size() * 100.0;

            boolean bothTechnical = containsTechnicalRole(normalizedTarget)
                    && containsTechnicalRole(normalizedRole);

            if (bothTechnical) {
                overlapScore = Math.max(overlapScore, 70.0);
            }

            bestScore = Math.max(bestScore, overlapScore);
        }

        return Math.min(bestScore, 100.0);
    }

    public String getRoleRelevanceLabel(Candidate candidate, String targetRole) {
        if (candidate != null && "Fresher".equalsIgnoreCase(candidate.getCandidateType())) {
            return "N/A (Fresher)";
        }

        double score = calculateRoleRelevance(candidate, targetRole);
        if (score >= 70) return "HIGH";
        if (score >= 40) return "MODERATE";
        return "LOW";
    }

    private String normalizeRole(String role) {
        if (role == null) return "";

        return role.toLowerCase()
                .replace("software engineer", "software developer")
                .replace("engineer", "developer")
                .replace("programmer", "developer")
                .replace("senior", "")
                .replace("junior", "")
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private Set<String> tokenSet(String text) {
        Set<String> tokens = new HashSet<>();
        if (text == null || text.isEmpty()) return tokens;

        for (String token : text.split("\\s+")) {
            if (token.length() > 1 && !token.equals("at") && !token.equals("for")) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private boolean containsTechnicalRole(String text) {
        return text.contains("developer")
                || text.contains("software")
                || text.contains("data")
                || text.contains("analyst")
                || text.contains("devops")
                || text.contains("cloud")
                || text.contains("machine learning")
                || text.contains("backend")
                || text.contains("frontend")
                || text.contains("full stack");
    }
}
