package com.example.university.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GradeReportTest {

    @Test
    void gradePointsPerCourse_returnsMutableMap() {
        GradeReport report = new GradeReport(1L, 1, 8.0, 24);

        // initially empty
        Map<String, Double> map = report.getGradePointsPerCourse();
        assertNotNull(map);
        assertTrue(map.isEmpty());

        // we should be able to put entries into it
        map.put("CS101", 9.0);
        map.put("CS201", 8.0);

        assertEquals(2, map.size());
        assertEquals(9.0, map.get("CS101"));
        assertEquals(8.0, map.get("CS201"));
    }
}
