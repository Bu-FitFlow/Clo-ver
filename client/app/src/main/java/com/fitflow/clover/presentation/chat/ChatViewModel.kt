package com.fitflow.clover.presentation.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {

    private val messageStore = mutableMapOf<Long, List<ChatMessageUiModel>>()

    private val _uiState = MutableStateFlow(
        ChatUiState(
            galleryImages = createInitialGalleryImages()
        )
    )
    val uiState: StateFlow<ChatUiState> = _uiState

    fun openProductChatRoom(
        productId: Long = 1L,
        productName: String = "어 뭐 상의라고 해두지",
        productImageUrl: String? = null,
        sellerId: Long = 2L,
        sellerNickname: String = "판매자",
        sellerProfileImageUrl: String? = null,
        buyerId: Long = 1L,
        buyerNickname: String = "구매자",
        buyerProfileImageUrl: String? = null
    ) {
        val existingRoom = _uiState.value.chatRooms.firstOrNull { room ->
            room.productId == productId &&
                    room.sellerId == sellerId &&
                    room.buyerId == buyerId
        }

        val room = existingRoom ?: ChatRoomUiModel(
            chatRoomId = createChatRoomId(
                productId = productId,
                sellerId = sellerId,
                buyerId = buyerId
            ),
            productId = productId,
            productName = productName,
            productImageUrl = productImageUrl,
            sellerId = sellerId,
            sellerNickname = sellerNickname,
            sellerProfileImageUrl = sellerProfileImageUrl,
            buyerId = buyerId,
            buyerNickname = buyerNickname,
            buyerProfileImageUrl = buyerProfileImageUrl,
            lastMessage = "",
            lastMessageTime = "",
            unreadCount = 0
        )

        val roomMessages = messageStore[room.chatRoomId].orEmpty()

        _uiState.update {
            it.copy(
                selectedRoom = room,
                messages = roomMessages,
                messageInput = "",
                selectedDateLabel = todayLabel(),
                isAttachmentPanelVisible = false,
                isFullGalleryVisible = false,
                errorMessage = null
            )
        }
    }

    fun selectChatRoom(chatRoomId: Long) {
        val selectedRoom = _uiState.value.chatRooms.firstOrNull { room ->
            room.chatRoomId == chatRoomId
        } ?: return

        val roomMessages = messageStore[chatRoomId].orEmpty()

        _uiState.update {
            it.copy(
                selectedRoom = selectedRoom,
                messages = roomMessages,
                messageInput = "",
                selectedDateLabel = todayLabel(),
                isAttachmentPanelVisible = false,
                isFullGalleryVisible = false,
                errorMessage = null
            )
        }
    }

    fun backToChatList() {
        _uiState.update {
            it.copy(
                selectedRoom = null,
                messages = emptyList(),
                messageInput = "",
                selectedDateLabel = "",
                isAttachmentPanelVisible = false,
                isFullGalleryVisible = false,
                errorMessage = null
            )
        }
    }

    fun changeFilter(filter: ChatRoomFilterUiType) {
        _uiState.update {
            it.copy(
                selectedFilter = filter,
                selectedRoom = null,
                messages = emptyList(),
                messageInput = "",
                selectedDateLabel = "",
                isAttachmentPanelVisible = false,
                isFullGalleryVisible = false,
                errorMessage = null
            )
        }
    }

    fun updateMessageInput(value: String) {
        _uiState.update {
            it.copy(messageInput = value)
        }
    }

    fun toggleAttachmentPanel() {
        _uiState.update {
            it.copy(
                isAttachmentPanelVisible = !it.isAttachmentPanelVisible
            )
        }
    }

    fun openFullGallery() {
        _uiState.update {
            it.copy(isFullGalleryVisible = true)
        }
    }

    fun closeFullGallery() {
        _uiState.update {
            it.copy(isFullGalleryVisible = false)
        }
    }

    fun setGalleryImages(imageUris: List<String>) {
        _uiState.update {
            it.copy(
                galleryImages = imageUris.mapIndexed { index, uri ->
                    ChatGalleryImageUiModel(
                        imageId = index.toLong() + 1L,
                        imageUrl = uri,
                        sortOrder = index
                    )
                }
            )
        }
    }

    fun sendTextMessage() {
        val state = _uiState.value
        val room = state.selectedRoom ?: return
        val input = state.messageInput.trim()

        if (input.isEmpty()) return

        val newMessage = ChatMessageUiModel(
            messageId = System.currentTimeMillis(),
            chatRoomId = room.chatRoomId,
            senderId = state.currentMemberId,
            senderNickname = room.buyerNickname,
            senderProfileImageUrl = room.buyerProfileImageUrl,
            content = input,
            imageUrl = null,
            messageType = ChatMessageType.TEXT,
            createdAt = "방금 전"
        )

        val updatedMessages = messageStore[room.chatRoomId].orEmpty() + newMessage
        messageStore[room.chatRoomId] = updatedMessages

        val updatedRoom = room.copy(
            lastMessage = input,
            lastMessageTime = "방금 전",
            unreadCount = 0
        )

        _uiState.update {
            it.copy(
                selectedRoom = updatedRoom,
                chatRooms = upsertChatRoom(
                    rooms = it.chatRooms,
                    room = updatedRoom
                ),
                messages = updatedMessages,
                messageInput = "",
                selectedDateLabel = todayLabel(),
                isAttachmentPanelVisible = false,
                isFullGalleryVisible = false,
                errorMessage = null
            )
        }
    }

    fun sendImageMessage(imageUrl: String?) {
        val state = _uiState.value
        val room = state.selectedRoom ?: return

        val safeImageUrl = imageUrl?.trim()

        val newMessage = ChatMessageUiModel(
            messageId = System.currentTimeMillis(),
            chatRoomId = room.chatRoomId,
            senderId = state.currentMemberId,
            senderNickname = room.buyerNickname,
            senderProfileImageUrl = room.buyerProfileImageUrl,
            content = safeImageUrl ?: "사진을 보냈습니다.",
            imageUrl = safeImageUrl,
            messageType = ChatMessageType.IMAGE,
            createdAt = "방금 전"
        )

        val updatedMessages = messageStore[room.chatRoomId].orEmpty() + newMessage
        messageStore[room.chatRoomId] = updatedMessages

        val updatedRoom = room.copy(
            lastMessage = "사진을 보냈습니다.",
            lastMessageTime = "방금 전",
            unreadCount = 0
        )

        _uiState.update {
            it.copy(
                selectedRoom = updatedRoom,
                chatRooms = upsertChatRoom(
                    rooms = it.chatRooms,
                    room = updatedRoom
                ),
                messages = updatedMessages,
                selectedDateLabel = todayLabel(),
                isAttachmentPanelVisible = false,
                isFullGalleryVisible = false,
                errorMessage = null
            )
        }
    }

    private fun upsertChatRoom(
        rooms: List<ChatRoomUiModel>,
        room: ChatRoomUiModel
    ): List<ChatRoomUiModel> {
        val filteredRooms = rooms.filterNot { existingRoom ->
            existingRoom.chatRoomId == room.chatRoomId
        }

        return listOf(room) + filteredRooms
    }

    private fun createChatRoomId(
        productId: Long,
        sellerId: Long,
        buyerId: Long
    ): Long {
        return productId * 10_000L + sellerId * 100L + buyerId
    }

    private fun todayLabel(): String {
        return "2026.04.04 (일요일)"
    }

    private companion object {
        fun createInitialGalleryImages(): List<ChatGalleryImageUiModel> {
            return List(12) { index ->
                ChatGalleryImageUiModel(
                    imageId = index.toLong() + 1L,
                    imageUrl = null,
                    sortOrder = index
                )
            }
        }
    }
}