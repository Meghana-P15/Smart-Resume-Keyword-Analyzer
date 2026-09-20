package analysis;

import java.util.List;

import algorithms.EditDistance;
import algorithms.FrequencyScorer;
import algorithms.KMP;
import algorithms.NGramMatcher;
import algorithms.Trie;
import model.Candidate;
import model.JobDescription;

public class SkillMatchAnalyzer {

    private final KMP kmp = new KMP();
    private final EditDistance editDistance = new EditDistance();
    private final NGramMatcher nGramMatcher = new NGramMatcher();
    private final FrequencyScorer frequencyScorer = new FrequencyScorer();

    public JobMatchResult analyze(Candidate candidate, JobDescription jobDescription) {
        JobMatchResult result = new JobMatchResult();

        List<String> candidateSkills = candidate.getSkills();
        List<String> requiredSkills = jobDescription.getRequiredSkills();
        String candidateText = buildCandidateText(candidate);

        Trie trie = new Trie();
        for (String skill : candidateSkills) {
            trie.insert(normalize(skill));
        }

        int kmpMatches = 0;
        int trieRecognized = 0;
        int nGramMatches = 0;

        for (String requiredSkill : requiredSkills) {
            String required = normalize(requiredSkill);
            boolean exactFound = false;

            // KMP exact matching against the explicitly entered candidate skills.
            for (String candidateSkill : candidateSkills) {
                String candidateValue = normalize(candidateSkill);
                if (candidateValue.equals(required)
                        && kmp.search(candidateValue, required) >= 0) {
                    exactFound = true;
                    kmpMatches++;
                    break;
                }
            }

            if (trie.search(required)) {
                trieRecognized++;
            }

            // N-Gram detects multi-word phrases anywhere in the candidate profile.
            boolean nGramFound = false;
            if (required.split("\\s+").length >= 2
                    && nGramMatcher.containsNGram(candidateText, required)) {
                nGramFound = true;
                nGramMatches++;
            }

            if (exactFound || nGramFound) {
                result.getExactMatches().add(requiredSkill);
                continue;
            }

            String closestSkill = null;
            int smallestDistance = Integer.MAX_VALUE;

            for (String candidateSkill : candidateSkills) {
                String candidateValue = normalize(candidateSkill);
                String a = candidateValue.replace(" ", "");
                String b = required.replace(" ", "");

                if (a.length() <= 2 || b.length() <= 2) continue;

                int distance = editDistance.calculate(a, b);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestSkill = candidateSkill;
                }
            }

            int threshold = required.length() <= 6 ? 1
                    : required.length() <= 12 ? 2 : 3;

            if (closestSkill != null && smallestDistance <= threshold) {
                result.getNearMatches().put(closestSkill, requiredSkill);
            } else {
                result.getMissingSkills().add(requiredSkill);
            }
        }

        result.setKmpMatches(kmpMatches);
        result.setTrieRecognizedSkills(trieRecognized);
        result.setNGramMatches(nGramMatches);
        result.setRelevantKeywordCount(
                frequencyScorer.countRelevantKeywordOccurrences(candidateText, requiredSkills));

        return result;
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

        return normalize(text.toString());
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
