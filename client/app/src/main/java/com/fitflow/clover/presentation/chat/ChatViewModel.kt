package com.fitflow.clover.presentation.chat

import androidx.lifecycle.ViewModel
import com.fitflow.clover.data.remote.api.ChatApi
import com.fitflow.clover.data.repository.ChatRepositoryImpl
import com.fitflow.clover.di.NetworkModule
import com.fitflow.clover.domain.modal.ChatMessageModel
import com.fitflow.clover.domain.modal.ChatRoomModel
import com.fitflow.clover.domain.usecase.ChatUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatUseCase: ChatUseCase = ChatUseCase(
        chatRepository = ChatRepositoryImpl(
            chatApi = NetworkModule.createApi<ChatApi>()
        )
    )
) : ViewModel() {

    private val viewModelScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    init {
        loadChatRooms()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun loadChatRooms() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                chatUseCase.getChatRooms(
                    filter = null
                )
            }.onSuccess { rooms ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        chatRooms = rooms.map { room ->
                            room.toUiModel()
                        },
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
            }
        }
    }

    fun openProductChatRoom(
        productId: Long,
        sellerId: Long
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isMessageLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                val room = chatUseCase.createOrGetProductChatRoom(
                    productId = productId,
                    sellerId = sellerId
                )

                val messages = chatUseCase.getChatMessages(
                    chatRoomId = room.chatRoomId
                )

                room to messages
            }.onSuccess { result ->
                val room = result.first
                val messages = result.second
                val roomUiModel = room.toUiModel(messages)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isMessageLoading = false,
                        selectedRoom = roomUiModel,
                        chatRooms = upsertChatRoom(
                            rooms = it.chatRooms,
                            room = roomUiModel
                        ),
                        messages = messages.map { message ->
                            message.toUiModel()
                        },
                        messageInput = "",
                        selectedDateLabel = messages.lastOrNull()?.createdAt.orEmpty(),
                        isAttachmentPanelVisible = false,
                        isFullGalleryVisible = false,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isMessageLoading = false,
                        errorMessage = throwable.message
                    )
                }
            }
        }
    }

    fun selectChatRoom(chatRoomId: Long) {
        val selectedRoom = _uiState.value.chatRooms.firstOrNull { room ->
            room.chatRoomId == chatRoomId
        } ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isMessageLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                chatUseCase.getChatMessages(
                    chatRoomId = chatRoomId
                )
            }.onSuccess { messages ->
                val updatedRoom = selectedRoom.copy(
                    lastMessage = messages.lastOrNull()?.content.orEmpty(),
                    lastMessageTime = messages.lastOrNull()?.createdAt.orEmpty()
                )

                _uiState.update {
                    it.copy(
                        isMessageLoading = false,
                        selectedRoom = updatedRoom,
                        chatRooms = upsertChatRoom(
                            rooms = it.chatRooms,
                            room = updatedRoom
                        ),
                        messages = messages.map { message ->
                            message.toUiModel()
                        },
                        messageInput = "",
                        selectedDateLabel = messages.lastOrNull()?.createdAt.orEmpty(),
                        isAttachmentPanelVisible = false,
                        isFullGalleryVisible = false,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isMessageLoading = false,
                        errorMessage = throwable.message
                    )
                }
            }
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

        loadChatRooms()
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
            it.copy(
                messageInput = value
            )
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
            it.copy(
                isFullGalleryVisible = true
            )
        }
    }

    fun closeFullGallery() {
        _uiState.update {
            it.copy(
                isFullGalleryVisible = false
            )
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

        viewModelScope.launch {
            runCatching {
                chatUseCase.sendTextMessage(
                    chatRoomId = room.chatRoomId,
                    content = input
                )
            }.onSuccess {
                selectChatRoom(room.chatRoomId)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        errorMessage = throwable.message
                    )
                }
            }
        }
    }

    fun sendImageMessage(imageUrl: String?) {
        val state = _uiState.value
        val room = state.selectedRoom ?: return
        val safeImageUrl = imageUrl?.trim().orEmpty()

        if (safeImageUrl.isBlank()) return

        viewModelScope.launch {
            runCatching {
                chatUseCase.sendImageMessage(
                    chatRoomId = room.chatRoomId,
                    imageUrl = safeImageUrl
                )
            }.onSuccess {
                selectChatRoom(room.chatRoomId)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        errorMessage = throwable.message
                    )
                }
            }
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

    private fun ChatRoomModel.toUiModel(
        messages: List<ChatMessageModel> = emptyList()
    ): ChatRoomUiModel {
        val lastMessage = messages.lastOrNull()

        return ChatRoomUiModel(
            chatRoomId = chatRoomId,
            productId = productId,
            productName = "상품 번호 $productId",
            productImageUrl = null,
            sellerId = sellerId,
            sellerNickname = "판매자 $sellerId",
            sellerProfileImageUrl = null,
            buyerId = buyerId,
            buyerNickname = "구매자 $buyerId",
            buyerProfileImageUrl = null,
            lastMessage = lastMessage?.content.orEmpty(),
            lastMessageTime = lastMessage?.createdAt.orEmpty(),
            unreadCount = 0
        )
    }

    private fun ChatMessageModel.toUiModel(): ChatMessageUiModel {
        return ChatMessageUiModel(
            messageId = messageId,
            chatRoomId = chatRoomId,
            senderId = senderId,
            senderNickname = "사용자 $senderId",
            senderProfileImageUrl = null,
            content = content,
            imageUrl = if (messageType.name == "IMAGE") {
                content
            } else {
                null
            },
            messageType = when (messageType.name) {
                "IMAGE" -> ChatMessageType.IMAGE
                "FILE" -> ChatMessageType.FILE
                else -> ChatMessageType.TEXT
            },
            createdAt = createdAt
        )
    }
}