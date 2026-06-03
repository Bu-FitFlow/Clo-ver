package com.fitflow.clover.domain.admin.dto.response;

import com.fitflow.clover.domain.community.entity.Community;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminCommunityResponse(
        Long communityId,
        String boardType,
        String title,
        Long writerId,
        int viewCount,
        int commentCount,
        String postStatus,
        LocalDateTime createdAt
) {
    public static AdminCommunityResponse from(Community community) {
        return AdminCommunityResponse.builder()
                .communityId(community.getCommunityId())
                .boardType(community.getBoardType().name())
                .title(community.getTitle())
                .writerId(community.getMemberId())
                .viewCount(community.getViewCount())
                .commentCount(community.getCommentCount())
                .postStatus(community.getPostStatus() != null ? community.getPostStatus().name() : "ACTIVE")
                .createdAt(community.getCreatedAt())
                .build();
    }
}