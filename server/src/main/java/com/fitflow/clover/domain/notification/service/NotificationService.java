package com.fitflow.clover.domain.notification.service;

import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.notification.dto.NotificationCreateRequest;
import com.fitflow.clover.domain.notification.dto.NotificationResponse;
import com.fitflow.clover.domain.notification.entity.Notification;
import com.fitflow.clover.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        Member receiver = memberRepository.findById(request.receiverId())
                .orElseThrow(() -> new IllegalArgumentException("알림 수신자를 찾을 수 없습니다."));

        Member sender = null;
        if (request.senderId() != null) {
            sender = memberRepository.findById(request.senderId())
                    .orElseThrow(() -> new IllegalArgumentException("알림 발신자를 찾을 수 없습니다."));
        }

        Notification notification = Notification.builder()
                .receiverId(receiver)
                .sender(sender)
                .notificationType(request.notificationType())
                .content(request.content())
                .relatedId(request.relatedId())
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return NotificationResponse.from(savedNotification);
    }

    public List<NotificationResponse> getNotificationsByReceiver(Long receiverId) {
        return notificationRepository.findByReceiverId_MemberIdOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public NotificationResponse readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));

        notification.read();
        return NotificationResponse.from(notification);
    }
}