package algorithms;

import java.util.ArrayList;
import java.util.List;

public class NGramMatcher {

    public List<String> generateNGrams(
            String text,
            int n) {

        List<String> ngrams =
                new ArrayList<>();

        if (text == null || n <= 0) {
            return ngrams;
        }

        String cleaned =
                text.toLowerCase()
                        .replaceAll("[^a-z0-9+#. ]", " ")
                        .replaceAll("\\s+", " ")
                        .trim();

        if (cleaned.isEmpty()) {
            return ngrams;
        }

        String[] words =
                cleaned.split("\\s+");

        if (words.length < n) {
            return ngrams;
        }

        for (int i = 0;
             i <= words.length - n;
             i++) {

            StringBuilder gram =
                    new StringBuilder();

            for (int j = 0; j < n; j++) {

                if (j > 0) {
                    gram.append(" ");
                }

                gram.append(words[i + j]);
            }

            ngrams.add(gram.toString());
        }

        return ngrams;
    }

    public boolean containsNGram(
            String text,
            String phrase) {

        if (text == null || phrase == null) {
            return false;
        }

        String normalizedPhrase =
                phrase.toLowerCase()
                        .trim()
                        .replaceAll("\\s+", " ");

        int n =
                normalizedPhrase.split("\\s+").length;

        List<String> ngrams =
                generateNGrams(text, n);

        for (String gram : ngrams) {

            if (gram.equals(normalizedPhrase)) {
                return true;
            }
        }

        return false;
    }
}