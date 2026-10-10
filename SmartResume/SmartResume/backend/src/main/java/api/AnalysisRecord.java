package api;

import java.time.LocalDateTime;

import analysis.JobMatchResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * One row of the analysis_records table = one saved analysis.
 * Numbers are stored in normal columns. Lists (matched / missing skills ...) are stored
 * as small JSON text so we do not need extra tables.
 */
@Entity
@Table(name = "analysis_records")
public class AnalysisRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // who and what was analysed
    private String candidateName;
    private String candidateEmail;
    private String candidateType;
    private String jobTitle;
    private String company;

    // scores copied from JobMatchResult
    private double overallScore;
    private String classification;
    private double skillScore;
    private double experienceScore;
    private double similarityScore;
    private double projectScore;
    private double certificationScore;
    private double roleRelevanceScore;
    private String roleRelevanceLabel;

    // DSA counters copied from JobMatchResult
    private int kmpMatches;
    private int ngramMatches;
    private int trieRecognizedSkills;
    private int relevantKeywordCount;

    // skill lists (JSON text)
    @Column(columnDefinition = "TEXT")
    private String matchedSkills;
    @Column(columnDefinition = "TEXT")
    private String nearMatches;
    @Column(columnDefinition = "TEXT")
    private String missingSkills;
    @Column(columnDefinition = "TEXT")
    private String prioritySkills;
    @Column(columnDefinition = "TEXT")
    private String priorityLevels;

    private LocalDateTime createdAt;

    // JPA needs an empty constructor
    protected AnalysisRecord() {
    }

    public AnalysisRecord(String candidateName, String candidateEmail, String candidateType,
                          String jobTitle, String company, JobMatchResult result,
                          String matchedSkills, String nearMatches, String missingSkills,
                          String prioritySkills, String priorityLevels) {
        this.candidateName = candidateName;
        this.candidateEmail = candidateEmail;
        this.candidateType = candidateType;
        this.jobTitle = jobTitle;
        this.company = company;

        this.overallScore = result.getOverallScore();
        this.classification = result.getClassification();
        this.skillScore = result.getSkillScore();
        this.experienceScore = result.getExperienceScore();
        this.similarityScore = result.getSimilarityScore();
        this.projectScore = result.getProjectScore();
        this.certificationScore = result.getCertificationScore();
        this.roleRelevanceScore = result.getRoleRelevanceScore();
        this.roleRelevanceLabel = result.getRoleRelevanceLabel();

        this.kmpMatches = result.getKmpMatches();
        this.ngramMatches = result.getNGramMatches();
        this.trieRecognizedSkills = result.getTrieRecognizedSkills();
        this.relevantKeywordCount = result.getRelevantKeywordCount();

        this.matchedSkills = matchedSkills;
        this.nearMatches = nearMatches;
        this.missingSkills = missingSkills;
        this.prioritySkills = prioritySkills;
        this.priorityLevels = priorityLevels;

        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCandidateName() { return candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public String getCandidateType() { return candidateType; }
    public String getJobTitle() { return jobTitle; }
    public String getCompany() { return company; }
    public double getOverallScore() { return overallScore; }
    public String getClassification() { return classification; }
    public double getSkillScore() { return skillScore; }
    public double getExperienceScore() { return experienceScore; }
    public double getSimilarityScore() { return similarityScore; }
    public double getProjectScore() { return projectScore; }
    public double getCertificationScore() { return certificationScore; }
    public double getRoleRelevanceScore() { return roleRelevanceScore; }
    public String getRoleRelevanceLabel() { return roleRelevanceLabel; }
    public int getKmpMatches() { return kmpMatches; }
    public int getNgramMatches() { return ngramMatches; }
    public int getTrieRecognizedSkills() { return trieRecognizedSkills; }
    public int getRelevantKeywordCount() { return relevantKeywordCount; }
    public String getMatchedSkills() { return matchedSkills; }
    public String getNearMatches() { return nearMatches; }
    public String getMissingSkills() { return missingSkills; }
    public String getPrioritySkills() { return prioritySkills; }
    public String getPriorityLevels() { return priorityLevels; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
