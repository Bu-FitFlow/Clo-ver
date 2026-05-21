package com.fitflow.clover.domain.deal.dto.response;

import com.fitflow.clover.domain.deal.entity.Deal;
import com.fitflow.clover.domain.deal.entity.DealStatus;

import java.time.LocalDateTime;

public record DealResponse(
        Long dealId,
        Long buyerId,
        Long sellerId,
        Long productId,
        DealStatus dealStatus,
        LocalDateTime createdAt
) {
    public static DealResponse from(Deal deal) {
        return new DealResponse(
                deal.getId(),
                deal.getBuyer().getMemberId(),
                deal.getSeller().getMemberId(),
                deal.getProduct().getProductId(),
                deal.getDealStatus(),
                deal.getCreatedAt()
        );
    }
}
