package com.example.university.service;

import com.example.university.model.GradeReport;
import com.example.university.model.Student;
import com.example.university.repository.GradeReportRepository;
import com.example.university.repository.StudentRepository;

import java.util.List;

public class StudentService {

    private final StudentRepository studentRepo;
    private final GradeReportRepository gradeReportRepo;

    public StudentService(StudentRepository studentRepo,
                          GradeReportRepository gradeReportRepo) {
        this.studentRepo = studentRepo;
        this.gradeReportRepo = gradeReportRepo;
    }

    /**
     * Simple example: update CGPA as average of all semester GPAs.
     */
    public void updateCgpa(long studentId) {
        Student s = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<GradeReport> reports = gradeReportRepo.findAll();
        double total = 0.0;
        int count = 0;
        for (GradeReport gr : reports) {
            if (gr.getStudentId() == studentId) {
                total += gr.getGpa();
                count++;
            }
        }
        s.setCgpa(count == 0 ? 0.0 : total / count);
        studentRepo.save(s);
    }
}
