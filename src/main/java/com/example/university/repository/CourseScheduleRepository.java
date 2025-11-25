package com.example.university.repository;

import com.example.university.model.CourseSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Simple in-memory repository for course schedules.
 */
public class CourseScheduleRepository {

    private final List<CourseSchedule> schedules = new ArrayList<>();

    public void save(CourseSchedule schedule) {
        // replace if existing same (course, semester), else add
        schedules.removeIf(s -> s.getCourseCode().equals(schedule.getCourseCode())
                && s.getSemester() == schedule.getSemester());
        schedules.add(schedule);
    }

    public Optional<CourseSchedule> findByCourseAndSemester(String code, int semester) {
        return schedules.stream()
                .filter(s -> s.getCourseCode().equals(code) && s.getSemester() == semester)
                .findFirst();
    }

    public List<CourseSchedule> findAllBySemester(int semester) {
        return schedules.stream()
                .filter(s -> s.getSemester() == semester)
                .collect(Collectors.toList());
    }
}
