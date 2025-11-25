package com.example.university.repository;

import com.example.university.model.GradeReport;

import java.util.*;

public class GradeReportRepository {
    private final Map<String, GradeReport> storage = new HashMap<>();

    private String key(long studentId, int semester) {
        return studentId + "-" + semester;
    }

    public void save(GradeReport report) {
        storage.put(key(report.getStudentId(), report.getSemester()), report);
    }

    public Optional<GradeReport> findByStudentAndSemester(long studentId, int semester) {
        return Optional.ofNullable(storage.get(key(studentId, semester)));
    }

    public List<GradeReport> findAll() {
        return new ArrayList<>(storage.values());
    }
}
