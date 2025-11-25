package com.example.university.service;

import com.example.university.model.Course;
import com.example.university.model.Enrollment;
import com.example.university.model.EnrollmentStatus;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.EnrollmentRepository;
import com.example.university.repository.GradeReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GPAServiceTest {

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
    }

    @Test
    void calculateGPA_mixedGrades_weightedByCredits() {
        // courses
        Course c1 = new Course();
        c1.setCode("CS101");
        c1.setCredits(3);
        courseRepo.save(c1);

        Course c2 = new Course();
        c2.setCode("CS102");
        c2.setCredits(4);
        courseRepo.save(c2);

        // enrollments
        Enrollment e1 = new Enrollment();
        e1.setCourseCode("CS101");
        e1.setStatus(EnrollmentStatus.COMPLETED);
        e1.setGrade("A");

        Enrollment e2 = new Enrollment();
        e2.setCourseCode("CS102");
        e2.setStatus(EnrollmentStatus.COMPLETED);
        e2.setGrade("B");

        List<Enrollment> list = List.of(e1, e2);
        Map<String, Course> map = courseRepo.findByCodes(Set.of("CS101", "CS102"));

        double gpa = gpaService.calculateGPA(list, map);

        // GPA = (3*10 + 4*8) / (3+4) = (30+32)/7 = 62/7 ≈ 8.857
        assertEquals(62.0 / 7.0, gpa, 0.0001);
    }

    @Test
    void calculateGPA_noCompletedCourses_returnsZero() {
        List<Enrollment> list = List.of(); // empty
        Map<String, Course> map = Map.of();
        double gpa = gpaService.calculateGPA(list, map);
        assertEquals(0.0, gpa, 0.0001);
    }
}
