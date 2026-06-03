package com.fitflow.clover.domain.report.dto;

public record ReportCreateRequest(
        Long reporterId,
        Long reportedId,
        String targetType,
        Long targetId,
        String reportType,
        String content
) {
}