package com.example.university.service;

import com.example.university.model.CourseSchedule;
import com.example.university.model.Enrollment;
import com.example.university.model.EnrollmentStatus;
import com.example.university.model.TimeSlot;
import com.example.university.repository.CourseScheduleRepository;
import com.example.university.repository.EnrollmentRepository;

import java.util.ArrayList;
import java.util.List;

public class TimetableService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseScheduleRepository scheduleRepo;

    public TimetableService(EnrollmentRepository enrollmentRepo,
                            CourseScheduleRepository scheduleRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.scheduleRepo = scheduleRepo;
    }

    /**
     * Checks if enrolling the given student in courseCode/semester would cause a clash
     * with any existing ENROLLED course in the same semester.
     */
    public boolean hasClash(long studentId, String courseCode, int semester) {
        CourseSchedule newSchedule = scheduleRepo
                .findByCourseAndSemester(courseCode, semester)
                .orElseThrow(() -> new IllegalArgumentException("No schedule for course"));

        List<Enrollment> current = enrollmentRepo.findByStudentAndSemester(studentId, semester);
        List<TimeSlot> newSlots = newSchedule.getSlots();

        for (Enrollment e : current) {
            if (e.getStatus() != EnrollmentStatus.ENROLLED) {
                continue;
            }
            CourseSchedule existingSchedule = scheduleRepo
                    .findByCourseAndSemester(e.getCourseCode(), semester)
                    .orElse(null);
            if (existingSchedule == null) {
                continue; // no schedule info, ignore
            }
            for (TimeSlot existing : existingSchedule.getSlots()) {
                for (TimeSlot candidate : newSlots) {
                    if (candidate.clashesWith(existing)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns all slots for a student's ENROLLED courses in a semester.
     */
    public List<TimeSlot> getStudentTimetable(long studentId, int semester) {
        List<Enrollment> current = enrollmentRepo.findByStudentAndSemester(studentId, semester);
        List<TimeSlot> result = new ArrayList<>();

        for (Enrollment e : current) {
            if (e.getStatus() != EnrollmentStatus.ENROLLED) {
                continue;
            }
            CourseSchedule schedule = scheduleRepo
                    .findByCourseAndSemester(e.getCourseCode(), semester)
                    .orElse(null);
            if (schedule != null) {
                result.addAll(schedule.getSlots());
            }
        }
        return result;
    }
}
