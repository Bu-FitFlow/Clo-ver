package com.fitflow.clover.domain.chat.dto.response;

import com.fitflow.clover.domain.chat.entity.ChatMessage;
import com.fitflow.clover.domain.chat.entity.MessageType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long messageId,
        Long roomId,
        Long senderId,
        String content,
        MessageType messageType,
        Boolean isRead,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getMessageId(),
                message.getChatRoom().getRoomId(),
                message.getSender().getMemberId(),
                message.getContent(),
                message.getMessageType(),
                message.getIsRead(),
                message.getCreatedAt()
        );
    }
}