package com.fitflow.clover.domain.repository

import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatRoomModel

interface ChatRepository {

    suspend fun getChatRooms(
        filter: String? = null
    ): List<ChatRoomModel>

    suspend fun getChatMessages(
        chatRoomId: Long
    ): List<ChatMessageModel>

    suspend fun createOrGetProductChatRoom(
        productId: Long,
        sellerId: Long
    ): ChatRoomModel

    suspend fun sendTextMessage(
        chatRoomId: Long,
        content: String
    ): ChatMessageModel

    suspend fun sendImageMessage(
        chatRoomId: Long,
        imageUrl: String
    ): ChatMessageModel
}