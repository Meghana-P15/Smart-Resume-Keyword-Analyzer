package main;

import java.util.Scanner;

import analysis.Analyzer;
import input.CandidateInput;
import input.JobDescriptionInput;
import model.Candidate;
import model.JobDescription;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Candidate candidate = null;
        JobDescription jobDescription = null;

        int choice;

        do {

            System.out.println("\n========================================");
            System.out.println("        SMART RESUME ANALYZER");
            System.out.println("========================================");
            System.out.println("1. Enter Candidate Details");
            System.out.println("2. Enter Job Description");
            System.out.println("3. Analyze Resume");
            System.out.println("4. Exit");
            System.out.println("========================================");

            System.out.print("Enter your choice: ");

            // Prevent InputMismatchException
            while (!sc.hasNextInt()) {

                System.out.println(
                        "Invalid input. Please enter a number."
                );

                sc.nextLine();

                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // =====================================
                // CASE 1: CANDIDATE DETAILS
                // =====================================

                case 1:

                    CandidateInput candidateInput =
                            new CandidateInput();

                    candidate =
                            candidateInput.getCandidate(sc);

                    System.out.println(
                            "\nCandidate details saved successfully!"
                    );

                    break;

                // =====================================
                // CASE 2: JOB DESCRIPTION
                // =====================================

                case 2:

                    JobDescriptionInput jobDescriptionInput =
                            new JobDescriptionInput();

                    jobDescription =
                            jobDescriptionInput
                                    .getJobDescription(sc);

                    System.out.println(
                            "\nJob description saved successfully!"
                    );

                    break;

                // =====================================
                // CASE 3: ANALYZE RESUME
                // =====================================

                case 3:

                    if (candidate == null ||
                            jobDescription == null) {

                        System.out.println(
                                "\n========================================"
                        );

                        System.out.println(
                                "          ANALYSIS NOT READY"
                        );

                        System.out.println(
                                "========================================"
                        );

                        if (candidate == null) {

                            System.out.println(
                                    "Candidate details are missing."
                            );
                        }

                        if (jobDescription == null) {

                            System.out.println(
                                    "Job description is missing."
                            );
                        }

                        System.out.println(
                                "\nPlease enter the required details first."
                        );

                    } else {

                        System.out.println(
                                "\n========================================"
                        );

                        System.out.println(
                                "          STARTING ANALYSIS"
                        );

                        System.out.println(
                                "========================================"
                        );

                        Analyzer analyzer =
                                new Analyzer();

                        analyzer.analyze(
                                candidate,
                                jobDescription
                        );
                    }

                    break;

                // =====================================
                // CASE 4: EXIT
                // =====================================

                case 4:

                    System.out.println(
                            "\n========================================"
                    );

                    System.out.println(
                            "Thank you for using Smart Resume Analyzer!"
                    );

                    System.out.println(
                            "========================================"
                    );

                    break;

                // =====================================
                // INVALID CHOICE
                // =====================================

                default:

                    System.out.println(
                            "\nInvalid choice. Please try again."
                    );
            }

        } while (choice != 4);

        sc.close();
    }
}