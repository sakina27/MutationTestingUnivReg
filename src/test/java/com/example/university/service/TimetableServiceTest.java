package com.example.university.service;

import com.example.university.model.*;
import com.example.university.repository.CourseScheduleRepository;
import com.example.university.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimetableServiceTest {

    private EnrollmentRepository enrollmentRepo;
    private CourseScheduleRepository scheduleRepo;
    private TimetableService timetableService;

    @BeforeEach
    void setup() {
        enrollmentRepo = new EnrollmentRepository();
        scheduleRepo = new CourseScheduleRepository();
        timetableService = new TimetableService(enrollmentRepo, scheduleRepo);

        // Schedules for two courses in semester 3
        CourseSchedule cs201 = new CourseSchedule("CS201", 3);
        cs201.addSlot(new TimeSlot(DayOfWeek.MONDAY, 10, 12));
        cs201.addSlot(new TimeSlot(DayOfWeek.WEDNESDAY, 10, 12));
        scheduleRepo.save(cs201);

        CourseSchedule cs301 = new CourseSchedule("CS301", 3);
        cs301.addSlot(new TimeSlot(DayOfWeek.MONDAY, 11, 13)); // overlaps Monday 10–12
        scheduleRepo.save(cs301);

        CourseSchedule cs401 = new CourseSchedule("CS401", 3);
        cs401.addSlot(new TimeSlot(DayOfWeek.TUESDAY, 14, 16)); // no clash
        scheduleRepo.save(cs401);

        // baseline enrollment: student 1 already in CS201
        Enrollment e = new Enrollment();
        e.setStudentId(1L);
        e.setCourseCode("CS201");
        e.setSemester(3);
        e.setStatus(EnrollmentStatus.ENROLLED);
        enrollmentRepo.save(e);
    }

    @Test
    void hasClash_withOverlappingCourse_returnsTrue() {
        boolean clash = timetableService.hasClash(1L, "CS301", 3);
        assertTrue(clash);
    }

    @Test
    void hasClash_withNonOverlappingCourse_returnsFalse() {
        boolean clash = timetableService.hasClash(1L, "CS401", 3);
        assertFalse(clash);
    }

    @Test
    void getStudentTimetable_returnsAllSlots() {
        List<TimeSlot> slots = timetableService.getStudentTimetable(1L, 3);
        assertEquals(2, slots.size()); // CS201 has two slots
    }

    @Test
    void hasClash_noExistingEnrollments_returnsFalse() {
        boolean clash = timetableService.hasClash(99L, "CS401", 3);
        assertFalse(clash);
    }
}
