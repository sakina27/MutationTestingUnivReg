package com.example.university.service;

import com.example.university.exception.InvalidPrerequisiteException;
import com.example.university.model.Course;
import com.example.university.model.Student;
import com.example.university.repository.CourseRepository;

import java.util.*;

/**
 * Handles prerequisite checks and graph analysis.
 */
public class PrerequisiteService {

    private final CourseRepository courseRepo;

    public PrerequisiteService() {
        this.courseRepo = new CourseRepository();
    }

    public PrerequisiteService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    /** Simple check: does the student satisfy all prerequisites of this course? */
    public boolean hasMetPrerequisites(Student student, Course course) {
        return missingPrerequisites(student, course).isEmpty();
    }

    /**
     * Returns the list of missing prerequisite course codes for the given student/course.
     * This is what RegistrationService expects.
     */
    public List<String> missingPrerequisites(Student student, Course course) {
        List<String> missing = new ArrayList<>();
        Set<String> completed = student.getCompletedCourses();
        for (String prereqCode : course.getPrerequisites()) {
            if (!completed.contains(prereqCode)) {
                missing.add(prereqCode);
            }
        }
        return missing;
    }

    /**
     * Throws an InvalidPrerequisiteException if any prerequisites are missing.
     */
    public void validatePrerequisites(Student student, Course course) {
        List<String> missing = missingPrerequisites(student, course);
        if (!missing.isEmpty()) {
            throw new InvalidPrerequisiteException("Missing prerequisites: " + missing);
        }
    }

    /**
     * Computes the maximum depth of prerequisite chain for a course.
     * Example: CS101 (no prereq) depth=0; CS201->CS101 depth=1; CS301->CS201->CS101 depth=2.
     */
    public int computePrerequisiteDepth(String courseCode) {
        Map<String, Course> allCourses = indexAllCourses();
        Set<String> visited = new HashSet<>();
        return depthDfs(courseCode, allCourses, visited);
    }

    private int depthDfs(String code, Map<String, Course> allCourses, Set<String> visited) {
        Course c = allCourses.get(code);
        if (c == null || c.getPrerequisites().isEmpty()) {
            return 0;
        }
        int max = 0;
        for (String prereq : c.getPrerequisites()) {
            if (!visited.add(prereq)) {
                // already visited in this path - cycle, skip for depth
                continue;
            }
            int d = 1 + depthDfs(prereq, allCourses, visited);
            max = Math.max(max, d);
            visited.remove(prereq);
        }
        return max;
    }

    /**
     * Detects if the prerequisite graph (starting from courseCode) contains a cycle.
     */
    public boolean hasPrerequisiteCycle(String courseCode) {
        Map<String, Course> allCourses = indexAllCourses();
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        return cycleDfs(courseCode, allCourses, visited, stack);
    }

    private boolean cycleDfs(String code,
                             Map<String, Course> allCourses,
                             Set<String> visited,
                             Set<String> stack) {
        if (stack.contains(code)) {
            return true; // back edge → cycle
        }
        if (!visited.add(code)) {
            return false;
        }

        stack.add(code);
        Course c = allCourses.get(code);
        if (c != null) {
            for (String prereq : c.getPrerequisites()) {
                if (cycleDfs(prereq, allCourses, visited, stack)) {
                    return true;
                }
            }
        }
        stack.remove(code);
        return false;
    }

    private Map<String, Course> indexAllCourses() {
        Map<String, Course> map = new HashMap<>();
        for (Course c : courseRepo.findAll()) {
            map.put(c.getCode(), c);
        }
        return map;
    }
}
