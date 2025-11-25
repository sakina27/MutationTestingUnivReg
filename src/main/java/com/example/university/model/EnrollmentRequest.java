package com.example.university.model;

public class EnrollmentRequest {
    private long studentId;
    private String courseCode;
    private int semester;

    public EnrollmentRequest(long studentId, String courseCode, int semester) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.semester = semester;
    }

    public long getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getSemester() {
        return semester;
    }
}
