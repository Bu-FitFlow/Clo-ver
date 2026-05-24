package com.fitflow.clover.data.remote.dto

import com.fitflow.clover.domain.modal.ChatGalleryImageModel
import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatMessageType
import com.fitflow.clover.domain.modal.ChatRoomModel

data class ChatRoomResponse(
    val chatRoomId: Long,
    val productId: Long,
    val productName: String,
    val productImageUrl: String? = null,

    val sellerId: Long,
    val sellerNickname: String,
    val sellerProfileImageUrl: String? = null,

    val buyerId: Long,
    val buyerNickname: String,
    val buyerProfileImageUrl: String? = null,

    val lastMessage: String? = null,
    val lastMessageType: String? = "TEXT",
    val lastMessageCreatedAt: String? = null,

    val unreadCount: Int? = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class ChatMessageResponse(
    val messageId: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val senderNickname: String,
    val senderProfileImageUrl: String? = null,
    val content: String? = "",
    val imageUrl: String? = null,
    val messageType: String? = "TEXT",
    val createdAt: String
)

data class ChatGalleryImageResponse(
    val imageId: Long,
    val imageUrl: String,
    val sortOrder: Int = 0
)

data class ChatTextMessageRequest(
    val content: String,
    val messageType: String = "TEXT"
)

data class ChatImageMessageRequest(
    val imageUrl: String,
    val messageType: String = "IMAGE"
)

fun ChatRoomResponse.toDomain(): ChatRoomModel {
    return ChatRoomModel(
        chatRoomId = chatRoomId,
        productId = productId,
        productName = productName,
        productImageUrl = productImageUrl,
        sellerId = sellerId,
        sellerNickname = sellerNickname,
        sellerProfileImageUrl = sellerProfileImageUrl,
        buyerId = buyerId,
        buyerNickname = buyerNickname,
        buyerProfileImageUrl = buyerProfileImageUrl,
        lastMessage = lastMessage,
        lastMessageType = ChatMessageType.from(lastMessageType),
        lastMessageCreatedAt = lastMessageCreatedAt,
        unreadCount = unreadCount ?: 0,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ChatMessageResponse.toDomain(): ChatMessageModel {
    return ChatMessageModel(
        messageId = messageId,
        chatRoomId = chatRoomId,
        senderId = senderId,
        senderNickname = senderNickname,
        senderProfileImageUrl = senderProfileImageUrl,
        content = content.orEmpty(),
        imageUrl = imageUrl,
        messageType = ChatMessageType.from(messageType),
        createdAt = createdAt
    )
}

fun ChatGalleryImageResponse.toDomain(): ChatGalleryImageModel {
    return ChatGalleryImageModel(
        imageId = imageId,
        imageUrl = imageUrl,
        sortOrder = sortOrder
    )
}