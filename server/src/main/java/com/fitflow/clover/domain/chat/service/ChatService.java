package com.fitflow.clover.domain.chat.service;

import com.fitflow.clover.domain.chat.dto.response.ChatMessageResponse;
import com.fitflow.clover.domain.chat.entity.ChatMessage;
import com.fitflow.clover.domain.chat.entity.ChatRoom;
import com.fitflow.clover.domain.chat.entity.MessageType;
import com.fitflow.clover.domain.chat.repository.ChatMessageRepository;
import com.fitflow.clover.domain.chat.repository.ChatRoomRepository;
import com.fitflow.clover.domain.member.entity.Member;
import com.fitflow.clover.domain.member.repository.MemberRepository;
import com.fitflow.clover.domain.product.entity.Product;
import com.fitflow.clover.domain.product.repository.ProductRepository;
import com.fitflow.clover.global.error.CustomException;
import com.fitflow.clover.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long getOrCreateRoom(Long buyerId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
        Member buyer = memberRepository.findById(buyerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (product.getSeller().getMemberId().equals(buyerId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        return chatRoomRepository.findByProductAndBuyer(product, buyer)
                .map(ChatRoom::getRoomId)
                .orElseGet(() -> {
                    ChatRoom newRoom = ChatRoom.builder()
                            .product(product)
                            .buyer(buyer)
                            .seller(product.getSeller())
                            .build();
                    return chatRoomRepository.save(newRoom).getRoomId();
                });
    }

    @Transactional
    public ChatMessageResponse saveMessage(Long roomId, Long senderId, String content, MessageType messageType) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .sender(sender)
                .content(content)
                .messageType(messageType)
                .build();

        return ChatMessageResponse.from(chatMessageRepository.save(message));
    }

    public List<ChatMessageResponse> getMessageHistory(Long roomId) {
        return chatMessageRepository.findByChatRoom_RoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }
}