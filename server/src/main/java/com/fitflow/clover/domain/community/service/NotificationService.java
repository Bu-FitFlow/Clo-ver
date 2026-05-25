package com.fitflow.clover.domain.community.service;

import com.fitflow.clover.domain.community.entity.Notification;
import com.fitflow.clover.domain.community.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void notifyComment(Long receiverId, Long senderId, Long communityId, String senderNickname) {
        if (receiverId.equals(senderId)) return;

        notificationRepository.save(Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .notificationType("COMMENT")
                .content(senderNickname + "님이 회원님의 게시글에 댓글을 달았습니다.")
                .relatedId(communityId)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void notifyReply(Long receiverId, Long senderId, Long communityId, String senderNickname) {
        if (receiverId.equals(senderId)) return;

        notificationRepository.save(Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .notificationType("COMMENT")
                .content(senderNickname + "님이 회원님의 댓글에 대댓글을 달았습니다.")
                .relatedId(communityId)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void notifyLike(Long receiverId, Long senderId, Long communityId, String senderNickname) {
        if (receiverId.equals(senderId)) return;

        notificationRepository.save(Notification.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .notificationType("LIKE")
                .content(senderNickname + "님이 회원님의 게시글을 좋아합니다.")
                .relatedId(communityId)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public List<Notification> getNotifications(Long memberId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(memberId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));
        notification.read();
    }

    public int getUnreadCount(Long memberId) {
        return notificationRepository.findByReceiverIdAndIsRead(memberId, 0).size();
    }
}
