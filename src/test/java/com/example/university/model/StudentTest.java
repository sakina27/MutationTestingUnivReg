package com.example.university.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void gettersAndSetters_workCorrectly() {
        Student s = new Student(1L, "Alice", 3);

        // id setter + getter
        s.setId(10L);
        assertEquals(10L, s.getId());

        // name setter + getter
        s.setName("Bob");
        assertEquals("Bob", s.getName());

        // semester setter + getter
        s.setCurrentSemester(5);
        assertEquals(5, s.getCurrentSemester());

        // cgpa setter + getter
        s.setCgpa(9.1);
        assertEquals(9.1, s.getCgpa(), 0.00001);
    }

    @Test
    void completedCourses_addsAndReturnsCorrectly() {
        Student s = new Student(1L, "Alice", 3);
        assertTrue(s.getCompletedCourses().isEmpty());

        s.addCompletedCourse("CS101");
        s.addCompletedCourse("CS102");

        Set<String> courses = s.getCompletedCourses();
        assertEquals(2, courses.size());
        assertTrue(courses.contains("CS101"));
        assertTrue(courses.contains("CS102"));
    }

    @Test
    void equals_sameObject_returnsTrue() {
        Student s = new Student(1L, "Alice", 3);
        assertEquals(s, s); // reflexive
    }

    @Test
    void equals_sameIdDifferentData_returnsTrue() {
        Student s1 = new Student(1L, "Alice", 3);
        Student s2 = new Student(1L, "Bob", 5);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void equals_differentId_returnsFalse() {
        Student s1 = new Student(1L, "Alice", 3);
        Student s2 = new Student(2L, "Alice", 3);

        assertNotEquals(s1, s2);
    }

    @Test
    void equals_withNullOrDifferentObject_returnsFalse() {
        Student s = new Student(1L, "Alice", 3);

        assertNotEquals(s, null);
        assertNotEquals(s, "NotAStudent");
    }

    @Test
    void hashCode_dependsOnlyOnId() {
        Student s1 = new Student(5L, "A", 1);
        Student s2 = new Student(5L, "B", 99);

        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
