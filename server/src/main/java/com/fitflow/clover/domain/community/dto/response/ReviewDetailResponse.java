package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetailResponse(
        Long communityId,
        String title,
        String content,
        List<String> imageUrls,
        String writerNickname,
        String writerProfileImg,
        int viewCount,
        int commentCount,
        int wishlistCount,
        LocalDateTime createdAt
) {}