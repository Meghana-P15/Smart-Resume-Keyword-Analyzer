package analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Candidate;
import model.Project;

public class ProjectAnalyzer {

    public int getProjectCount(Candidate candidate) {
        if (candidate == null || candidate.getProjects() == null) {
            return 0;
        }
        return candidate.getProjects().size();
    }

    public List<Project> getRelevantProjects(Candidate candidate, ArrayList<String> requiredSkills) {
        List<Project> relevantProjects = new ArrayList<>();

        if (candidate == null || requiredSkills == null) {
            return relevantProjects;
        }

        for (Project project : candidate.getProjects()) {
            String projectText = normalize(
                    project.getTitle() + " "
                    + project.getDescription() + " "
                    + project.getTechnologies()
            );

            for (String skill : requiredSkills) {
                if (containsSkill(projectText, normalize(skill))) {
                    relevantProjects.add(project);
                    break;
                }
            }
        }

        return relevantProjects;
    }

    public int getRelevantProjectCount(Candidate candidate, ArrayList<String> requiredSkills) {
        return getRelevantProjects(candidate, requiredSkills).size();
    }

    public List<String> getRelevantProjectNames(Candidate candidate, ArrayList<String> requiredSkills) {
        List<String> names = new ArrayList<>();
        for (Project project : getRelevantProjects(candidate, requiredSkills)) {
            names.add(project.getTitle());
        }
        return names;
    }

    // Relevance is skill coverage across the complete project portfolio.
    // This is more meaningful than simply relevantProjects / totalProjects.
    public double calculateRelevance(Candidate candidate, ArrayList<String> requiredSkills) {
        if (candidate == null || requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        StringBuilder portfolio = new StringBuilder();
        for (Project project : candidate.getProjects()) {
            portfolio.append(project.getTitle()).append(" ")
                    .append(project.getDescription()).append(" ")
                    .append(project.getTechnologies()).append(" ");
        }

        String normalizedPortfolio = normalize(portfolio.toString());
        Set<String> coveredSkills = new HashSet<>();

        for (String skill : requiredSkills) {
            String normalizedSkill = normalize(skill);
            if (containsSkill(normalizedPortfolio, normalizedSkill)) {
                coveredSkills.add(normalizedSkill);
            }
        }

        return (double) coveredSkills.size() / requiredSkills.size() * 100.0;
    }

    private boolean containsSkill(String text, String skill) {
        if (skill.isEmpty()) return false;
        return (" " + text + " ").contains(" " + skill + " ");
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9+#. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
}
