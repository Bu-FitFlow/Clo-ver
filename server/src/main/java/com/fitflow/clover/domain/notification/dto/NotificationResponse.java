package com.fitflow.clover.domain.notification.dto;

import com.fitflow.clover.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

    private Long notificationId;
    private Long receiverId;
    private Long senderId;
    private String notificationType;
    private String content;
    private Long relatedId;
    private boolean isRead;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .receiverId(notification.getReceiver().getMemberId())
                .senderId(notification.getSender() != null ? notification.getSender().getMemberId() : null)
                .notificationType(notification.getNotificationType())
                .content(notification.getContent())
                .relatedId(notification.getRelatedId())
                .isRead(notification.isRead())
                .build();
    }
}