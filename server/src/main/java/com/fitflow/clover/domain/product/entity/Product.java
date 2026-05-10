package com.fitflow.clover.domain.product.entity;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.product.dto.request.ProductUpdateRequest;
import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLRestriction("post_status != 'DELETED'")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 50)
    private String size;

    @Column(nullable = false, length = 20)
    private String grade;

    @Column(name = "trading_area", nullable = false, length = 100)
    private String tradingArea;

    @Column(name = "recommended_type", length = 50)
    private String recommendedType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false, length = 20)
    private ProductStatus postStatus = ProductStatus.ACTIVE;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Builder.Default
    @Column(name = "wishlist_count", nullable = false)
    private Integer wishlistCount = 0;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductHashtag> productHashtags = new ArrayList<>();

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void changeStatus(ProductStatus newStatus) {
        this.postStatus = newStatus;
    }

    public void addProductHashtag(ProductHashtag productHashtag) {
        this.productHashtags.add(productHashtag);
    }

    public void increaseWishlistCount() {
        this.wishlistCount++;
    }

    public void decreaseWishlistCount() {
        if (this.wishlistCount > 0) {
            this.wishlistCount--;
        }
    }

    public void update(Category category, ProductUpdateRequest request) {
        this.category = category;
        this.name = request.name();
        this.price = request.price();
        this.content = request.content();
        this.size = request.size();
        this.grade = request.grade();
        this.tradingArea = request.tradingArea();
        this.recommendedType = request.recommendedType();
    }
}
