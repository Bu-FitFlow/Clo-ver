package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;

public record CommunityReportResponse(
        Long communityId,
        String reportReason,
        String status,
        LocalDateTime reportedAt
) {}