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

import java.util.Optional;

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
        // 상품 행을 잠가 상태 확인과 예약 처리 사이에 다른 거래 생성 요청이 끼어들지 못하게 한다.
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
        Member seller = product.getSeller();

        if (!seller.getMemberId().equals(sellerId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (buyer.getMemberId().equals(seller.getMemberId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (product.getPostStatus() != ProductStatus.ACTIVE) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }

        product.changeStatus(ProductStatus.RESERVED);

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
        Deal deal = dealRepository.findByIdForUpdate(dealId)
                .orElseThrow(() -> new CustomException(ErrorCode.DEAL_NOT_FOUND));

        if (deal.getDealStatus() != DealStatus.IN_PROGRESS) {
            throw new CustomException(ErrorCode.DEAL_ALREADY_CLOSED);
        }

        deal.updateStatus(newStatus);

        if (newStatus == DealStatus.COMPLETED) {
            lockProduct(deal).ifPresent(product -> product.changeStatus(ProductStatus.SOLD_OUT));
        } else if (newStatus == DealStatus.CANCELED || newStatus == DealStatus.ABORTED) {
            releaseReservation(deal);
        }
    }

    public Slice<DealResponse> getMyDeals(Long memberId, DealRole role, Pageable pageable) {
        Slice<Deal> deals = dealRepository.findMyDeals(memberId, role, pageable);
        return deals.map(DealResponse::from);
    }

    @Transactional
    public void deleteDeal(Long memberId, Long dealId) {
        Deal deal = dealRepository.findByIdForUpdate(dealId)
                .orElseThrow(() -> new CustomException(ErrorCode.DEAL_NOT_FOUND));

        boolean isParticipant = deal.getBuyer().getMemberId().equals(memberId)
                || deal.getSeller().getMemberId().equals(memberId);
        if (!isParticipant) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DEAL_ACCESS);
        }

        if (deal.getDealStatus() == DealStatus.COMPLETED) {
            throw new CustomException(ErrorCode.COMPLETED_DEAL_CANNOT_DELETE);
        }

        if (deal.getDealStatus() == DealStatus.IN_PROGRESS) {
            releaseReservation(deal);
        }

        dealRepository.delete(deal);
    }

    private Optional<Product> lockProduct(Deal deal) {
        // 삭제(DELETED)된 상품은 @SQLRestriction 때문에 조회되지 않으므로 상태를 건드리지 않는다.
        return productRepository.findByIdForUpdate(deal.getProduct().getProductId());
    }

    private void releaseReservation(Deal deal) {
        lockProduct(deal)
                .filter(product -> product.getPostStatus() == ProductStatus.RESERVED)
                .ifPresent(product -> product.changeStatus(ProductStatus.ACTIVE));
    }
}

