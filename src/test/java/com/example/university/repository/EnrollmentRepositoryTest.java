package com.example.university.repository;

import com.example.university.model.Course;
import com.example.university.model.Enrollment;
import com.example.university.model.EnrollmentStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentRepositoryTest {

    @Test
    void save_assignsIdAndPersists() {
        EnrollmentRepository repo = new EnrollmentRepository();

        Enrollment e = new Enrollment();
        e.setStudentId(1L);
        e.setCourseCode("CS101");
        e.setSemester(1);
        e.setStatus(EnrollmentStatus.ENROLLED);

        Enrollment saved = repo.save(e);

        assertNotEquals(0, saved.getId());
        assertTrue(repo.findById(saved.getId()).isPresent());
    }

    @Test
    void countByCourseAndSemester_countsOnlyEnrolledInThatSemester() {
        EnrollmentRepository repo = new EnrollmentRepository();

        Enrollment e1 = new Enrollment();
        e1.setStudentId(1L);
        e1.setCourseCode("CS101");
        e1.setSemester(1);
        e1.setStatus(EnrollmentStatus.ENROLLED);
        repo.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setStudentId(2L);
        e2.setCourseCode("CS101");
        e2.setSemester(1);
        e2.setStatus(EnrollmentStatus.DROPPED); // not counted
        repo.save(e2);

        Enrollment e3 = new Enrollment();
        e3.setStudentId(3L);
        e3.setCourseCode("CS101");
        e3.setSemester(2); // different semester
        e3.setStatus(EnrollmentStatus.ENROLLED);
        repo.save(e3);

        int count = repo.countByCourseAndSemester("CS101", 1);
        assertEquals(1, count);
    }

    @Test
    void findByStudentAndSemester_returnsCorrectEnrollments() {
        EnrollmentRepository repo = new EnrollmentRepository();

        Enrollment e1 = new Enrollment();
        e1.setStudentId(1L);
        e1.setCourseCode("CS101");
        e1.setSemester(1);
        e1.setStatus(EnrollmentStatus.ENROLLED);
        repo.save(e1);

        Enrollment e2 = new Enrollment();
        e2.setStudentId(1L);
        e2.setCourseCode("CS102");
        e2.setSemester(2);
        e2.setStatus(EnrollmentStatus.ENROLLED);
        repo.save(e2);

        List<Enrollment> sem1 = repo.findByStudentAndSemester(1L, 1);
        assertEquals(1, sem1.size());
        assertEquals("CS101", sem1.get(0).getCourseCode());
    }
}
