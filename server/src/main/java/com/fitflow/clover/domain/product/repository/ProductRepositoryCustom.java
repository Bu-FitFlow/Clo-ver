package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.product.dto.request.ProductSearchCondition;
import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.domain.product.entity.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<Product> searchProducts(ProductSearchCondition condition, Pageable pageable);

    Slice<Product> findRecommendedProducts(String recommendedType, String personalColor, ProductStatus status, Pageable pageable);
}
