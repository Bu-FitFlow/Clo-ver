package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FreeDetailResponse(
        Long communityId,
        String title,
        String content,
        List<String> imageUrl,
        String writerNickname,
        String writerProfileImg,
        int viewCount,
        int commentCount,
        int wishlistCount,
        LocalDateTime createdAt
) {}