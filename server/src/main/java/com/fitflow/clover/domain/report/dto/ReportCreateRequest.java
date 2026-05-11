package com.fitflow.clover.domain.report.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportCreateRequest {

    private Long reporterId;
    private Long reportedId;
    private Long communityId;
    private String reportType;
    private String content;
}