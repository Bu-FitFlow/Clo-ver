package com.fitflow.clover.domain.community.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        Long communityId,
        String content,


        Long writerId,
        String writerNickname,
        String writerProfileImg,

        boolean isWriter,
        LocalDateTime createdAt
) {}