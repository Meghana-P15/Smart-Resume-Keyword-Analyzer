package algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrequencyScorer {

    public Map<String, Integer> getFrequency(String text) {
        Map<String, Integer> frequency = new HashMap<>();

        if (text == null) {
            return frequency;
        }

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        if (cleaned.isEmpty()) {
            return frequency;
        }

        for (String word : cleaned.split("\\s+")) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        return frequency;
    }

    public int getScore(String text) {
        int score = 0;
        for (int value : getFrequency(text).values()) {
            score += value;
        }
        return score;
    }

    public double weightedScore(String text, String keyword) {
        if (keyword == null) return 0;
        return getFrequency(text).getOrDefault(keyword.toLowerCase(), 0);
    }

    // Counts occurrences of JD-relevant keywords in candidate text using HashMap frequency.
    public int countRelevantKeywordOccurrences(String text, List<String> requiredSkills) {
        Map<String, Integer> frequency = getFrequency(text);
        Set<String> relevantTokens = new HashSet<>();

        for (String skill : requiredSkills) {
            if (skill == null) continue;
            String normalized = skill.toLowerCase()
                    .replaceAll("[^a-z0-9+#. ]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();

            for (String token : normalized.split("\\s+")) {
                if (!token.isEmpty()) relevantTokens.add(token);
            }
        }

        int count = 0;
        for (String token : relevantTokens) {
            count += frequency.getOrDefault(token, 0);
        }
        return count;
    }
}
