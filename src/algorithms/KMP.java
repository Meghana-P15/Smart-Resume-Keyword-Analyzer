package algorithms;

public class KMP {

    private int[] buildLPS(String pattern) {

        int[] lps = new int[pattern.length()];
        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {
                lps[i] = ++length;
                i++;
            } else if (length > 0) {
                length = lps[length - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }

        return lps;
    }

    public int search(String text, String pattern) {

        if (text == null || pattern == null ||
                pattern.length() == 0) {
            return -1;
        }

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return i - j;
                }

            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }

        return -1;
    }

    public int countOccurrences(String text, String pattern) {

        if (text == null || pattern == null ||
                pattern.length() == 0) {
            return 0;
        }

        int count = 0;
        int start = 0;

        while (start <= text.length() - pattern.length()) {

            int position =
                    search(text.substring(start), pattern);

            if (position == -1) {
                break;
            }

            count++;
            start += position + 1;
        }

        return count;
    }
}