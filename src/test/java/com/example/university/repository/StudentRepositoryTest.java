package com.example.university.repository;

import com.example.university.model.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    @Test
    void saveAndFindById_persistsStudent() {
        StudentRepository repo = new StudentRepository();

        Student s = new Student(1L, "Alice", 3);
        repo.save(s);

        assertTrue(repo.findById(1L).isPresent());
        assertEquals("Alice", repo.findById(1L).get().getName());
    }

    @Test
    void findAll_returnsAllStudents() {
        StudentRepository repo = new StudentRepository();

        repo.save(new Student(1L, "Alice", 3));
        repo.save(new Student(2L, "Bob", 5));

        List<Student> all = repo.findAll();
        assertEquals(2, all.size());
    }
}
