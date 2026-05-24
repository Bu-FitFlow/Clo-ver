package com.fitflow.clover.data.remote.api

import com.fitflow.clover.data.remote.dto.ChatGalleryImageResponse
import com.fitflow.clover.data.remote.dto.ChatImageMessageRequest
import com.fitflow.clover.data.remote.dto.ChatMessageResponse
import com.fitflow.clover.data.remote.dto.ChatRoomResponse
import com.fitflow.clover.data.remote.dto.ChatTextMessageRequest

interface ChatApi {

    suspend fun getChatRooms(
        filter: String? = null
    ): List<ChatRoomResponse>

    suspend fun getChatMessages(
        chatRoomId: Long
    ): List<ChatMessageResponse>

    suspend fun sendTextMessage(
        chatRoomId: Long,
        request: ChatTextMessageRequest
    ): ChatMessageResponse

    suspend fun sendImageMessage(
        chatRoomId: Long,
        request: ChatImageMessageRequest
    ): ChatMessageResponse

    suspend fun getGalleryImages(): List<ChatGalleryImageResponse>
}