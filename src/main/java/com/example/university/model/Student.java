package com.example.university.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Student {
    private long id;
    private String name;
    private int currentSemester;
    private double cgpa;
    private Set<String> completedCourses = new HashSet<>();

    public Student(long id, String name, int currentSemester) {
        this.id = id;
        this.name = name;
        this.currentSemester = currentSemester;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public Set<String> getCompletedCourses() {
        return completedCourses;
    }

    public void addCompletedCourse(String courseCode) {
        completedCourses.add(courseCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
