package com.fitflow.clover.domain.review.service;

import com.fitflow.clover.domain.deal.entity.Deal;
import com.fitflow.clover.domain.deal.entity.DealStatus;
import com.fitflow.clover.domain.deal.repository.DealRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.review.dto.request.DealReviewCreateRequest;
import com.fitflow.clover.domain.review.dto.request.DealReviewUpdateRequest;
import com.fitflow.clover.domain.review.dto.response.DealReviewResponse;
import com.fitflow.clover.domain.review.entity.DealReview;
import com.fitflow.clover.domain.review.repository.DealReviewRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealReviewService {
    private final DealReviewRepository dealReviewRepository;
    private final DealRepository dealRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createReview(Long authorId, DealReviewCreateRequest request) {
        Deal deal = dealRepository.findById(request.dealId())
                .orElseThrow(() -> new CustomException(ErrorCode.DEAL_NOT_FOUND));
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (deal.getDealStatus() != DealStatus.COMPLETED) {
            throw new CustomException(ErrorCode.DEAL_NOT_COMPLETED);
        }

        if (dealReviewRepository.existsByDeal(deal)) {
            throw new CustomException(ErrorCode.ALREADY_REVIEWED);
        }

        Member target;
        if (deal.getBuyer().getMemberId().equals(authorId)) {
            target = deal.getSeller();
        } else if (deal.getSeller().getMemberId().equals(authorId)) {
            target = deal.getBuyer();
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        DealReview review = DealReview.builder()
                .deal(deal)
                .author(author)
                .target(target)
                .rating(request.rating())
                .content(request.content())
                .build();

        return dealReviewRepository.save(review).getId();
    }

    public Slice<DealReviewResponse> getReceivedReviews(Long memberId, Pageable pageable) {
        return dealReviewRepository.findReceivedReviews(memberId, pageable)
                .map(DealReviewResponse::from);
    }

    public Slice<DealReviewResponse> getWrittenReviews(Long memberId, Pageable pageable) {
        return dealReviewRepository.findWrittenReviews(memberId, pageable)
                .map(DealReviewResponse::from);
    }

    @Transactional
    public void updateReview(Long authorId, Long reviewId, DealReviewUpdateRequest request) {
        DealReview review = dealReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getAuthor().getMemberId().equals(authorId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (review.getIsEdited()) {
            throw new CustomException(ErrorCode.ALREADY_EDITED_REVIEW);
        }

        review.updateReview(request.rating(), request.content());
    }
}