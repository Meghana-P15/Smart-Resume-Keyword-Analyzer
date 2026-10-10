package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Candidate;
import model.JobDescription;

public class Analyzer {

    private final ExperienceAnalyzer experienceAnalyzer = new ExperienceAnalyzer();
    private final ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer();
    private final CertificationAnalyzer certificationAnalyzer = new CertificationAnalyzer();
    private final ProfileAnalyzer profileAnalyzer = new ProfileAnalyzer();
    private final JobMatchAnalyzer jobMatchAnalyzer = new JobMatchAnalyzer();

    public void analyze(Candidate candidate, JobDescription jobDescription) {
        JobMatchResult result = jobMatchAnalyzer.analyze(candidate, jobDescription);

        double totalExperience = experienceAnalyzer.calculateTotalExperience(candidate);
        double requiredExperience = jobDescription.getRequiredExperience();
        String careerStage = experienceAnalyzer.getCareerStage(candidate);
        String experienceStatus = experienceAnalyzer.getExperienceStatus(
                totalExperience, requiredExperience);
        String experienceStrength = experienceAnalyzer.getExperienceStrength(
                totalExperience, requiredExperience);

        ArrayList<String> requiredSkills = jobDescription.getRequiredSkills();
        int projectCount = projectAnalyzer.getProjectCount(candidate);
        int relevantProjectCount = projectAnalyzer.getRelevantProjectCount(candidate, requiredSkills);
        List<String> relevantProjectNames = projectAnalyzer.getRelevantProjectNames(candidate, requiredSkills);

        int certificationCount = certificationAnalyzer.getCertificationCount(candidate);
        int relevantCertificationCount = certificationAnalyzer.getRelevantCertificationCount(candidate, requiredSkills);

        double profileStrength = profileAnalyzer.calculateProfileStrength(candidate, jobDescription);

        printHeader();
        System.out.println();
        System.out.println("Candidate       : " + candidate.getName());
        System.out.println("Category        : " + candidate.getCandidateType());
        System.out.println("Age             : " + candidate.getAge());
        System.out.println("Gender          : " + candidate.getGender());
        System.out.println();
        System.out.println("Target Role     : " + jobDescription.getJobTitle());

        printSection("CAREER PROFILE");
        System.out.printf("Career Stage          : %s%n", careerStage);
        System.out.printf("Total Experience      : %.1f years%n", totalExperience);
        System.out.printf("Required Experience   : %.1f years%n", requiredExperience);
        System.out.printf("Experience Status     : %s %s%n",
                totalExperience >= requiredExperience ? "✓" : "✗",
                experienceStatus);
        System.out.println();
        System.out.printf("Experience Strength   : %s%n", experienceStrength);
        System.out.printf("Role Relevance        : %s%n", result.getRoleRelevanceLabel());

        printSection("SKILL ANALYSIS");
        for (String skill : result.getExactMatches()) {
            System.out.println("✓ " + skill);
        }

        if (!result.getNearMatches().isEmpty()) {
            System.out.println();
            for (Map.Entry<String, String> entry : result.getNearMatches().entrySet()) {
                System.out.println("~ " + entry.getKey() + " → " + entry.getValue());
            }
        }

        if (!result.getMissingSkills().isEmpty()) {
            System.out.println();
            for (String skill : result.getMissingSkills()) {
                System.out.println("✗ " + skill);
            }
        }

        System.out.println();
        System.out.println("Exact Matches         : " + result.getExactMatches().size());
        System.out.println("Near Matches          : " + result.getNearMatches().size());
        System.out.println("Missing Skills        : " + result.getMissingSkills().size());

        printSection("PROJECT ANALYSIS");
        System.out.println("Projects Analyzed     : " + projectCount);
        System.out.println("Relevant Projects     : " + relevantProjectCount);
        System.out.printf("Project Relevance     : %.0f%%%n", result.getProjectScore());

        if (!relevantProjectNames.isEmpty()) {
            System.out.println();
            for (String projectName : relevantProjectNames) {
                System.out.println("✓ " + projectName);
            }
        }

        printSection("CERTIFICATION ANALYSIS");
        System.out.println("Certifications        : " + certificationCount);
        System.out.println("Relevant              : " + relevantCertificationCount);
        System.out.printf("Relevance             : %.0f%%%n", result.getCertificationScore());

        printSection("STRENGTHS");
        List<String> strengths = buildStrengths(
                experienceStrength, totalExperience, requiredExperience, result);
        for (String strength : strengths) {
            System.out.println("✓ " + strength);
        }

        printSection("AREAS TO IMPROVE");
        boolean hasImprovement = false;
        for (String skill : result.getMissingSkills()) {
            System.out.println("✗ " + skill);
            hasImprovement = true;
        }
        for (Map.Entry<String, String> entry : result.getNearMatches().entrySet()) {
            System.out.println("~ " + entry.getValue() + " experience could be strengthened");
            hasImprovement = true;
        }
        if (!hasImprovement) {
            System.out.println("✓ No major skill gaps detected");
        }

        printSection("ALGORITHM RESULTS");
        System.out.println("KMP Exact Matching       : " + result.getKmpMatches() + " matches");
        System.out.println("Trie Skill Lookup        : " + result.getTrieRecognizedSkills() + " recognized skills");
        System.out.println("HashMap Frequency        : " + result.getRelevantKeywordCount() + " relevant keywords");
        System.out.println("Edit Distance            : " + result.getNearMatches().size() + " near match(es)");
        System.out.println("N-Gram Matching          : " + result.getNGramMatches() + " phrases matched");
        System.out.printf("Cosine Similarity        : %.2f%%%n", result.getSimilarityScore());

        printSection("PRIORITY IMPROVEMENTS");
        if (result.getPrioritySkills().isEmpty()) {
            System.out.println("No priority improvements required.");
        } else {
            int rank = 1;
            for (String skill : result.getPrioritySkills()) {
                String level = result.getPriorityLevels().getOrDefault(skill, "MEDIUM");
                System.out.printf("%d. %-17s → %s%n", rank, skill, level);
                rank++;
            }
        }

        printSection("SCORECARD");
        System.out.printf("Profile Strength       : %.1f / 100%n", profileStrength);
        System.out.printf("Job Match Score        : %.1f / 100%n", result.getOverallScore());
        System.out.println();
        System.out.println("Classification         : ★ " + result.getClassification());

        printSection("WHY THIS SCORE?");
        printWhyScore(experienceStrength, totalExperience, requiredExperience, result);

        printSection("FINAL RECOMMENDATION");
        System.out.println(result.getClassification());
        System.out.println();
        printRecommendation(result);

        System.out.println();
        System.out.println("========================================================");
    }

