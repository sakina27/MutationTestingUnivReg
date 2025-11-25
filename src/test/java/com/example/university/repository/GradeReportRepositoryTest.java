package com.example.university.repository;

import com.example.university.model.GradeReport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeReportRepositoryTest {

    @Test
    void saveAndFindByStudentAndSemester_persistsReport() {
        GradeReportRepository repo = new GradeReportRepository();

        GradeReport r = new GradeReport(1L, 1, 8.0, 24);
        repo.save(r);

        GradeReport fromRepo = repo.findByStudentAndSemester(1L, 1)
                .orElseThrow();

        assertEquals(8.0, fromRepo.getGpa(), 0.0001);
        assertEquals(24, fromRepo.getTotalCredits());
    }

    @Test
    void findAll_returnsAllReports() {
        GradeReportRepository repo = new GradeReportRepository();

        repo.save(new GradeReport(1L, 1, 7.0, 20));
        repo.save(new GradeReport(1L, 2, 9.0, 22));

        List<GradeReport> all = repo.findAll();
        assertEquals(2, all.size());
    }
}
