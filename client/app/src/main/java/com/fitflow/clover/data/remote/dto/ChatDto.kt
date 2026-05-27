package com.fitflow.clover.data.remote.dto

import com.fitflow.clover.domain.modal.ChatImageModel
import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatMessageType
import com.fitflow.clover.domain.modal.ChatRoomModel
import com.google.gson.annotations.SerializedName

data class ChatRoomResponse(
    @SerializedName("chat_room_id")
    val chatRoomId: Long? = null,

    @SerializedName("product_id")
    val productId: Long? = null,

    @SerializedName("buyer_id")
    val buyerId: Long? = null,

    @SerializedName("seller_id")
    val sellerId: Long? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)

data class ChatMessageResponse(
    @SerializedName("message_id")
    val messageId: Long? = null,

    @SerializedName("chat_room_id")
    val chatRoomId: Long? = null,

    @SerializedName("sender_id")
    val senderId: Long? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("message_type")
    val messageType: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)

data class ChatImageResponse(
    @SerializedName("image_id")
    val imageId: Long? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("reference_type")
    val referenceType: String? = null,

    @SerializedName("reference_id")
    val referenceId: Long? = null,

    @SerializedName("sort_order")
    val sortOrder: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)

data class CreateProductChatRoomRequest(
    @SerializedName("product_id")
    val productId: Long,

    @SerializedName("seller_id")
    val sellerId: Long
)

data class CreateChatMessageRequest(
    @SerializedName("content")
    val content: String,

    @SerializedName("message_type")
    val messageType: String
)

fun ChatRoomResponse.toDomain(): ChatRoomModel {
    return ChatRoomModel(
        chatRoomId = chatRoomId ?: 0L,
        productId = productId ?: 0L,
        buyerId = buyerId ?: 0L,
        sellerId = sellerId ?: 0L,
        createdAt = createdAt.orEmpty(),
        updatedAt = updatedAt.orEmpty()
    )
}

fun ChatMessageResponse.toDomain(): ChatMessageModel {
    return ChatMessageModel(
        messageId = messageId ?: 0L,
        chatRoomId = chatRoomId ?: 0L,
        senderId = senderId ?: 0L,
        content = content.orEmpty(),
        messageType = ChatMessageType.from(messageType),
        createdAt = createdAt.orEmpty()
    )
}

fun ChatImageResponse.toDomain(): ChatImageModel {
    return ChatImageModel(
        imageId = imageId ?: 0L,
        imageUrl = imageUrl.orEmpty(),
        referenceType = referenceType.orEmpty(),
        referenceId = referenceId ?: 0L,
        sortOrder = sortOrder ?: 0,
        createdAt = createdAt.orEmpty()
    )
}