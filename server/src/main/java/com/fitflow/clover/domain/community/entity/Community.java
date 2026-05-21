package com.fitflow.clover.domain.community.entity;

import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Community extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long communityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", length = 20, nullable = false)
    private BoardType boardType;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @Column(name = "comment_count", nullable = false)
    @Builder.Default
    private int commentCount = 0;

    // ================= DB에 컬럼 없음 → @Transient로 변경 ================= //
    @Transient
    private int wishlistCount = 0;

    @Transient
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", length = 20)
    private PostStatus postStatus;

    // ================= 마켓 관련 가상 필드 ================= //
    @Transient
    private String grade;

    @Transient
    private String recommendedType;

    @Transient
    private String faceShape;

    @Transient
    private Long categoryId;

    @Transient
    private Long colorId;


    // ================= 비즈니스 메서드 ================= //

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void assignMarketOptions(Integer price, String grade, String recommendedType, String faceShape, Long categoryId, Long colorId) {
        this.price = price;
        this.grade = grade;
        this.recommendedType = recommendedType;
        this.faceShape = faceShape;
        this.categoryId = categoryId;
        this.colorId = colorId;
    }

    public boolean toggleWishlistSingleTable(Long memberId) {
        this.wishlistCount++;
        return true;
    }

    public void updateFreePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void increaseViewCount() {
        this.viewCount++;
    }
}
