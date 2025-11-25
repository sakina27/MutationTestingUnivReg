package com.example.university.service;

import com.example.university.model.GradeReport;
import com.example.university.model.Student;
import com.example.university.repository.GradeReportRepository;
import com.example.university.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentRepository studentRepo;
    private GradeReportRepository gradeRepo;
    private StudentService studentService;

    @BeforeEach
    void setup() {
        studentRepo = new StudentRepository();
        gradeRepo = new GradeReportRepository();
        studentService = new StudentService(studentRepo, gradeRepo);

        // base student
        Student s = new Student(1L, "Alice", 3);
        studentRepo.save(s);
    }

    @Test
    void updateCgpa_validReports_updatesCorrectCgpa() {
        // Two grade reports for the same student, different semesters
        GradeReport r1 = new GradeReport(1L, 1, 3.0, 20);
        GradeReport r2 = new GradeReport(1L, 2, 7.0, 22);
        gradeRepo.save(r1);
        gradeRepo.save(r2);

        studentService.updateCgpa(1L);

        Student updated = studentRepo.findById(1L).orElseThrow();
        double expectedCgpa = (3.0 + 7.0) / 2.0;

        assertEquals(expectedCgpa, updated.getCgpa(), 0.0001);
    }

    @Test
    void updateCgpa_noReports_setsCgpaToZero() {
        studentService.updateCgpa(1L);

        Student updated = studentRepo.findById(1L).orElseThrow();
        assertEquals(0.0, updated.getCgpa(), 0.0001);
    }

    @Test
    void updateCgpa_studentNotFound_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> studentService.updateCgpa(999L));
    }
}
