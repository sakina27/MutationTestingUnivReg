package com.example.university.repository;

import com.example.university.model.Course;

import java.util.*;
import java.util.stream.Collectors;

public class CourseRepository {
    private final Map<String, Course> storage = new HashMap<>();

    public void save(Course course) {
        storage.put(course.getCode(), course);
    }

    public Optional<Course> findByCode(String code) {
        return Optional.ofNullable(storage.get(code));
    }

    public Map<String, Course> findByCodes(Set<String> codes) {
        return storage.entrySet().stream()
                .filter(e -> codes.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Course> findAll() {
        return new ArrayList<>(storage.values());
    }
}
