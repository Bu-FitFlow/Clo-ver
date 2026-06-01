package com.fitflow.clover.domain.product.dto.request;

import com.fitflow.clover.domain.product.entity.ProductStatus;

public record ProductSearchCondition(
        Long cursorId,
        Long categoryId,
        String tradingArea,
        String keyword,
        ProductStatus status
) {
}
