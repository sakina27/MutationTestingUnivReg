package com.example.university.integration;

import com.example.university.exception.CourseFullException;
import com.example.university.exception.InvalidPrerequisiteException;
import com.example.university.exception.InvalidSemesterException;
import com.example.university.exception.MaxCreditsExceededException;
import com.example.university.model.*;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.EnrollmentRepository;
import com.example.university.repository.StudentRepository;
import com.example.university.service.PrerequisiteService;
import com.example.university.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceIT {

    private StudentRepository studentRepo;
    private CourseRepository courseRepo;
    private EnrollmentRepository enrollmentRepo;
    private PrerequisiteService prerequisiteService;
    private RegistrationService registrationService;

    @BeforeEach
    void setup() {
        studentRepo = new StudentRepository();
        courseRepo = new CourseRepository();
        enrollmentRepo = new EnrollmentRepository();
        prerequisiteService = new PrerequisiteService();
        registrationService = new RegistrationService(
                studentRepo, courseRepo, enrollmentRepo, prerequisiteService);

        // base student
        Student s = new Student(1L, "Alice", 3);
        s.addCompletedCourse("CS101");
        studentRepo.save(s);

        // main course CS201
        Course c = new Course();
        c.setCode("CS201");
        c.setTitle("Data Structures");
        c.setCredits(4);
        c.setMaxCapacity(1);
        c.setSemesterOffered(3);
        c.setPrerequisites(List.of("CS101"));
        courseRepo.save(c);
    }

    // ---------- Happy path ----------

    @Test
    void enroll_validScenario_savesEnrollment() {
        EnrollmentRequest req = new EnrollmentRequest(1L, "CS201", 3);
        Enrollment e = registrationService.enroll(req);

        assertNotEquals(0, e.getId());
        Optional<Enrollment> fromRepo = enrollmentRepo.findById(e.getId());
        assertTrue(fromRepo.isPresent());
        assertEquals(EnrollmentStatus.ENROLLED, fromRepo.get().getStatus());
    }

    // ---------- Error cases for enroll() ----------

    @Test
    void enroll_studentNotFound_throwsException() {
        // studentId 999 does not exist
        EnrollmentRequest req = new EnrollmentRequest(999L, "CS201", 3);

        assertThrows(IllegalArgumentException.class,
                () -> registrationService.enroll(req));
    }

    @Test
    void enroll_courseNotFound_throwsException() {
        // course code UNKNOWN does not exist
        EnrollmentRequest req = new EnrollmentRequest(1L, "UNKNOWN", 3);

        assertThrows(IllegalArgumentException.class,
                () -> registrationService.enroll(req));
    }

    @Test
    void enroll_invalidSemester_throwsException() {
        // student in too early semester for CS201
        Student s = studentRepo.findById(1L).orElseThrow();
        s.setCurrentSemester(1);
        studentRepo.save(s);

        EnrollmentRequest req = new EnrollmentRequest(1L, "CS201", 3);

        assertThrows(InvalidSemesterException.class,
                () -> registrationService.enroll(req));
    }

    @Test
    void enroll_missingPrerequisite_throwsAndNotSaved() {
        // remove student's completed course to break prerequisite
        Student s = studentRepo.findById(1L).orElseThrow();
        s.getCompletedCourses().clear();
        studentRepo.save(s);

        EnrollmentRequest req = new EnrollmentRequest(1L, "CS201", 3);

        assertThrows(InvalidPrerequisiteException.class,
                () -> registrationService.enroll(req));

        assertEquals(0, enrollmentRepo.countByCourseAndSemester("CS201", 3));
    }

    @Test
    void enroll_courseFull_throwsExceptionAndDoesNotOverbook() {
        // first valid enrollment fills capacity = 1
        registrationService.enroll(new EnrollmentRequest(1L, "CS201", 3));

        // second student who also satisfies prereqs
        Student s2 = new Student(2L, "Bob", 3);
        s2.addCompletedCourse("CS101");
        studentRepo.save(s2);

        EnrollmentRequest req2 = new EnrollmentRequest(2L, "CS201", 3);

        assertThrows(CourseFullException.class,
                () -> registrationService.enroll(req2));

        // ensure we didn't overbook
        assertEquals(1, enrollmentRepo.countByCourseAndSemester("CS201", 3));
    }

    @Test
    void enroll_exceedsMaxCredits_throwsException() {
        // Create another heavy course to consume credits
        Course heavy = new Course();
        heavy.setCode("HEAVY");
        heavy.setTitle("Heavy Course");
        heavy.setCredits(22);
        heavy.setMaxCapacity(10);
        heavy.setSemesterOffered(3);
        courseRepo.save(heavy);

        // Existing enrollment that already uses 22 credits
        Enrollment e = new Enrollment();
        e.setStudentId(1L);
        e.setCourseCode("HEAVY");
        e.setSemester(3);
        e.setStatus(EnrollmentStatus.ENROLLED);
        enrollmentRepo.save(e);

        // Now trying to enroll into CS201 (4 credits) should exceed MAX_CREDITS_PER_SEM = 24
        EnrollmentRequest req = new EnrollmentRequest(1L, "CS201", 3);

        assertThrows(MaxCreditsExceededException.class,
                () -> registrationService.enroll(req));
    }

    // ---------- drop() behaviour ----------

    @Test
    void drop_enrollment_updatesStatusInRepository() {
        EnrollmentRequest req = new EnrollmentRequest(1L, "CS201", 3);
        Enrollment e = registrationService.enroll(req);

        registrationService.drop(e.getId());

        Enrollment fromRepo = enrollmentRepo.findById(e.getId()).orElseThrow();
        assertEquals(EnrollmentStatus.DROPPED, fromRepo.getStatus());
    }

    @Test
    void drop_enrollmentNotFound_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> registrationService.drop(999L));
    }

    @Test
    void drop_notEnrolledStatus_throwsException() {
        EnrollmentRequest req = new EnrollmentRequest(1L, "CS201", 3);
        Enrollment e = registrationService.enroll(req);

        // manually mark as COMPLETED, not ENROLLED
        e.setStatus(EnrollmentStatus.COMPLETED);
        enrollmentRepo.save(e);

        assertThrows(IllegalStateException.class,
                () -> registrationService.drop(e.getId()));
    }
}
