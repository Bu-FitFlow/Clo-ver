package com.fitflow.clover.domain.chat.dto.request;

import com.fitflow.clover.domain.chat.entity.MessageType;

public record ChatMessageRequest(
        Long roomId,
        String content,
        MessageType messageType
) {
}