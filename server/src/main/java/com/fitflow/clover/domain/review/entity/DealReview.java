package com.fitflow.clover.domain.review.entity;

import com.fitflow.clover.domain.deal.entity.Deal;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealReview extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false, unique = true)
    private Deal deal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Member target;

    @Column(nullable = false)
    private Byte rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "original_rating")
    private Byte originalRating;

    @Column(name = "original_content", columnDefinition = "TEXT")
    private String originalContent;

    @Column(name = "is_edited", nullable = false)
    private Boolean isEdited = false;

    @Builder
    public DealReview(Deal deal, Member author, Member target, Byte rating, String content) {
        this.deal = deal;
        this.author = author;
        this.target = target;
        this.rating = rating;
        this.content = content;
    }

    public void updateReview(Byte newRating, String newContent) {
        this.originalRating = this.rating;
        this.originalContent = this.content;
        this.rating = newRating;
        this.content = newContent;
        this.isEdited = true;
    }
}
