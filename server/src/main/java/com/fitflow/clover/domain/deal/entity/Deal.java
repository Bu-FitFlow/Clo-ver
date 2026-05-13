package com.fitflow.clover.domain.deal.entity;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_status", nullable = false, length = 20)
    private DealStatus dealStatus;

    @Builder
    public Deal(Member buyer, Member seller, Product product, DealStatus dealStatus) {
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;
        this.dealStatus = dealStatus != null ? dealStatus : DealStatus.IN_PROGRESS;
    }

    public void updateStatus(DealStatus newStatus) {
        this.dealStatus = newStatus;
    }
}
