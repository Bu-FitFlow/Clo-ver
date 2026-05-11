package com.fitflow.clover.domain.report.entity;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "report")
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false)
    private Member reported;

    @Column(name = "community_id")
    private Long communityId;

    @Column(name = "report_type", nullable = false, length = 50)
    private String reportType;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDING";

    @Column(name = "admin_memo", columnDefinition = "TEXT")
    private String adminMemo;

    public void resolve(String adminMemo) {
        this.status = "RESOLVED";
        this.adminMemo = adminMemo;
    }

    public void reject(String adminMemo) {
        this.status = "REJECTED";
        this.adminMemo = adminMemo;
    }
}