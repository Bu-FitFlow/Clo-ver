package com.fitflow.clover.domain.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationCreateRequest {

    private Long receiverId;
    private Long senderId;
    private String notificationType;
    private String content;
    private Long relatedId;
}