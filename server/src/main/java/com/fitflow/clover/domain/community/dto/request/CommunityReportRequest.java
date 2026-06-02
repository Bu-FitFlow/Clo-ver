package com.fitflow.clover.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommunityReportRequest(
        @NotNull(message = "신고할 커뮤니티 게시글 ID는 필수입니다.")
        Long communityId,

        @NotBlank(message = "신고 사유는 필수입니다.")
        String reportReason,

        String detailContent
) {}
