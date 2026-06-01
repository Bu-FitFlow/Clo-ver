package com.fitflow.clover.domain.review.repository;

import com.fitflow.clover.domain.review.entity.DealReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DealReviewRepositoryCustom {
    Slice<DealReview> findReceivedReviews(Long targetId, Pageable pageable);

    Slice<DealReview> findWrittenReviews(Long authorId, Pageable pageable);
}
