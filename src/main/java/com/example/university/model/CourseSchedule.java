package com.example.university.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Timetable information for a particular course in a given semester.
 */
public class CourseSchedule {

    private String courseCode;
    private int semester;
    private List<TimeSlot> slots = new ArrayList<>();

    public CourseSchedule(String courseCode, int semester) {
        this.courseCode = courseCode;
        this.semester = semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getSemester() {
        return semester;
    }

    public List<TimeSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public void addSlot(TimeSlot slot) {
        this.slots.add(slot);
    }
}
