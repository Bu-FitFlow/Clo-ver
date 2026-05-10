package com.fitflow.clover.domain.product.repository;

import com.fitflow.clover.domain.product.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByMember_MemberIdAndProduct_ProductId(Long memberId, Long productId);
}
