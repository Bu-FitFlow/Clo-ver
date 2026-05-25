package com.fitflow.clover.domain.modal

data class ChatRoomModel(
    val chatRoomId: Long,
    val productId: Long,
    val buyerId: Long,
    val sellerId: Long,
    val createdAt: String,
    val updatedAt: String
)

data class ChatMessageModel(
    val messageId: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val messageType: ChatMessageType,
    val createdAt: String
)

data class ChatImageModel(
    val imageId: Long,
    val imageUrl: String,
    val referenceType: String,
    val referenceId: Long,
    val sortOrder: Int,
    val createdAt: String
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