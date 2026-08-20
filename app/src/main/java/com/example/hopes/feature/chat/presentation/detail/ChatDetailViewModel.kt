package com.example.hopes.feature.chat.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ChatMessage
import com.example.hopes.domain.model.ChatMessageRole
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.CreateChatUseCase
import com.example.hopes.domain.usecase.GetChatUseCase
import com.example.hopes.domain.usecase.SendChatMessageUseCase
import com.example.hopes.navigation.CHAT_DETAIL_ARGUMENT
import com.example.hopes.navigation.CHAT_DETAIL_QUESTION_ARGUMENT
import com.example.hopes.navigation.NEW_CHAT_ID
import com.example.hopes.navigation.decodeChatDetailQuestion
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
    private val createChatUseCase: CreateChatUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
) : ViewModel() {
    /** 대화가 생성되면 이후의 추가 질문 전송이 실제 chatId를 쓰도록 갱신한다. */
    private var chatId: Long = checkNotNull(savedStateHandle[CHAT_DETAIL_ARGUMENT])
    private val initialQuestion: String =
        decodeChatDetailQuestion(savedStateHandle[CHAT_DETAIL_QUESTION_ARGUMENT])

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var loadChatJob: Job? = null

    init {
        if (chatId == NEW_CHAT_ID && initialQuestion.isNotEmpty()) {
            createNewChat(initialQuestion)
        } else {
            loadChat()
        }
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

    /**
     * 채팅 홈에서 질문을 곧바로 상세 화면으로 들고 온 경우, 여기서 대화를 만들고 첫 질문을 보낸다.
     * 서버 응답을 기다리는 동안에도 내가 보낸 질문을 먼저 보여준 뒤, 그 아래에 추가 질문 전송과
     * 같은 로딩 말풍선(isSending)을 띄운다.
     */
    private fun createNewChat(question: String) {
        loadChatJob?.cancel()
        loadChatJob = viewModelScope.launch {
            val optimisticQuestion = ChatMessageUiModel(id = null, content = question, isUser = true)
            updateState {
                copy(
                    isLoading = false,
                    isLoadError = false,
                    messages = listOf(optimisticQuestion),
                    isSending = true,
                )
            }
            when (val createResult = createChatUseCase(question)) {
                is AppResult.Success -> {
                    chatId = createResult.value.id
                    when (val sendResult = sendChatMessageUseCase(chatId, question)) {
                        is AppResult.Success -> {
                            updateState {
                                copy(
                                    title = sendResult.value.title,
                                    messages = sendResult.value.messages.map { message ->
                                        message.toChatMessageUiModel()
                                    },
                                    isSending = false,
                                )
                            }
                        }

                        else -> updateState { copy(messages = emptyList(), isSending = false, isLoadError = true) }
                    }
                }

                else -> updateState { copy(messages = emptyList(), isSending = false, isLoadError = true) }
            }
        }
    }

    /** 추가 질문 입력과 전송 의도를 처리하고, 전송 성공 시 서버 대화 응답으로 목록을 교체한다. */
    fun onEvent(event: ChatDetailScreenEvent) {
        when (event) {
            ChatDetailScreenEvent.BackClicked -> Unit
            ChatDetailScreenEvent.RetryClicked -> retry()
            is ChatDetailScreenEvent.ReplyChanged -> updateState { copy(replyText = event.value, isSendError = false) }
            ChatDetailScreenEvent.ReplySubmitted -> sendReply()
        }
    }

    /** 대화 생성이 실패했다면 생성부터, 기존 대화 로드가 실패했다면 로드부터 다시 시도한다. */
    private fun retry() {
        if (chatId == NEW_CHAT_ID && initialQuestion.isNotEmpty()) {
            createNewChat(initialQuestion)
        } else {
            loadChat()
        }
    }

    private fun sendReply() {
        val trimmedReply = _uiState.value.replyText.trim()
        if (trimmedReply.isEmpty()) {
            return
        }

        viewModelScope.launch {
            // 서버 응답 전에 내가 보낸 메시지를 먼저 보여준 뒤, 그 아래에 로딩 말풍선을 띄운다.
            val messagesBeforeSend = _uiState.value.messages
            val optimisticReply = ChatMessageUiModel(id = null, content = trimmedReply, isUser = true)
            updateState {
                copy(
                    messages = messagesBeforeSend + optimisticReply,
                    replyText = "",
                    isSending = true,
                    isSendError = false,
                )
            }
            when (val result = sendChatMessageUseCase(chatId, trimmedReply)) {
                is AppResult.Success -> {
                    updateState {
                        copy(
                            title = result.value.title,
                            messages = result.value.messages.map { message ->
                                message.toChatMessageUiModel()
                            },
                            isSending = false,
                        )
                    }
                }

                else -> updateState {
                    copy(
                        messages = messagesBeforeSend,
                        replyText = trimmedReply,
                        isSending = false,
                        isSendError = true,
                    )
                }
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
