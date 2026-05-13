package com.fitflow.clover.domain.deal.service;

import com.fitflow.clover.domain.deal.dto.response.DealResponse;
import com.fitflow.clover.domain.deal.entity.Deal;
import com.fitflow.clover.domain.deal.entity.DealRole;
import com.fitflow.clover.domain.deal.entity.DealStatus;
import com.fitflow.clover.domain.deal.repository.DealRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.domain.product.entity.ProductStatus;
import com.fitflow.clover.domain.product.repository.ProductRepository;
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
public class DealService {
    private final DealRepository dealRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long createDeal(Long buyerId, Long sellerId, Long productId) {
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        if (buyer.getMemberId().equals(seller.getMemberId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (product.getPostStatus() == ProductStatus.SOLD_OUT || product.getPostStatus() == ProductStatus.RESERVED) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }

        Deal deal = Deal.builder()
                .buyer(buyer)
                .seller(seller)
                .product(product)
                .dealStatus(DealStatus.IN_PROGRESS)
                .build();

        Deal savedDeal = dealRepository.save(deal);
        return savedDeal.getId();
    }

    @Transactional
    public void updateDealStatus(Long dealId, DealStatus newStatus) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new CustomException(ErrorCode.DEAL_NOT_FOUND));

        deal.updateStatus(newStatus);

        if (newStatus == DealStatus.COMPLETED) {
            deal.getProduct().changeStatus(ProductStatus.SOLD_OUT);
        }
    }

    public Slice<DealResponse> getMyDeals(Long memberId, DealRole role, Pageable pageable) {
        Slice<Deal> deals = dealRepository.findMyDeals(memberId, role, pageable);
        return deals.map(DealResponse::from);
    }

    @Transactional
    public void deleteDeal(Long memberId, Long dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new CustomException(ErrorCode.DEAL_NOT_FOUND));

        boolean isParticipant = deal.getBuyer().getMemberId().equals(memberId)
                || deal.getSeller().getMemberId().equals(memberId);
        if (!isParticipant) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DEAL_ACCESS);
        }

        if (deal.getDealStatus() == DealStatus.COMPLETED) {
            throw new CustomException(ErrorCode.COMPLETED_DEAL_CANNOT_DELETE);
        }

        dealRepository.delete(deal);
    }
}

