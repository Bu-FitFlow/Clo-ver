package com.fitflow.clover.domain.review.repository;

import com.fitflow.clover.domain.deal.entity.Deal;
import com.fitflow.clover.domain.review.entity.DealReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealReviewRepository extends JpaRepository<DealReview, Long>, DealReviewRepositoryCustom {
    boolean existsByDeal(Deal deal);
}