package algorithms;

import java.util.HashMap;
import java.util.Map;

public class CosineSimilarity {

    public double calculate(
            String text1,
            String text2) {

        Map<String, Integer> vector1 =
                frequencyVector(text1);

        Map<String, Integer> vector2 =
                frequencyVector(text2);

        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        for (String word : vector1.keySet()) {

            int value1 = vector1.get(word);

            int value2 =
                    vector2.getOrDefault(word, 0);

            dotProduct += value1 * value2;
        }

        for (int value : vector1.values()) {
            magnitude1 += value * value;
        }

        for (int value : vector2.values()) {
            magnitude2 += value * value;
        }

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0;
        }

        return dotProduct /
                (Math.sqrt(magnitude1)
                        * Math.sqrt(magnitude2));
    }

    private Map<String, Integer> frequencyVector(
            String text) {

        Map<String, Integer> map =
                new HashMap<>();

        if (text == null) {
            return map;
        }

        String cleaned =
                text.toLowerCase()
                        .replaceAll("[^a-z0-9+#. ]", " ");

        String[] words =
                cleaned.trim().split("\\s+");

        for (String word : words) {

            if (word.isEmpty()) {
                continue;
            }

            map.put(
                    word,
                    map.getOrDefault(word, 0) + 1
            );
        }

        return map;
    }
}