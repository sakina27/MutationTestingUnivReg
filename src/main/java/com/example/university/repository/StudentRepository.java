package com.example.university.repository;

import com.example.university.model.Student;

import java.util.*;

public class StudentRepository {
    private final Map<Long, Student> storage = new HashMap<>();

    public Optional<Student> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void save(Student student) {
        storage.put(student.getId(), student);
    }

    public List<Student> findAll() {
        return new ArrayList<>(storage.values());
    }
}
