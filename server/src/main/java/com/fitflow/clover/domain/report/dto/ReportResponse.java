package com.fitflow.clover.domain.report.dto;

import com.fitflow.clover.domain.report.entity.Report;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportResponse(
        Long reportId,
        Long reporterId,
        Long reportedId,
        String targetType,
        Long targetId,
        String reportType,
        String content,
        String status,
        String adminMemo,
        LocalDateTime createdAt
) {
    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .reporterId(report.getReporter().getMemberId())
                .reportedId(report.getReported().getMemberId())
                .targetType(report.getTargetType())
                .targetId(report.getTargetId())
                .reportType(report.getReportType())
                .content(report.getContent())
                .status(report.getStatus())
                .adminMemo(report.getAdminMemo())
                .createdAt(report.getCreatedAt())
                .build();
    }
}