    private List<String> buildStrengths(
            String experienceStrength,
            double totalExperience,
            double requiredExperience,
            JobMatchResult result) {

        List<String> strengths = new ArrayList<>();

        if ("STRONG".equals(experienceStrength)) {
            strengths.add("Strong experience for career stage");
        }
        if (requiredExperience > 0 && totalExperience >= requiredExperience) {
            strengths.add("Experience exceeds requirement");
        }
        if (result.getRoleRelevanceScore() >= 70) {
            strengths.add("High role relevance");
        }
        if (result.getSkillScore() >= 70) {
            strengths.add("Strong skill overlap");
        }
        if (result.getProjectScore() >= 60) {
            strengths.add("Good project relevance");
        }
        if (result.getCertificationScore() >= 60) {
            strengths.add("Relevant certifications");
        }
        if (result.getSimilarityScore() >= 60) {
            strengths.add("Good resume/JD similarity");
        }

        if (strengths.isEmpty()) {
            strengths.add("Profile contains a foundation that can be improved for the target role");
        }

        return strengths;
    }

    private void printWhyScore(
            String experienceStrength,
            double totalExperience,
            double requiredExperience,
            JobMatchResult result) {

        if ("STRONG".equals(experienceStrength)) {
            System.out.println("+ Strong experience for career stage");
        }
        if (requiredExperience > 0 && totalExperience >= requiredExperience) {
            System.out.println("+ Required experience exceeded");
        }
        if (result.getSkillScore() >= 70) {
            System.out.println("+ High skill overlap");
        }
        if (result.getProjectScore() >= 60) {
            System.out.println("+ Relevant projects");
        }
        if (result.getSimilarityScore() >= 60) {
            System.out.println("+ Good resume/JD similarity");
        }
        if (result.getCertificationScore() >= 60) {
            System.out.println("+ Relevant certifications");
        }

        System.out.println();

        for (String skill : result.getMissingSkills()) {
            System.out.println("- " + skill + " skill missing");
        }
        for (String required : result.getNearMatches().values()) {
            System.out.println("- " + required + " only a near match");
        }

        if (result.getMissingSkills().isEmpty() && result.getNearMatches().isEmpty()) {
            System.out.println("- No major technical gaps detected");
        }
    }

    private void printRecommendation(JobMatchResult result) {
        switch (result.getClassification()) {
            case "STRONG MATCH":
                System.out.println("The candidate meets most of the technical and");
                System.out.println("experience requirements for this position.");
                break;
            case "GOOD MATCH":
                System.out.println("The candidate matches many requirements but should");
                System.out.println("improve the highlighted gaps before applying.");
                break;
            case "MODERATE MATCH":
                System.out.println("The candidate has partial alignment with the role.");
                System.out.println("Priority skill and experience gaps should be addressed.");
                break;
            default:
                System.out.println("The candidate currently has limited alignment with");
                System.out.println("this role and should focus on the priority improvements.");
        }
    }

    private void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║              SMART RESUME ANALYZER                  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private void printSection(String title) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────────");
        System.out.printf("%27s%n", title);
        System.out.println("──────────────────────────────────────────────────────");
        System.out.println();
    }
}
