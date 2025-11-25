package com.example.university.integration;

import com.example.university.model.*;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.EnrollmentRepository;
import com.example.university.repository.GradeReportRepository;
import com.example.university.service.GPAService;
import com.example.university.service.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPAServiceIT {

    private EnrollmentRepository enrollmentRepo;
    private CourseRepository courseRepo;
    private GradeReportRepository gradeReportRepo;
    private GradeService gradeService;
    private GPAService gpaService;

    @BeforeEach
    void setup() {
        enrollmentRepo = new EnrollmentRepository();
        courseRepo = new CourseRepository();
        gradeReportRepo = new GradeReportRepository();
        gradeService = new GradeService();
        gpaService = new GPAService(enrollmentRepo, courseRepo, gradeService, gradeReportRepo);

        // Create two courses
        Course c1 = new Course();
        c1.setCode("CS101");
        c1.setTitle("Intro");
        c1.setCredits(3);
        courseRepo.save(c1);

        Course c2 = new Course();
        c2.setCode("CS102");
        c2.setTitle("Advanced");
        c2.setCredits(4);
        courseRepo.save(c2);

        // Enrollments for student 1, semester 1
        Enrollment e1 = new Enrollment();
        e1.setStudentId(1L);
        e1.setCourseCode("CS101");
        e1.setSemester(1);
        e1.setStatus(EnrollmentStatus.COMPLETED);
        e1.setGrade("A");
        enrollmentRepo.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setStudentId(1L);
        e2.setCourseCode("CS102");
        e2.setSemester(1);
        e2.setStatus(EnrollmentStatus.COMPLETED);
        e2.setGrade("B");
        enrollmentRepo.save(e2);
    }

    @Test
    void generateGradeReport_correctGpaAndCreditsSaved() {
        GradeReport report = gpaService.generateGradeReport(1L, 1);

        assertNotNull(report);
        assertEquals(1L, report.getStudentId());
        assertEquals(1, report.getSemester());

        // Expected GPA = (3*10 + 4*8)/(3+4) = 62/7
        assertEquals(62.0 / 7.0, report.getGpa(), 0.0001);
        assertEquals(7, report.getTotalCredits());

        GradeReport fromRepo = gradeReportRepo.findByStudentAndSemester(1L, 1)
                .orElseThrow();
        assertEquals(report.getGpa(), fromRepo.getGpa(), 0.0001);
    }
}
