package com.example.university.service;

import com.example.university.exception.InvalidPrerequisiteException;
import com.example.university.model.Course;
import com.example.university.model.Student;
import com.example.university.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrerequisiteServiceTest {

    private CourseRepository courseRepo;
    private PrerequisiteService prerequisiteService;

    @BeforeEach
    void setup() {
        courseRepo = new CourseRepository();

        Course cs101 = new Course();
        cs101.setCode("CS101");
        cs101.setPrerequisites(List.of());
        courseRepo.save(cs101);

        Course cs201 = new Course();
        cs201.setCode("CS201");
        cs201.setPrerequisites(List.of("CS101"));
        courseRepo.save(cs201);

        Course cs301 = new Course();
        cs301.setCode("CS301");
        cs301.setPrerequisites(List.of("CS201"));
        courseRepo.save(cs301);

        prerequisiteService = new PrerequisiteService(courseRepo);
    }

    @Test
    void computePrerequisiteDepth_linearChain_returnsCorrectDepth() {
        assertEquals(0, prerequisiteService.computePrerequisiteDepth("CS101"));
        assertEquals(1, prerequisiteService.computePrerequisiteDepth("CS201"));
        assertEquals(2, prerequisiteService.computePrerequisiteDepth("CS301"));
    }

    @Test
    void hasPrerequisiteCycle_detectsCycle() {
        // create cycle: CS101 -> CS301
        Course cs101 = courseRepo.findByCode("CS101").orElseThrow();
        cs101.setPrerequisites(List.of("CS301"));
        courseRepo.save(cs101);

        assertTrue(prerequisiteService.hasPrerequisiteCycle("CS301"));
        assertTrue(prerequisiteService.hasPrerequisiteCycle("CS101"));
    }

    @Test
    void hasPrerequisiteCycle_noCycle_returnsFalse() {
        assertFalse(prerequisiteService.hasPrerequisiteCycle("CS101"));
        assertFalse(prerequisiteService.hasPrerequisiteCycle("CS201"));
        assertFalse(prerequisiteService.hasPrerequisiteCycle("CS301"));
    }

    // keep your existing tests for validatePrerequisites / hasMetPrerequisites...
}
