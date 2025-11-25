package com.example.university.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeServiceTest {

    private final GradeService gradeService = new GradeService();

    // ✅ Covers ALL grade boundaries + ranges
    @Test
    void assignGrade_allBoundariesAndRanges() {
        // A
        assertEquals("A", gradeService.assignGrade(100));
        assertEquals("A", gradeService.assignGrade(95));
        assertEquals("A", gradeService.assignGrade(90));

        // B
        assertEquals("B", gradeService.assignGrade(89));
        assertEquals("B", gradeService.assignGrade(85));
        assertEquals("B", gradeService.assignGrade(80));

        // C
        assertEquals("C", gradeService.assignGrade(79));
        assertEquals("C", gradeService.assignGrade(75));
        assertEquals("C", gradeService.assignGrade(70));

        // D
        assertEquals("D", gradeService.assignGrade(69));
        assertEquals("D", gradeService.assignGrade(65));
        assertEquals("D", gradeService.assignGrade(60));

        // E
        assertEquals("E", gradeService.assignGrade(59));
        assertEquals("E", gradeService.assignGrade(55));
        assertEquals("E", gradeService.assignGrade(50));

        // F
        assertEquals("F", gradeService.assignGrade(49));
        assertEquals("F", gradeService.assignGrade(10));
        assertEquals("F", gradeService.assignGrade(0));
    }

    // ✅ Tests out-of-range marks → exception
    @Test
    void assignGrade_invalidMarks_throwException() {
        assertThrows(IllegalArgumentException.class,
                () -> gradeService.assignGrade(-1));
        assertThrows(IllegalArgumentException.class,
                () -> gradeService.assignGrade(101));
    }

    // ✅ Covers ALL switch cases in gradeToPoints
    @Test
    void gradeToPoints_allGrades() {
        assertEquals(10.0, gradeService.gradeToPoints("A"));
        assertEquals(8.0, gradeService.gradeToPoints("B"));
        assertEquals(6.0, gradeService.gradeToPoints("C"));
        assertEquals(4.0, gradeService.gradeToPoints("D"));
        assertEquals(2.0, gradeService.gradeToPoints("E"));
        assertEquals(0.0, gradeService.gradeToPoints("F"));
    }

    // ✅ Ensures default branch throws for invalid grade
    @Test
    void gradeToPoints_invalidGrade_throws() {
        assertThrows(IllegalArgumentException.class,
                () -> gradeService.gradeToPoints("Z"));
    }
}
