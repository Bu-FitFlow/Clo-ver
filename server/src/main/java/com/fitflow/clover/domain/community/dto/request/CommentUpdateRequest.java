package com.fitflow.clover.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "수정할 내용을 입력해주세요.")
        String content
) {}
