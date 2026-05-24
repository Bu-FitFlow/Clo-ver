package com.fitflow.clover.domain.community.dto.response;

public record CommunityRecommendResponse(
        Long communityId,
        String boardType,
        String title,
        String representativeImg,
        String writerNickname,
        int viewCount,
        int commentCount,
        int wishlistCount
) {}

