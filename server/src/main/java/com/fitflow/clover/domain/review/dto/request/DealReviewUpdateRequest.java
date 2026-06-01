package com.fitflow.clover.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record DealReviewUpdateRequest(
        @Min(1) @Max(5) Byte rating,
        String content
) {
}