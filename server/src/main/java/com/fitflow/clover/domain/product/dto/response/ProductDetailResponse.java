package com.fitflow.clover.domain.product.dto.response;

import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.domain.product.entity.ProductStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ProductDetailResponse(
        Long productId,
        Long sellerId,
        String sellerNickname,
        String categoryName,
        String name,
        Integer price,
        String content,
        String size,
        String grade,
        String tradingArea,
        String recommendedType,
        String personalColor,
        ProductStatus postStatus,
        Integer viewCount,
        Integer wishlistCount,
        List<String> imageUrls,
        List<String> hashtags,
        LocalDateTime createdAt
) {
    public static ProductDetailResponse from(Product product, List<String> imageUrls, List<String> hashtags) {
        return new ProductDetailResponse(
                product.getProductId(),
                product.getSeller().getMemberId(),
                product.getSeller().getNickname(),
                product.getCategory().getName(),
                product.getName(),
                product.getPrice(),
                product.getContent(),
                product.getSize(),
                product.getGrade(),
                product.getTradingArea(),
                product.getRecommendedType(),
                product.getPersonalColor(),
                product.getPostStatus(),
                product.getViewCount(),
                product.getWishlistCount(),
                imageUrls,
                hashtags,
                product.getCreatedAt()

        );
    }
}
