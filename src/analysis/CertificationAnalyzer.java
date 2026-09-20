package analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Candidate;
import model.Certification;

public class CertificationAnalyzer {

    public int getCertificationCount(Candidate candidate) {
        if (candidate == null || candidate.getCertifications() == null) {
            return 0;
        }
        return candidate.getCertifications().size();
    }

    public int getRelevantCertificationCount(Candidate candidate, ArrayList<String> requiredSkills) {
        if (candidate == null || requiredSkills == null) {
            return 0;
        }

        int relevant = 0;
        for (Certification certification : candidate.getCertifications()) {
            String text = normalize(certification.getName() + " "
                    + certification.getIssuingOrganization());

            for (String skill : requiredSkills) {
                if (containsSkill(text, normalize(skill))) {
                    relevant++;
                    break;
                }
            }
        }
        return relevant;
    }

    public double calculateRelevance(Candidate candidate, ArrayList<String> requiredSkills) {
        int total = getCertificationCount(candidate);
        if (total == 0 || requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        int relevant = getRelevantCertificationCount(candidate, requiredSkills);
        double relevantRatio = (double) relevant / total * 100.0;

        StringBuilder allCertifications = new StringBuilder();
        for (Certification certification : candidate.getCertifications()) {
            allCertifications.append(certification.getName()).append(" ")
                    .append(certification.getIssuingOrganization()).append(" ");
        }

        String normalized = normalize(allCertifications.toString());
        Set<String> coveredSkills = new HashSet<>();
        for (String skill : requiredSkills) {
            String normalizedSkill = normalize(skill);
            if (containsSkill(normalized, normalizedSkill)) {
                coveredSkills.add(normalizedSkill);
            }
        }

        double skillCoverage = (double) coveredSkills.size() / requiredSkills.size() * 100.0;

        // Combine certification count relevance and required-skill coverage.
        return Math.min(100.0, relevantRatio * 0.70 + skillCoverage * 0.30);
    }

    private boolean containsSkill(String text, String skill) {
        if (skill.isEmpty()) return false;
        return (" " + text + " ").contains(" " + skill + " ");
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
