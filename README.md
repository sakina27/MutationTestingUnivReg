# University Course Registration System – Mutation Testing Report

This project implements a complete **University Course Registration System** with modules for students, courses, enrollment, grade report management, prerequisites, timetables, and clash detection.  
The main objective of this work is to design the system and evaluate its test quality using **mutation testing** (PIT).

---

## Project Modules

- **Model Layer**  
  Student, Course, Enrollment, GradeReport, EnrollmentRequest, TimeSlot, CourseSchedule

- **Repository Layer**  
  In-memory repositories for storing students, courses, enrollments, grade reports, timetables

- **Service Layer**  
  - `RegistrationService` – Enrollment & drop workflows  
  - `GPAService` – GPA & CGPA calculations  
  - `PrerequisiteService` – Prerequisite depth & missing checks  
  - `TimetableService` – Clash detection & scheduling  
  - `GradeService` – Grade assignment & validation  
  - `StudentService` – CGPA update  

---

## Testing Performed

### **Unit Testing**
Unit tests were written for:

- All **model classes** (getters, setters, equals, hashCode, toString)
- All **service classes**, covering:
  - Grade assignment
  - GPA & CGPA calculation
  - Prerequisite validation
  - Timetable clash detection
- All **repositories**
- Utility classes

### **Integration Testing**
Integration tests covered complete workflows:

- Successful enrollment  
- Enrollment with missing prerequisites  
- Course capacity limits  
- Semester mismatch  
- Timetable clash failures  
- GPA + CGPA update  
- Combined workflow: **Enrollment + Prerequisite + Timetable**

---

## Mutation Testing (PIT)

Mutation testing was performed for both **unit** and **integration** tests.  
The goal was to measure the ability of our tests to detect faulty versions of code (mutants).

### **Final Mutation Testing Results**
- **Total classes mutated:** 18  
- **Line coverage:** **94%** (356/378)  
- **Mutation coverage:** **87%** (173/199 mutants killed)  
- **Test strength:** 92%  
- **Total mutants generated:** 199  
- **Mutants killed:** 173  
- **Mutants survived:** 11  

### Strongly-Killed Mutators (Examples)
Below are examples of mutation operators that were strongly killed by our tests:

- `CONDITIONALS_BOUNDARY`  
- `INVERT_NEGS`  
- `RETURN_VALS`  
- `VOID_METHOD_CALL_MUTATOR`  
- `PRIMITIVE_RETURNS_MUTATOR`  
- `BOOLEAN_FALSE_RETURN_MUTATOR`  
- `MATH_MUTATOR`

These indicate that the test suite is effective at catching incorrect logic, wrong returns, removed conditions, and altered computations.

---

## PIT Console Logs (Important Part of the Report)

Example statistics from PIT run:

![Build Logs](/build-success.png)

PIT also lists every mutator used such as:

![Mutators Statistics](/mutator-stats.png)

These logs confirm the depth of mutation analysis performed on the system.

### PIT HTML Dashboard  
![PIT-Summary](/pit-summary.png)

### PIT Model Coverage  
![Model-Coverage](/model-coverage.png)

### PIT Service Coverage  
![Service-Coverage](/service-coverage.png)

### PIT Repository Coverage
![Repository-Coverage](/repository-coverage.png)

---

## 📄 Full LaTeX Mutation Testing Report

The complete mutation testing report is available inside the project under: ![Project Report](/SWTProjectReport.pdf)

## References

- https://www.geeksforgeeks.org  
- https://www.javatpoint.com  
- https://pitest.org 

## Authors
- **Sakina Baranwala (MT2024130)**  
- **Yashraj Singh Chauhan (MT2024170)**
