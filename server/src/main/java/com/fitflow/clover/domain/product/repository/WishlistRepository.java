package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.product.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    void deleteAllByProduct_ProductId(Long productId);
}
