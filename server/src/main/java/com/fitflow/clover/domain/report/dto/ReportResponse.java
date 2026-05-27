package com.fitflow.clover.domain.report.dto;

import com.fitflow.clover.domain.report.entity.Report;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponse {

    private Long reportId;
    private Long reporterId;
    private Long reportedId;
    private Long communityId;
    private String reportType;
    private String content;
    private String status;
    private String adminMemo;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .reportId(report.getReportId())
                .reporterId(report.getReporter().getMemberId())
                .reportedId(report.getReported().getMemberId())
                .communityId(report.getCommunityId())
                .reportType(report.getReportType())
                .content(report.getContent())
                .status(report.getStatus())
                .adminMemo(report.getAdminMemo())
                .build();
    }
}