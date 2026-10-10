package input;

import java.util.Scanner;

import model.JobDescription;

public class JobDescriptionInput {

    public JobDescription getJobDescription(Scanner sc) {

        System.out.println("\n========================================");
        System.out.println("          JOB DESCRIPTION");
        System.out.println("========================================");

        System.out.println("\n----------------------------------------");
        System.out.println("           BASIC INFORMATION");
        System.out.println("----------------------------------------");

        System.out.print("Enter job title: ");
        String jobTitle = sc.nextLine();

        System.out.print("Enter company name: ");
        String company = sc.nextLine();

        System.out.print("Enter required experience (in years): ");
        double requiredExperience = sc.nextDouble();
        sc.nextLine();

        System.out.println("\n----------------------------------------");
        System.out.println("          REQUIRED SKILLS");
        System.out.println("----------------------------------------");

        System.out.print("Enter number of required skills: ");
        int skillCount = sc.nextInt();
        sc.nextLine();

        JobDescription jobDescription = new JobDescription(
                jobTitle,
                company,
                requiredExperience,
                ""
        );

        for (int i = 1; i <= skillCount; i++) {

            System.out.print("Enter required skill " + i + ": ");
            String skill = sc.nextLine();

            jobDescription.addRequiredSkill(skill);
        }

        System.out.println("\n----------------------------------------");
        System.out.println("         JOB DESCRIPTION TEXT");
        System.out.println("----------------------------------------");

        System.out.print("Enter the complete job description: ");

        String description = sc.nextLine();

        JobDescription finalJobDescription = new JobDescription(
                jobTitle,
                company,
                requiredExperience,
                description
        );

        for (String skill : jobDescription.getRequiredSkills()) {
            finalJobDescription.addRequiredSkill(skill);
        }

        return finalJobDescription;
    }
}