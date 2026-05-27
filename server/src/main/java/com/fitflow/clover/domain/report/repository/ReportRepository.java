package com.fitflow.clover.domain.report.repository;

import com.fitflow.clover.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}