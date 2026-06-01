package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;

public record CommunityListResponse(
        Long communityId,
        String title,
        Long writerId,
        int viewCount,
        int commentCount,
        LocalDateTime createdAt
) {}