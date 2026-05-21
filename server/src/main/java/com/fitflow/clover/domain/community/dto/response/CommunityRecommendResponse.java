package com.fitflow.clover.domain.community.dto.response;


public record CommunityRecommendResponse(
            Long communityId,
            String boardType,          // FREE, REVIEW, STYLING (어느 게시판 인기글인지 구분)
            String title,
            String representativeImg,  // 썸네일 이미지 (없으면 null)
            String writerNickname,
            int viewCount,
            int commentCount
    ) {}

