package com.fitflow.clover.domain.modal

data class ChatRoomModel(
    val chatRoomId: Long,
    val productId: Long,
    val productName: String,
    val productImageUrl: String?,
    val sellerId: Long,
    val sellerNickname: String,
    val sellerProfileImageUrl: String?,
    val buyerId: Long,
    val buyerNickname: String,
    val buyerProfileImageUrl: String?,
    val lastMessage: String?,
    val lastMessageType: ChatMessageType,
    val lastMessageCreatedAt: String?,
    val unreadCount: Int,
    val createdAt: String?,
    val updatedAt: String?
)

data class ChatMessageModel(
    val messageId: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val senderNickname: String,
    val senderProfileImageUrl: String?,
    val content: String,
    val imageUrl: String?,
    val messageType: ChatMessageType,
    val createdAt: String
)

data class ChatGalleryImageModel(
    val imageId: Long,
    val imageUrl: String,
    val sortOrder: Int
)

enum class ChatMessageType {
    TEXT,
    IMAGE,
    FILE;

    companion object {
        fun from(value: String?): ChatMessageType {
            return when (value?.uppercase()) {
                "IMAGE" -> IMAGE
                "FILE" -> FILE
                else -> TEXT
            }
        }
    }
}