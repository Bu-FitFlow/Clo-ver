package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<Product> searchProducts(Long cursorId, Pageable pageable);
}
