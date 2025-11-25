package com.example.university.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.*;

class TimeSlotTest {

    // ---------- constructor boundary tests ----------

    @Test
    void constructor_allowsValidBoundaryValues() {
        // lowest valid start / end
        assertDoesNotThrow(() -> new TimeSlot(DayOfWeek.MONDAY, 0, 1));
        // highest valid start / end
        assertDoesNotThrow(() -> new TimeSlot(DayOfWeek.FRIDAY, 23, 24));
    }

    @Test
    void constructor_rejectsInvalidHoursAndOrder() {
        // start < 0
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlot(DayOfWeek.MONDAY, -1, 10));
        // start > 23
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlot(DayOfWeek.MONDAY, 24, 25));
        // end < 1
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlot(DayOfWeek.MONDAY, 0, 0));
        // end > 24
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlot(DayOfWeek.MONDAY, 10, 25));
        // start == end
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlot(DayOfWeek.MONDAY, 10, 10));
        // start > end
        assertThrows(IllegalArgumentException.class,
                () -> new TimeSlot(DayOfWeek.MONDAY, 15, 10));
    }

    // ---------- getters ----------

    @Test
    void getters_returnCorrectFields() {
        TimeSlot slot = new TimeSlot(DayOfWeek.TUESDAY, 9, 11);

        assertEquals(DayOfWeek.TUESDAY, slot.getDay());
        assertEquals(9, slot.getStartHour());
        assertEquals(11, slot.getEndHour());
    }

    // ---------- clashesWith boundary tests ----------

    @Test
    void clashesWith_overlappingSameDay_returnsTrue() {
        TimeSlot a = new TimeSlot(DayOfWeek.MONDAY, 10, 12);
        TimeSlot b = new TimeSlot(DayOfWeek.MONDAY, 11, 13); // overlaps 11–12

        assertTrue(a.clashesWith(b));
        assertTrue(b.clashesWith(a));
    }

    @Test
    void clashesWith_nestedInterval_returnsTrue() {
        TimeSlot outer = new TimeSlot(DayOfWeek.TUESDAY, 9, 13);
        TimeSlot inner = new TimeSlot(DayOfWeek.TUESDAY, 10, 12);

        assertTrue(outer.clashesWith(inner));
        assertTrue(inner.clashesWith(outer));
    }

    @Test
    void clashesWith_justTouchingEdges_returnsFalse() {
        TimeSlot a = new TimeSlot(DayOfWeek.WEDNESDAY, 10, 12);
        TimeSlot b = new TimeSlot(DayOfWeek.WEDNESDAY, 12, 14); // starts exactly at a.end

        assertFalse(a.clashesWith(b));
        assertFalse(b.clashesWith(a));
    }

    @Test
    void clashesWith_differentDays_returnsFalse() {
        TimeSlot a = new TimeSlot(DayOfWeek.THURSDAY, 10, 12);
        TimeSlot b = new TimeSlot(DayOfWeek.FRIDAY, 10, 12);

        assertFalse(a.clashesWith(b));
        assertFalse(b.clashesWith(a));
    }

    // ---------- equals / hashCode / toString ----------

    @Test
    void equals_reflexive_sameInstanceIsEqual() {
        TimeSlot a = new TimeSlot(DayOfWeek.MONDAY, 10, 12);
        assertTrue(a.equals(a)); // hits the "if (this == o) return true" branch
    }

    @Test
    void equalsAndHashCode_sameFields_areEqual() {
        TimeSlot a = new TimeSlot(DayOfWeek.MONDAY, 10, 12);
        TimeSlot b = new TimeSlot(DayOfWeek.MONDAY, 10, 12);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsAndHashCode_differentFields_areNotEqual() {
        TimeSlot base = new TimeSlot(DayOfWeek.MONDAY, 10, 12);
        TimeSlot different = new TimeSlot(DayOfWeek.TUESDAY, 10, 12);

        assertNotEquals(base, different);
        // This should fail if hashCode() is mutated to always 0
        assertNotEquals(base.hashCode(), different.hashCode());
    }

    @Test
    void equals_withNullOrDifferentType_returnsFalse() {
        TimeSlot a = new TimeSlot(DayOfWeek.MONDAY, 10, 12);

        assertNotEquals(a, null);
        assertNotEquals(a, "not a timeslot");
    }

    @Test
    void toString_containsDayAndHours() {
        TimeSlot a = new TimeSlot(DayOfWeek.MONDAY, 10, 12);
        String s = a.toString();

        assertTrue(s.contains("MONDAY"));
        assertTrue(s.contains("10:00"));
        assertTrue(s.contains("12:00"));
    }
}
