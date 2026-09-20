package algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GreedySkillPriority {

    public List<String> getMissingSkills(List<String> candidateSkills, List<String> requiredSkills) {
        List<String> missing = new ArrayList<>();
        Set<String> candidateSet = new HashSet<>();

        for (String skill : candidateSkills) {
            candidateSet.add(skill.toLowerCase().trim());
        }

        for (String skill : requiredSkills) {
            if (!candidateSet.contains(skill.toLowerCase().trim())) {
                missing.add(skill);
            }
        }

        return missing;
    }

    public List<String> prioritize(List<String> skills) {
        return prioritize(skills, java.util.Collections.emptyMap());
    }

    // Greedy selection: repeatedly choose the highest-value remaining skill.
    public List<String> prioritize(List<String> skills, Map<String, Integer> weights) {
        List<String> remaining = new ArrayList<>(new LinkedHashSet<>(skills));
        List<String> prioritized = new ArrayList<>();

        while (!remaining.isEmpty()) {
            String best = remaining.get(0);
            int bestWeight = weights.getOrDefault(best.toLowerCase(), 1);

            for (String skill : remaining) {
                int weight = weights.getOrDefault(skill.toLowerCase(), 1);
                if (weight > bestWeight) {
                    best = skill;
                    bestWeight = weight;
                }
            }

            prioritized.add(best);
            remaining.remove(best);
        }

        return prioritized;
    }

    public String getPriorityLevel(int weight) {
        if (weight >= 5) return "HIGH";
        if (weight >= 3) return "MEDIUM";
        return "LOW";
    }
}
