package com.fitflow.clover.domain.repository

import com.fitflow.clover.domain.modal.ChatGalleryImageModel
import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatRoomModel

interface ChatRepository {

    suspend fun getChatRooms(
        filter: String? = null
    ): Result<List<ChatRoomModel>>

    suspend fun getChatMessages(
        chatRoomId: Long
    ): Result<List<ChatMessageModel>>

    suspend fun sendTextMessage(
        chatRoomId: Long,
        content: String
    ): Result<ChatMessageModel>

    suspend fun sendImageMessage(
        chatRoomId: Long,
        imageUrl: String
    ): Result<ChatMessageModel>

    suspend fun getGalleryImages(): Result<List<ChatGalleryImageModel>>
}