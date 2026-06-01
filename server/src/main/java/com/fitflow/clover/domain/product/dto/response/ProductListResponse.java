package com.fitflow.clover.domain.product.dto.response;

import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.domain.product.entity.ProductStatus;

import java.time.LocalDateTime;

public record ProductListResponse(
        Long productId,
        String name,
        Integer price,
        String tradingArea,
        ProductStatus postStatus,
        String thumbnailImageUrl,
        Integer wishlistCount,
        LocalDateTime createdAt
) {
    public static ProductListResponse from(Product product, String thumbnailImageUrl) {
        return new ProductListResponse(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getTradingArea(),
                product.getPostStatus(),
                thumbnailImageUrl,
                product.getWishlistCount(),
                product.getCreatedAt()
        );
    }
}
