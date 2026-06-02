package com.fitflow.clover.domain.deal.dto.request;

import com.fitflow.clover.domain.deal.entity.DealStatus;

public record DealStatusUpdateRequest(
        DealStatus dealStatus
) {
}
