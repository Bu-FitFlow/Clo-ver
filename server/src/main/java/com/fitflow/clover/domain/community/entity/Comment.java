package com.fitflow.clover.domain.community.entity;

import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")  // ✅ MariaDB 예약어 충돌 방지
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 대댓글 기능 구현용 (nullable = true)
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT")
    private int isDeleted = 0;
}
