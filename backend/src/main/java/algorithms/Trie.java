package algorithms;

import java.util.ArrayList;
import java.util.List;

public class Trie {

    private static class TrieNode {

        TrieNode[] children = new TrieNode[27];
        boolean isEnd;
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    private int index(char c) {

        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }

        if (c == ' ') {
            return 26;
        }

        return -1;
    }

    public void insert(String word) {

        if (word == null) {
            return;
        }

        word = word.toLowerCase().trim();

        TrieNode current = root;

        for (char c : word.toCharArray()) {

            int index = index(c);

            if (index == -1) {
                continue;
            }

            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }

            current = current.children[index];
        }

        current.isEnd = true;
    }

    public boolean search(String word) {

        if (word == null) {
            return false;
        }

        TrieNode node = findNode(word.toLowerCase().trim());

        return node != null && node.isEnd;
    }

    public boolean startsWith(String prefix) {

        if (prefix == null) {
            return false;
        }

        return findNode(prefix.toLowerCase().trim()) != null;
    }

    private TrieNode findNode(String word) {

        TrieNode current = root;

        for (char c : word.toCharArray()) {

            int index = index(c);

            if (index == -1) {
                continue;
            }

            if (current.children[index] == null) {
                return null;
            }

            current = current.children[index];
        }

        return current;
    }

    public List<String> getWordsWithPrefix(String prefix) {

        List<String> words = new ArrayList<>();

        if (prefix == null) {
            return words;
        }

        TrieNode node =
                findNode(prefix.toLowerCase().trim());

        if (node == null) {
            return words;
        }

        collectWords(node,
                prefix.toLowerCase().trim(),
                words);

        return words;
    }

    private void collectWords(
            TrieNode node,
            String currentWord,
            List<String> words) {

        if (node.isEnd) {
            words.add(currentWord);
        }

        for (int i = 0; i < 27; i++) {

            if (node.children[i] != null) {

                char c;

                if (i == 26) {
                    c = ' ';
                } else {
                    c = (char) ('a' + i);
                }

                collectWords(
                        node.children[i],
                        currentWord + c,
                        words
                );
            }
        }
    }
}