package com.fitflow.clover.domain.deal.dto.request;

public record DealCreateRequest(
        Long sellerId,
        Long productId
) {
}
