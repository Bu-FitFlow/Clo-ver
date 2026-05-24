// CommentReportResponse
package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;

public record CommentReportResponse(
        Long commentId,
        String reportReason,
        String status,
        LocalDateTime reportedAt
) {}