package input;

import java.util.Scanner;

import model.Candidate;
import model.Certification;
import model.Experience;
import model.Project;

public class CandidateInput {

    public Candidate getCandidate(Scanner sc) {

        System.out.println("\n========================================");
        System.out.println("          CANDIDATE DETAILS");
        System.out.println("========================================");

        System.out.println("\nAre you a:");
        System.out.println("1. Fresher");
        System.out.println("2. Experienced Professional");

        int typeChoice;

        while (true) {

            System.out.print("\nEnter your choice: ");
            typeChoice = sc.nextInt();
            sc.nextLine();

            if (typeChoice == 1 || typeChoice == 2) {
                break;
            }

            System.out.println("Invalid choice. Please enter 1 or 2.");
        }

        String candidateType;

        if (typeChoice == 1) {
            candidateType = "Fresher";
        } else {
            candidateType = "Experienced Professional";
        }

        System.out.println("\n----------------------------------------");
        System.out.println("         PERSONAL INFORMATION");
        System.out.println("----------------------------------------");

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.println("\n----------------------------------------");
        System.out.println("             EDUCATION");
        System.out.println("----------------------------------------");

        System.out.print("Enter degree: ");
        String degree = sc.nextLine();

        System.out.print("Enter university: ");
        String university = sc.nextLine();

        System.out.print("Enter graduation year: ");
        int graduationYear = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter CGPA: ");
        double cgpa = sc.nextDouble();
        sc.nextLine();

        Candidate candidate = new Candidate(
                name,
                email,
                age,
                gender,
                candidateType,
                degree,
                university,
                graduationYear,
                cgpa
        );

        // Fresher-specific information
        if (candidateType.equals("Fresher")) {

            System.out.println("\n----------------------------------------");
            System.out.println("          FRESHER PROFILE");
            System.out.println("----------------------------------------");

            // Skills
            System.out.print("Enter number of skills: ");
            int skillCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= skillCount; i++) {

                System.out.print("Enter skill " + i + ": ");
                String skill = sc.nextLine();

                candidate.addSkill(skill);
            }

            // Projects
            System.out.print("\nEnter number of projects: ");
            int projectCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= projectCount; i++) {

                System.out.println("\nProject " + i);

                System.out.print("Enter project title: ");
                String title = sc.nextLine();

                System.out.print("Enter project description: ");
                String description = sc.nextLine();

                System.out.print("Enter technologies used: ");
                String technologies = sc.nextLine();

                Project project = new Project(
                        title,
                        description,
                        technologies
                );

                candidate.addProject(project);
            }

            // Certifications
            System.out.print("\nEnter number of certifications: ");
            int certificationCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= certificationCount; i++) {

                System.out.println("\nCertification " + i);

                System.out.print("Enter certification name: ");
                String certificationName = sc.nextLine();

                System.out.print("Enter issuing organization: ");
                String organization = sc.nextLine();

                Certification certification = new Certification(
                        certificationName,
                        organization
                );

                candidate.addCertification(certification);
            }

            // Internship
            System.out.print("\nDo you have an internship? (Y/N): ");
            String internshipChoice = sc.nextLine();

            if (internshipChoice.equalsIgnoreCase("Y")) {

                System.out.print("Enter internship details: ");
                String internship = sc.nextLine();

                candidate.setInternship(internship);
            }

            // Achievements
            System.out.print("\nDo you have any achievements? (Y/N): ");
            String achievementChoice = sc.nextLine();

            if (achievementChoice.equalsIgnoreCase("Y")) {

                System.out.print("Enter achievement details: ");
                String achievements = sc.nextLine();

                candidate.setAchievements(achievements);
            }

            // Summary
            System.out.print("\nEnter your profile summary: ");
            String summary = sc.nextLine();

            candidate.setSummary(summary);
        }

        // Experienced-specific information
        else {

            System.out.println("\n----------------------------------------");
            System.out.println("       EXPERIENCE INFORMATION");
            System.out.println("----------------------------------------");

            System.out.print("Enter number of previous experiences: ");
            int experienceCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= experienceCount; i++) {

                System.out.println("\nExperience " + i);

                System.out.println("Enter experience details.");
                System.out.println("Example: Software Engineer at Google for 2 years.");

                System.out.print("Experience: ");
                String description = sc.nextLine();

                Experience experience = new Experience(description);

                candidate.addExperience(experience);
            }

            // Skills
            System.out.println("\n----------------------------------------");
            System.out.println("              SKILLS");
            System.out.println("----------------------------------------");

            System.out.print("Enter number of skills: ");
            int skillCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= skillCount; i++) {

                System.out.print("Enter skill " + i + ": ");
                String skill = sc.nextLine();

                candidate.addSkill(skill);
            }

            // Projects
            System.out.print("\nEnter number of projects: ");
            int projectCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= projectCount; i++) {

                System.out.println("\nProject " + i);

                System.out.print("Enter project title: ");
                String title = sc.nextLine();

                System.out.print("Enter project description: ");
                String description = sc.nextLine();

                System.out.print("Enter technologies used: ");
                String technologies = sc.nextLine();

                Project project = new Project(
                        title,
                        description,
                        technologies
                );

                candidate.addProject(project);
            }

            // Certifications
            System.out.print("\nEnter number of certifications: ");
            int certificationCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= certificationCount; i++) {

                System.out.println("\nCertification " + i);

                System.out.print("Enter certification name: ");
                String certificationName = sc.nextLine();

                System.out.print("Enter issuing organization: ");
                String organization = sc.nextLine();

                Certification certification = new Certification(
                        certificationName,
                        organization
                );

                candidate.addCertification(certification);
            }

            // Achievements
            System.out.print("\nDo you have any achievements? (Y/N): ");
            String achievementChoice = sc.nextLine();

            if (achievementChoice.equalsIgnoreCase("Y")) {

                System.out.print("Enter achievement details: ");
                String achievements = sc.nextLine();

                candidate.setAchievements(achievements);
            }

            // Summary
            System.out.print("\nEnter your profile summary: ");
            String summary = sc.nextLine();

            candidate.setSummary(summary);
        }

        return candidate;
    }
}