package com.fitflow.clover.domain.community.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record ReviewPostRequest(
        @NotBlank(message = "제목을 입력해주세요.")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        List<String> imageUrls,

        String productName,

        String recommendedType
) {}
