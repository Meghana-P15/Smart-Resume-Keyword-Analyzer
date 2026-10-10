package analysis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JobMatchResult {

    private final List<String> exactMatches = new ArrayList<>();
    private final Map<String, String> nearMatches = new LinkedHashMap<>();
    private final List<String> missingSkills = new ArrayList<>();
    private final List<String> prioritySkills = new ArrayList<>();
    private final Map<String, String> priorityLevels = new LinkedHashMap<>();

    private double skillScore;
    private double experienceScore;
    private double roleRelevanceScore;
    private String roleRelevanceLabel = "LOW";
    private double similarityScore;
    private double projectScore;
    private double certificationScore;
    private double overallScore;
    private String classification;

    private int kmpMatches;
    private int trieRecognizedSkills;
    private int relevantKeywordCount;
    private int nGramMatches;

    public List<String> getExactMatches() { return exactMatches; }
    public Map<String, String> getNearMatches() { return nearMatches; }
    public List<String> getMissingSkills() { return missingSkills; }
    public List<String> getPrioritySkills() { return prioritySkills; }
    public Map<String, String> getPriorityLevels() { return priorityLevels; }

    public double getSkillScore() { return skillScore; }
    public void setSkillScore(double skillScore) { this.skillScore = skillScore; }

    public double getExperienceScore() { return experienceScore; }
    public void setExperienceScore(double experienceScore) { this.experienceScore = experienceScore; }

    public double getRoleRelevanceScore() { return roleRelevanceScore; }
    public void setRoleRelevanceScore(double roleRelevanceScore) { this.roleRelevanceScore = roleRelevanceScore; }

    public String getRoleRelevanceLabel() { return roleRelevanceLabel; }
    public void setRoleRelevanceLabel(String roleRelevanceLabel) { this.roleRelevanceLabel = roleRelevanceLabel; }

    public double getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(double similarityScore) { this.similarityScore = similarityScore; }

    public double getProjectScore() { return projectScore; }
    public void setProjectScore(double projectScore) { this.projectScore = projectScore; }

    public double getCertificationScore() { return certificationScore; }
    public void setCertificationScore(double certificationScore) { this.certificationScore = certificationScore; }

    public double getOverallScore() { return overallScore; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }

    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

    public int getKmpMatches() { return kmpMatches; }
    public void setKmpMatches(int kmpMatches) { this.kmpMatches = kmpMatches; }

    public int getTrieRecognizedSkills() { return trieRecognizedSkills; }
    public void setTrieRecognizedSkills(int trieRecognizedSkills) { this.trieRecognizedSkills = trieRecognizedSkills; }

    public int getRelevantKeywordCount() { return relevantKeywordCount; }
    public void setRelevantKeywordCount(int relevantKeywordCount) { this.relevantKeywordCount = relevantKeywordCount; }

    public int getNGramMatches() { return nGramMatches; }
    public void setNGramMatches(int nGramMatches) { this.nGramMatches = nGramMatches; }
}
