package com.fitflow.clover.domain.usecase

import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatRoomModel
import com.fitflow.clover.domain.repository.ChatRepository

class ChatUseCase(
    private val chatRepository: ChatRepository
) {

    suspend fun getChatRooms(
        filter: String? = null
    ): List<ChatRoomModel> {
        return chatRepository.getChatRooms(
            filter = filter
        )
    }

    suspend fun getChatMessages(
        chatRoomId: Long
    ): List<ChatMessageModel> {
        return chatRepository.getChatMessages(
            chatRoomId = chatRoomId
        )
    }

    suspend fun createOrGetProductChatRoom(
        productId: Long,
        sellerId: Long
    ): ChatRoomModel {
        return chatRepository.createOrGetProductChatRoom(
            productId = productId,
            sellerId = sellerId
        )
    }

    suspend fun sendTextMessage(
        chatRoomId: Long,
        content: String
    ): ChatMessageModel {
        return chatRepository.sendTextMessage(
            chatRoomId = chatRoomId,
            content = content
        )
    }

    suspend fun sendImageMessage(
        chatRoomId: Long,
        imageUrl: String
    ): ChatMessageModel {
        return chatRepository.sendImageMessage(
            chatRoomId = chatRoomId,
            imageUrl = imageUrl
        )
    }
}