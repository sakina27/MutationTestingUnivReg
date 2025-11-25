package com.example.university.service;

import com.example.university.exception.CourseFullException;
import com.example.university.exception.InvalidPrerequisiteException;
import com.example.university.exception.InvalidSemesterException;
import com.example.university.exception.MaxCreditsExceededException;
import com.example.university.model.*;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.EnrollmentRepository;
import com.example.university.repository.StudentRepository;

import java.util.List;

public class RegistrationService {

    private static final int MAX_CREDITS_PER_SEM = 24;

    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final PrerequisiteService prerequisiteService;

    public RegistrationService(StudentRepository studentRepo,
                               CourseRepository courseRepo,
                               EnrollmentRepository enrollmentRepo,
                               PrerequisiteService prerequisiteService) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.prerequisiteService = prerequisiteService;
    }

    public Enrollment enroll(EnrollmentRequest request) {
        Student student = studentRepo.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Course course = courseRepo.findByCode(request.getCourseCode())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (student.getCurrentSemester() < course.getSemesterOffered()) {
            throw new InvalidSemesterException("Course is for later semester");
        }

        if (!prerequisiteService.hasMetPrerequisites(student, course)) {
            throw new InvalidPrerequisiteException("Missing prerequisites: "
                    + prerequisiteService.missingPrerequisites(student, course));
        }

        int enrolledCount = enrollmentRepo.countByCourseAndSemester(
                course.getCode(), request.getSemester());
        if (enrolledCount >= course.getMaxCapacity()) {
            throw new CourseFullException("Course is full");
        }

        int currentCredits = calculateCurrentCredits(student.getId(), request.getSemester());
        if (currentCredits + course.getCredits() > MAX_CREDITS_PER_SEM) {
            throw new MaxCreditsExceededException("Max credits per semester exceeded");
        }

        Enrollment e = new Enrollment();
        e.setStudentId(student.getId());
        e.setCourseCode(course.getCode());
        e.setSemester(request.getSemester());
        e.setStatus(EnrollmentStatus.ENROLLED);

        return enrollmentRepo.save(e);
    }

    public void drop(long enrollmentId) {
        Enrollment e = enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));

        if (e.getStatus() != EnrollmentStatus.ENROLLED) {
            throw new IllegalStateException("Only ENROLLED enrollments can be dropped");
        }

        e.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepo.save(e);
    }

    private int calculateCurrentCredits(long studentId, int semester) {
        List<Enrollment> enrollments =
                enrollmentRepo.findByStudentAndSemester(studentId, semester);
        int sum = 0;
        for (Enrollment e : enrollments) {
            if (e.getStatus() == EnrollmentStatus.ENROLLED ||
                e.getStatus() == EnrollmentStatus.COMPLETED) {
                Course c = courseRepo.findByCode(e.getCourseCode())
                        .orElseThrow(() -> new IllegalStateException("Course missing"));
                sum += c.getCredits();
            }
        }
        return sum;
    }
}
