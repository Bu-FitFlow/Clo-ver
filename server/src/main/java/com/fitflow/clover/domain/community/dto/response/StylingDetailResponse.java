package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record StylingDetailResponse(
        Long communityId,
        String title,
        String content,
        String ootdInfo,
        List<String> imageUrls,
        String writerNickname,
        String writerProfileImg,
        int viewCount,
        int commentCount,
        LocalDateTime createdAt
) {}
