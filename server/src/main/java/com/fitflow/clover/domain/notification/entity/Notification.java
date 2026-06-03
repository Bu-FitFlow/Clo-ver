package com.fitflow.clover.domain.notification.entity;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.global.common.BaseCreatedTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(name = "is_read", nullable = false, columnDefinition = "TINYINT(1)")
    @Builder.Default
    private boolean isRead = false;

    public void read() {
        this.isRead = true;
    }
}