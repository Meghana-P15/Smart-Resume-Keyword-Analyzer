package processing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextProcessor {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "was", "were", "be", "been",
            "being", "and", "or", "but", "if", "then", "than", "of", "in",
            "on", "at", "to", "for", "from", "with", "without", "by", "as",
            "into", "about", "over", "under", "between", "through", "during",
            "before", "after", "above", "below", "this", "that", "these",
            "those", "it", "its", "we", "you", "your", "our", "their",
            "they", "he", "she", "his", "her", "them", "will", "would",
            "should", "could", "can", "may", "might", "must", "do", "does",
            "did", "have", "has", "had", "having", "looking", "required"
    ));

    public String normalizeText(String text) {
        if (text == null) {
            return "";
        }

        text = text.toLowerCase();
        text = text.replaceAll("[^a-z0-9+#.\\s]", " ");
        text = text.replaceAll("\\s+", " ").trim();
        return text;
    }

    public String[] tokenize(String text) {
        String normalized = normalizeText(text);
        if (normalized.isEmpty()) {
            return new String[0];
        }
        return normalized.split("\\s+");
    }

    public String removeStopWords(String text) {
        StringBuilder result = new StringBuilder();

        for (String token : tokenize(text)) {
            if (!STOP_WORDS.contains(token)) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(token);
            }
        }

        return result.toString();
    }

    public String processText(String text) {
        return removeStopWords(normalizeText(text));
    }

    public Set<String> getStopWords() {
        return new HashSet<>(STOP_WORDS);
    }
}
