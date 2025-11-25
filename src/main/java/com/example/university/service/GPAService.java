package com.example.university.service;

import com.example.university.model.Course;
import com.example.university.model.Enrollment;
import com.example.university.model.EnrollmentStatus;
import com.example.university.model.GradeReport;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.EnrollmentRepository;
import com.example.university.repository.GradeReportRepository;

import java.util.*;
import java.util.stream.Collectors;

public class GPAService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final GradeService gradeService;
    private final GradeReportRepository gradeReportRepo;

    public GPAService(EnrollmentRepository enrollmentRepo,
                      CourseRepository courseRepo,
                      GradeService gradeService,
                      GradeReportRepository gradeReportRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.courseRepo = courseRepo;
        this.gradeService = gradeService;
        this.gradeReportRepo = gradeReportRepo;
    }

    public double calculateGPA(List<Enrollment> enrollments, Map<String, Course> courseMap) {
        int totalCredits = 0;
        double totalPoints = 0.0;

        for (Enrollment e : enrollments) {
            if (e.getStatus() != EnrollmentStatus.COMPLETED || e.getGrade() == null) {
                continue;
            }
            Course c = courseMap.get(e.getCourseCode());
            int credits = c.getCredits();
            double points = gradeService.gradeToPoints(e.getGrade());

            totalCredits += credits;
            totalPoints += credits * points;
        }

        if (totalCredits == 0) {
            return 0.0;
        }
        return totalPoints / totalCredits;
    }

    public GradeReport generateGradeReport(long studentId, int semester) {
        List<Enrollment> enrollments = enrollmentRepo
                .findByStudentAndSemester(studentId, semester);

        Set<String> codes = enrollments.stream()
                .map(Enrollment::getCourseCode)
                .collect(Collectors.toSet());

        Map<String, Course> courseMap = courseRepo.findByCodes(codes);

        double gpa = calculateGPA(enrollments, courseMap);

        int totalCredits = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .mapToInt(e -> courseMap.get(e.getCourseCode()).getCredits())
                .sum();

        GradeReport report = new GradeReport(studentId, semester, gpa, totalCredits);
        gradeReportRepo.save(report);
        return report;
    }
}
