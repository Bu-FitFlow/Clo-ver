package com.fitflow.clover.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentPostRequest(
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        String content
) {}