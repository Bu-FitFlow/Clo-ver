package com.fitflow.clover.domain.notification.dto;

public record NotificationCreateRequest(

        Long receiverId,
        Long senderId,
        String notificationType,
        String content,
        Long relatedId
) {
}