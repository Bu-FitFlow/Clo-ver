package com.fitflow.clover.domain.notification.controller;

import com.fitflow.clover.domain.notification.dto.NotificationCreateRequest;
import com.fitflow.clover.domain.notification.dto.NotificationResponse;
import com.fitflow.clover.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림 관리", description = "알림 생성, 조회, 읽음 처리 관련 API")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 생성")
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestBody NotificationCreateRequest request
    ) {
        return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @Operation(summary = "회원별 알림 목록 조회")
    @GetMapping("/members/{memberId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByMember(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(notificationService.getNotificationsByReceiver(memberId));
    }

    @Operation(summary = "알림 읽음 처리")
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> readNotification(
            @PathVariable Long notificationId
    ) {
        return ResponseEntity.ok(notificationService.readNotification(notificationId));
    }
}