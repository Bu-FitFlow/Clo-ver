package com.fitflow.clover.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DealReviewCreateRequest(
        @NotNull Long dealId,
        @Min(1) @Max(5) Byte rating, // 1~5점 사이
        String content
) {
}