package com.example.hopes.feature.chat.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ChatMessage
import com.example.hopes.domain.model.ChatMessageRole
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetChatUseCase
import com.example.hopes.domain.usecase.SendChatMessageUseCase
import com.example.hopes.navigation.CHAT_DETAIL_ARGUMENT
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getChatUseCase: GetChatUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
) : ViewModel() {
    private val chatId: Long = checkNotNull(savedStateHandle[CHAT_DETAIL_ARGUMENT])
    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var loadChatJob: Job? = null

    init {
        loadChat()
    }

    /** 상세 진입 또는 재시도 시 첫 메시지 페이지를 요청해 성공·오류 상태를 갱신한다. */
    fun loadChat() {
        loadChatJob?.cancel()
        loadChatJob = viewModelScope.launch {
            updateState { copy(isLoading = true, isLoadError = false, isSendError = false) }
            when (val result = getChatUseCase(chatId, FIRST_MESSAGE_PAGE, MESSAGE_PAGE_SIZE)) {
                is AppResult.Success -> {
                    updateState {
                        copy(
                            title = result.value.title,
                            messages = result.value.messages.map { message ->
                                message.toChatMessageUiModel()
                            },
                            isLoading = false,
                        )
                    }
                }

                else -> updateState { copy(isLoading = false, isLoadError = true) }
            }
        }
    }

    /** 추가 질문 입력과 전송 의도를 처리하고, 전송 성공 시 서버 대화 응답으로 목록을 교체한다. */
    fun onEvent(event: ChatDetailScreenEvent) {
        when (event) {
            ChatDetailScreenEvent.BackClicked -> Unit
            ChatDetailScreenEvent.RetryClicked -> loadChat()
            is ChatDetailScreenEvent.ReplyChanged -> updateState { copy(replyText = event.value, isSendError = false) }
            ChatDetailScreenEvent.ReplySubmitted -> sendReply()
        }
    }

    private fun sendReply() {
        val trimmedReply = _uiState.value.replyText.trim()
        if (trimmedReply.isEmpty()) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isSendError = false) }
            when (val result = sendChatMessageUseCase(chatId, trimmedReply)) {
                is AppResult.Success -> {
                    updateState {
                        copy(
                            title = result.value.title,
                            messages = result.value.messages.map { message ->
                                message.toChatMessageUiModel()
                            },
                            replyText = "",
                        )
                    }
                }

                else -> updateState { copy(isSendError = true) }
            }
        }
    }

    private fun ChatMessage.toChatMessageUiModel(): ChatMessageUiModel {
        return ChatMessageUiModel(
            id = id,
            content = content,
            isUser = role == ChatMessageRole.User,
        )
    }

    private fun updateState(transform: ChatDetailUiState.() -> ChatDetailUiState) {
        _uiState.value = _uiState.value.transform()
    }

    private companion object {
        const val FIRST_MESSAGE_PAGE = 0
        const val MESSAGE_PAGE_SIZE = 20
    }
}
