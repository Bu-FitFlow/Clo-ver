package com.fitflow.clover.domain.chat.controller;

import com.fitflow.clover.domain.chat.dto.request.ChatMessageRequest;
import com.fitflow.clover.domain.chat.dto.response.ChatMessageResponse;
import com.fitflow.clover.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @Operation(summary = "실시간 메시지 전송 및 브로드캐스팅", description = "클라이언트로부터 받은 메시지를 DB에 저장하고, 해당 채팅방을 구독 중인 모든 사용자에게 실시간으로 전달합니다.")
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequest request, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());
        ChatMessageResponse response = chatService.saveMessage(
                request.roomId(),
                senderId,
                request.content(),
                request.messageType()
        );

        messagingTemplate.convertAndSend("/sub/chat/room/" + request.roomId(), response);
    }
}