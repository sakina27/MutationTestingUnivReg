package com.example.university.repository;

import com.example.university.model.Enrollment;
import com.example.university.model.EnrollmentStatus;

import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentRepository {
    private final Map<Long, Enrollment> storage = new HashMap<>();
    private long sequence = 1;

    public Enrollment save(Enrollment e) {
        if (e.getId() == 0) {
            e.setId(sequence++);
        }
        storage.put(e.getId(), e);
        return e;
    }

    public Optional<Enrollment> findById(long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Enrollment> findByStudentAndSemester(long studentId, int semester) {
        return storage.values().stream()
                .filter(e -> e.getStudentId() == studentId && e.getSemester() == semester)
                .collect(Collectors.toList());
    }

    public int countByCourseAndSemester(String courseCode, int semester) {
        return (int) storage.values().stream()
                .filter(e -> e.getCourseCode().equals(courseCode)
                        && e.getSemester() == semester
                        && e.getStatus() == EnrollmentStatus.ENROLLED)
                .count();
    }

    public List<Enrollment> findAll() {
        return new ArrayList<>(storage.values());
    }
}
