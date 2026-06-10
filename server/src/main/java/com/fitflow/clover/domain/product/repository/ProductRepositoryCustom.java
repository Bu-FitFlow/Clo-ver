package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.diagnosis.entity.BodyType;
import com.fitflow.clover.domain.diagnosis.entity.PersonalColor;
import com.fitflow.clover.domain.product.dto.request.ProductSearchCondition;
import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.domain.product.entity.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<Product> searchProducts(ProductSearchCondition condition, Pageable pageable);

    Slice<Product> findRecommendedProducts(BodyType recommendedType, PersonalColor personalColor, ProductStatus status, Pageable pageable);
}
