package model;

import java.util.ArrayList;

public class JobDescription {

    // Basic Job Information
    private String jobTitle;
    private String company;

    // Experience Requirement
    private double requiredExperience;

    // Required Skills
    private ArrayList<String> requiredSkills;

    // Full Job Description
    private String description;

    // Constructor
    public JobDescription(String jobTitle, String company,
                          double requiredExperience,
                          String description) {

        this.jobTitle = jobTitle;
        this.company = company;
        this.requiredExperience = requiredExperience;
        this.description = description;

        this.requiredSkills = new ArrayList<>();
    }

    // Getters
    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public double getRequiredExperience() {
        return requiredExperience;
    }

    public ArrayList<String> getRequiredSkills() {
        return requiredSkills;
    }

    public String getDescription() {
        return description;
    }

    // Method to add required skills
    public void addRequiredSkill(String skill) {
        requiredSkills.add(skill);
    }
}