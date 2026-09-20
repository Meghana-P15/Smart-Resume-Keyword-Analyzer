package processing;

import java.util.ArrayList;

import model.JobDescription;

public class JobTextProcessor {

    // Returns the complete normalized job description
    public String getProcessedDescription(JobDescription jobDescription) {

        if (jobDescription == null) {
            return "";
        }

        String description = jobDescription.getDescription();

        if (description == null) {
            return "";
        }

        return normalizeText(description);
    }

    // Converts JD text into a standard format
    private String normalizeText(String text) {

        text = text.toLowerCase();

        // Keep letters, numbers and spaces
        text = text.replaceAll("[^a-z0-9\\s]", " ");

        // Remove extra spaces
        text = text.replaceAll("\\s+", " ").trim();

        return text;
    }

    // Converts the processed JD into individual words
    public ArrayList<String> getTokens(JobDescription jobDescription) {

        ArrayList<String> tokens = new ArrayList<>();

        String processedText = getProcessedDescription(jobDescription);

        if (processedText.isEmpty()) {
            return tokens;
        }

        String[] words = processedText.split(" ");

        for (String word : words) {

            if (!word.isEmpty()) {
                tokens.add(word);
            }
        }

        return tokens;
    }

    // Displays the processed JD for testing
    public void displayProcessedDescription(JobDescription jobDescription) {

        System.out.println("\n----------------------------------------");
        System.out.println("       PROCESSED JOB DESCRIPTION");
        System.out.println("----------------------------------------");

        System.out.println(
                getProcessedDescription(jobDescription)
        );

        System.out.println("\nTokens:");

        ArrayList<String> tokens = getTokens(jobDescription);

        for (String token : tokens) {
            System.out.println("- " + token);
        }
    }
}