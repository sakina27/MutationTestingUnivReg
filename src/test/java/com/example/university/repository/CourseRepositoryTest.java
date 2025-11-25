package com.example.university.repository;

import com.example.university.model.Course;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryTest {

    @Test
    void saveAndFindByCode_persistsCourse() {
        CourseRepository repo = new CourseRepository();

        Course c = new Course();
        c.setCode("CS101");
        c.setTitle("Intro");
        repo.save(c);

        assertTrue(repo.findByCode("CS101").isPresent());
        assertEquals("Intro", repo.findByCode("CS101").get().getTitle());
    }

    @Test
    void findByCodes_returnsOnlyRequestedCourses() {
        CourseRepository repo = new CourseRepository();

        Course c1 = new Course();
        c1.setCode("CS101");
        repo.save(c1);

        Course c2 = new Course();
        c2.setCode("CS102");
        repo.save(c2);

        Map<String, Course> map = repo.findByCodes(Set.of("CS101"));

        assertEquals(1, map.size());
        assertTrue(map.containsKey("CS101"));
        assertFalse(map.containsKey("CS102"));
    }

    @Test
    void findAll_returnsAllCourses() {
        CourseRepository repo = new CourseRepository();

        Course c1 = new Course();
        c1.setCode("CS101");
        repo.save(c1);

        Course c2 = new Course();
        c2.setCode("CS102");
        repo.save(c2);

        List<Course> all = repo.findAll();
        assertEquals(2, all.size());
    }
}
