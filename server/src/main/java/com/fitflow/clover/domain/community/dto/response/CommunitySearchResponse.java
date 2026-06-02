package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;

public record CommunitySearchResponse(
        Long communityId,
        String boardType,
        String title,
        String writerNickname,
        int viewCount,
        int commentCount,
        int wishlistCount,
        LocalDateTime createdAt
) {}
