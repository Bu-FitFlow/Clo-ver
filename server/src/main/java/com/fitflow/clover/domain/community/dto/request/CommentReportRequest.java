package com.fitflow.clover.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentReportRequest(
        @NotNull(message = "신고할 댓글 ID는 필수입니다.")
        Long commentId,

        @NotBlank(message = "신고 사유는 필수입니다.")
        String reportReason,

        String detailContent
) {}
