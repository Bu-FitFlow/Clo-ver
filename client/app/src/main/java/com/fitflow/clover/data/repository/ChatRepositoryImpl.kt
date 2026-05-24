package com.fitflow.clover.data.repository

import com.fitflow.clover.data.remote.api.ChatApi
import com.fitflow.clover.data.remote.dto.ChatImageMessageRequest
import com.fitflow.clover.data.remote.dto.ChatTextMessageRequest
import com.fitflow.clover.data.remote.dto.toDomain
import com.fitflow.clover.domain.modal.ChatGalleryImageModel
import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatRoomModel
import com.fitflow.clover.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val chatApi: ChatApi
) : ChatRepository {

    override suspend fun getChatRooms(
        filter: String?
    ): Result<List<ChatRoomModel>> {
        return runCatching {
            chatApi.getChatRooms(filter).map { it.toDomain() }
        }
    }

    override suspend fun getChatMessages(
        chatRoomId: Long
    ): Result<List<ChatMessageModel>> {
        return runCatching {
            chatApi.getChatMessages(chatRoomId).map { it.toDomain() }
        }
    }

    override suspend fun sendTextMessage(
        chatRoomId: Long,
        content: String
    ): Result<ChatMessageModel> {
        return runCatching {
            chatApi.sendTextMessage(
                chatRoomId = chatRoomId,
                request = ChatTextMessageRequest(content = content)
            ).toDomain()
        }
    }

    override suspend fun sendImageMessage(
        chatRoomId: Long,
        imageUrl: String
    ): Result<ChatMessageModel> {
        return runCatching {
            chatApi.sendImageMessage(
                chatRoomId = chatRoomId,
                request = ChatImageMessageRequest(imageUrl = imageUrl)
            ).toDomain()
        }
    }

    override suspend fun getGalleryImages(): Result<List<ChatGalleryImageModel>> {
        return runCatching {
            chatApi.getGalleryImages().map { it.toDomain() }
        }
    }
}