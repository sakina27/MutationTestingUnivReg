package com.example.university.service;

public class GradeService {

    public String assignGrade(int marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks out of range");
        }
        if (marks >= 90) return "A";
        if (marks >= 80) return "B";
        if (marks >= 70) return "C";
        if (marks >= 60) return "D";
        if (marks >= 50) return "E";
        return "F";
    }

    public double gradeToPoints(String grade) {
        switch (grade) {
            case "A": return 10.0;
            case "B": return 8.0;
            case "C": return 6.0;
            case "D": return 4.0;
            case "E": return 2.0;
            case "F": return 0.0;
            default: throw new IllegalArgumentException("Unknown grade: " + grade);
        }
    }
}
