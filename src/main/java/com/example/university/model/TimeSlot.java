package com.example.university.model;

import java.time.DayOfWeek;
import java.util.Objects;

/**
 * Represents a single teaching slot for a course.
 * Example: MONDAY 10:00–12:00.
 */
public class TimeSlot {

    private DayOfWeek day;
    // Represent time as hour of day in 24h format for simplicity
    private int startHour;
    private int endHour;

    public TimeSlot(DayOfWeek day, int startHour, int endHour) {
        if (startHour < 0 || startHour > 23 || endHour < 1 || endHour > 24 || startHour >= endHour) {
            throw new IllegalArgumentException("Invalid time range");
        }
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    /**
     * Returns true if this slot overlaps with another slot on the same day.
     */
    public boolean clashesWith(TimeSlot other) {
        if (this.day != other.day) {
            return false;
        }
        // [start, end) intervals overlap?
        return this.startHour < other.endHour && other.startHour < this.endHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return startHour == timeSlot.startHour &&
                endHour == timeSlot.endHour &&
                day == timeSlot.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startHour, endHour);
    }

    @Override
    public String toString() {
        return day + " " + startHour + ":00-" + endHour + ":00";
    }
}
