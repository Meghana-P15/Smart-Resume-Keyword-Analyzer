package processing;

import java.util.ArrayList;

import model.Candidate;
import model.JobDescription;

public class SkillProcessor {

    // Returns normalized candidate skills
    public ArrayList<String> getCandidateSkills(Candidate candidate) {

        ArrayList<String> skills = new ArrayList<>();

        if (candidate == null) {
            return skills;
        }

        for (String skill : candidate.getSkills()) {

            String normalizedSkill = normalizeSkill(skill);

            if (!normalizedSkill.isEmpty()) {
                skills.add(normalizedSkill);
            }
        }

        return skills;
    }

    // Returns normalized JD required skills
    public ArrayList<String> getRequiredSkills(JobDescription jobDescription) {

        ArrayList<String> skills = new ArrayList<>();

        if (jobDescription == null) {
            return skills;
        }

        for (String skill : jobDescription.getRequiredSkills()) {

            String normalizedSkill = normalizeSkill(skill);

            if (!normalizedSkill.isEmpty()) {
                skills.add(normalizedSkill);
            }
        }

        return skills;
    }

    // Normalizes an individual skill
    private String normalizeSkill(String skill) {

        if (skill == null) {
            return "";
        }

        skill = skill.toLowerCase().trim();

        skill = skill.replaceAll("[^a-z0-9+#.\\s]", " ");

        skill = skill.replaceAll("\\s+", " ").trim();

        return skill;
    }

    // Displays processed skills for testing
    public void displaySkills(
            Candidate candidate,
            JobDescription jobDescription) {

        ArrayList<String> candidateSkills =
                getCandidateSkills(candidate);

        ArrayList<String> requiredSkills =
                getRequiredSkills(jobDescription);

        System.out.println("\n----------------------------------------");
        System.out.println("          PROCESSED SKILLS");
        System.out.println("----------------------------------------");

        System.out.println("Candidate Skills:");

        for (String skill : candidateSkills) {
            System.out.println("- " + skill);
        }

        System.out.println("\nRequired JD Skills:");

        for (String skill : requiredSkills) {
            System.out.println("- " + skill);
        }
    }
}