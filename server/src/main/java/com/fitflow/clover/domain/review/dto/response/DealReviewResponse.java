package com.fitflow.clover.domain.review.dto.response;

import com.fitflow.clover.domain.review.entity.DealReview;

import java.time.LocalDateTime;

public record DealReviewResponse(
        Long reviewId,
        Long dealId,
        String productName,
        String authorNickname,
        Byte rating,
        String content,
        Boolean isEdited,
        Byte originalRating,
        String originalContent,
        LocalDateTime createdAt
) {
    public static DealReviewResponse from(DealReview review) {
        return new DealReviewResponse(
                review.getId(),
                review.getDeal().getId(),
                review.getDeal().getProduct().getName(),
                review.getAuthor().getNickname(),
                review.getRating(),
                review.getContent(),
                review.getIsEdited(),
                review.getOriginalRating(),
                review.getOriginalContent(),
                review.getCreatedAt()
        );
    }
}