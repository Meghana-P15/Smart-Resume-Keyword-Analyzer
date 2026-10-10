package processing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Candidate;
import model.Experience;

public class ExperienceExtractor {

    // Extracts the company name from common experience formats
    public String extractCompany(String text) {

        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // Format: "Software Engineer at Google for 2 years"
        Pattern pattern1 = Pattern.compile(
                "\\bat\\s+([A-Za-z0-9&. ]+?)(?:\\s+for\\s+|\\s*-\\s*|$)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher1 = pattern1.matcher(text);

        if (matcher1.find()) {
            return matcher1.group(1).trim();
        }

        // Format: "2 years at Google"
        Pattern pattern2 = Pattern.compile(
                "\\bat\\s+([A-Za-z0-9&. ]+?)(?:\\s+as\\s+|\\s*-\\s*|$)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher2 = pattern2.matcher(text);

        if (matcher2.find()) {
            return matcher2.group(1).trim();
        }

        return "Not detected";
    }

    // Extracts years of experience
    public double extractYears(String text) {

        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        Pattern pattern = Pattern.compile(
                "(\\d+(?:\\.\\d+)?)\\s*(?:years?|yrs?)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return 0;
    }

    // Extracts the job role
    public String extractRole(String text) {

        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // Format: "Software Engineer at Google"
        Pattern pattern = Pattern.compile(
                "^(.+?)\\s+at\\s+",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return "Not detected";
    }

    // Processes all experiences of a candidate
    public void processExperiences(Candidate candidate) {

        if (candidate == null) {
            return;
        }

        for (Experience experience : candidate.getExperiences()) {

            String description = experience.getDescription();

            String role = extractRole(description);
            String company = extractCompany(description);
            double years = extractYears(description);

            System.out.println("\n----------------------------------------");
            System.out.println("        EXTRACTED EXPERIENCE");
            System.out.println("----------------------------------------");

            System.out.println("Original    : " + description);
            System.out.println("Role        : " + role);
            System.out.println("Company     : " + company);
            System.out.println("Years       : " + years);
        }
    }
}