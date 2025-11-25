package com.example.university.model;

import java.util.HashMap;
import java.util.Map;

public class GradeReport {
    private long studentId;
    private int semester;
    private double gpa;
    private int totalCredits;
    private Map<String, Double> gradePointsPerCourse = new HashMap<>();

    public GradeReport(long studentId, int semester, double gpa, int totalCredits) {
        this.studentId = studentId;
        this.semester = semester;
        this.gpa = gpa;
        this.totalCredits = totalCredits;
    }

    public long getStudentId() {
        return studentId;
    }

    public int getSemester() {
        return semester;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Map<String, Double> getGradePointsPerCourse() {
        return gradePointsPerCourse;
    }
}
