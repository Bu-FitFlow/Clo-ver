package com.fitflow.clover.domain.chat.controller;

import com.fitflow.clover.domain.chat.dto.response.ChatMessageResponse;
import com.fitflow.clover.domain.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "채팅방", description = "채팅방 생성 및 관리 API")
@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", allowedHeaders = "*") // 임시 보안 해제 코드
public class ChatRoomController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 생성 및 조회", description = "상품 페이지에서 채팅하기를 눌렀을 때 호출합니다. 기존 방이 있으면 해당 방 번호를, 없으면 새로 생성하여 반환합니다.")
    @PostMapping
    public ResponseEntity<Long> getOrCreateRoom(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long productId) {

        Long buyerId = Long.parseLong(userDetails.getUsername());
        Long roomId = chatService.getOrCreateRoom(buyerId, productId);

        return ResponseEntity.ok(roomId);
    }

    @Operation(summary = "채팅방 이전 대화 내역 조회", description = "특정 채팅방의 모든 이전 대화 내역을 시간순으로 조회합니다.")
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessageHistory(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessageHistory(roomId));
    }
}