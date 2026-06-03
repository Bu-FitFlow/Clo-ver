package com.fitflow.clover.domain.notification.dto;

import com.fitflow.clover.domain.notification.entity.Notification;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        Long notificationId,
        Long receiverId,
        Long senderId,
        String notificationType,
        String content,
        Long relatedId,
        boolean isRead,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .receiverId(notification.getReceiver().getMemberId())
                .senderId(notification.getSender() != null ? notification.getSender().getMemberId() : null)
                .notificationType(notification.getNotificationType())
                .content(notification.getContent())
                .relatedId(notification.getRelatedId())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}