package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;

public record CommentReportResponse(
        Long reportId,         // 생성된 신고 내역 고유 ID
        Long commentId,        // 신고된 댓글 ID
        String status,         // 현재 처리 상태 (예: "RECEIVED" - 접수됨 / "REVIEWING" - 검토중)
        LocalDateTime reportedAt
) {}