package model;

import java.util.ArrayList;

public class Candidate {

    // Personal Information
    private String name;
    private String email;
    private int age;
    private String gender;

    // Candidate Category
    private String candidateType;

    // Education
    private String degree;
    private String university;
    private int graduationYear;
    private double cgpa;

    // Skills
    private ArrayList<String> skills;

    // Experience
    private ArrayList<Experience> experiences;

    // Projects
    private ArrayList<Project> projects;

    // Certifications
    private ArrayList<Certification> certifications;

    // Optional Information
    private String internship;
    private String achievements;
    private String summary;

    // Constructor
    public Candidate(String name, String email, int age, String gender,
                     String candidateType, String degree, String university,
                     int graduationYear, double cgpa) {

        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.candidateType = candidateType;
        this.degree = degree;
        this.university = university;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;

        this.skills = new ArrayList<>();
        this.experiences = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.certifications = new ArrayList<>();

        this.internship = "";
        this.achievements = "";
        this.summary = "";
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCandidateType() {
        return candidateType;
    }

    public String getDegree() {
        return degree;
    }

    public String getUniversity() {
        return university;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public double getCgpa() {
        return cgpa;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public ArrayList<Experience> getExperiences() {
        return experiences;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public ArrayList<Certification> getCertifications() {
        return certifications;
    }

    public String getInternship() {
        return internship;
    }

    public String getAchievements() {
        return achievements;
    }

    public String getSummary() {
        return summary;
    }

    // Methods to add information
    public void addSkill(String skill) {
        skills.add(skill);
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void addCertification(Certification certification) {
        certifications.add(certification);
    }

    // Methods to set optional information
    public void setInternship(String internship) {
        this.internship = internship;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}