package com.fitflow.clover.domain.chat.repository;

import com.fitflow.clover.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
    List<ChatMessage> findByChatRoom_RoomIdOrderByCreatedAtAsc(Long roomId);
}
