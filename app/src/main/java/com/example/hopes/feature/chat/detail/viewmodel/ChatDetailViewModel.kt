package com.example.hopes.feature.chat.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ChatMessage
import com.example.hopes.domain.model.ChatMessageRole
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.CreateChatUseCase
import com.example.hopes.domain.usecase.GetChatUseCase
import com.example.hopes.domain.usecase.SendChatMessageUseCase
import com.example.hopes.navigation.NEW_CHAT_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel
    @Inject
    constructor(
        private val createChatUseCase: CreateChatUseCase,
        private val getChatUseCase: GetChatUseCase,
        private val sendChatMessageUseCase: SendChatMessageUseCase,
    ) : ViewModel() {
        /** 대화가 생성되면 이후의 추가 질문 전송이 실제 chatId를 쓰도록 갱신한다. */
        private var chatId: Long = NEW_CHAT_ID
        private var isInitialized = false

        private val _uiState = MutableStateFlow(ChatDetailUiState())
        val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

        private var loadChatJob: Job? = null

        /** NavKey로 전달받은 chatId/question을 1회만 반영해 대화 생성 또는 로드를 시작한다. */
        fun initialize(
            chatId: Long,
            question: String,
        ) {
            if (isInitialized) return
            isInitialized = true
            this.chatId = chatId
            if (chatId == NEW_CHAT_ID && question.isNotEmpty()) {
                createNewChat(question)
            } else {
                loadChat()
            }
        }

        /** 상세 진입 또는 재시도 시 첫 메시지 페이지를 요청해 성공·오류 상태를 갱신한다. */
        fun loadChat() {
            loadChatJob?.cancel()
            loadChatJob =
                viewModelScope.launch {
                    updateState { copy(isLoading = true, isLoadError = false, failedQuestion = null) }
                    when (val result = getChatUseCase(chatId, FIRST_MESSAGE_PAGE, MESSAGE_PAGE_SIZE)) {
                        is AppResult.Success -> {
                            updateState {
                                copy(
                                    title = result.value.title,
                                    messages =
                                        result.value.messages.map { message ->
                                            message.toChatMessageUiModel()
                                        },
                                    isLoading = false,
                                )
                            }
                        }

                        else -> {
                            updateState { copy(isLoading = false, isLoadError = true) }
                        }
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
            loadChatJob =
                viewModelScope.launch {
                    val optimisticQuestion = ChatMessageUiModel(id = null, content = question, isUser = true)
                    updateState {
                        copy(
                            isLoading = false,
                            isLoadError = false,
                            messages = listOf(optimisticQuestion),
                            isSending = true,
                            failedQuestion = null,
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
                                            messages =
                                                sendResult.value.messages.map { message ->
                                                    message.toChatMessageUiModel()
                                                },
                                            isSending = false,
                                        )
                                    }
                                }

                                else -> {
                                    updateState { copy(isSending = false, failedQuestion = question) }
                                }
                            }
                        }

                        else -> {
                            updateState { copy(isSending = false, failedQuestion = question) }
                        }
                    }
                }
        }

        /** 추가 질문 입력과 전송 의도를 처리하고, 전송 성공 시 서버 대화 응답으로 목록을 교체한다. */
        fun onEvent(event: ChatDetailScreenEvent) {
            when (event) {
                ChatDetailScreenEvent.BackClicked -> {
                    Unit
                }

                ChatDetailScreenEvent.RetryClicked -> {
                    loadChat()
                }

                ChatDetailScreenEvent.ResendClicked -> {
                    resendFailedQuestion()
                }

                is ChatDetailScreenEvent.ReplyChanged -> {
                    updateState { copy(replyText = event.value) }
                }

                ChatDetailScreenEvent.ReplySubmitted -> {
                    val trimmedReply = _uiState.value.replyText.trim()
                    if (trimmedReply.isNotEmpty() && _uiState.value.isReplyEnabled) {
                        updateState { copy(replyText = "") }
                        sendReply(trimmedReply, baseMessages = _uiState.value.messages)
                    }
                }
            }
        }

        /**
         * 답변을 받지 못한 질문을 다시 보낸다. 대화 생성부터 실패했다면 생성부터 다시 하고,
         * 아니면 화면에 남겨 둔 실패한 질문을 빼고 같은 질문을 다시 보낸다.
         */
        private fun resendFailedQuestion() {
            val failedQuestion = _uiState.value.failedQuestion ?: return
            if (_uiState.value.isSending) return

            if (chatId == NEW_CHAT_ID) {
                createNewChat(failedQuestion)
                return
            }

            val messagesWithoutFailedQuestion =
                _uiState.value.messages.let { messages ->
                    if (messages.lastOrNull()?.isUser == true) messages.dropLast(1) else messages
                }
            sendReply(failedQuestion, baseMessages = messagesWithoutFailedQuestion)
        }

        private fun sendReply(
            question: String,
            baseMessages: List<ChatMessageUiModel>,
        ) {
            viewModelScope.launch {
                // 서버 응답 전에 내가 보낸 질문을 먼저 보여준 뒤, 그 아래에 답변 생성 중 표시를 띄운다.
                val optimisticReply = ChatMessageUiModel(id = null, content = question, isUser = true)
                updateState {
                    copy(
                        messages = baseMessages + optimisticReply,
                        isSending = true,
                        failedQuestion = null,
                    )
                }
                when (val result = sendChatMessageUseCase(chatId, question)) {
                    is AppResult.Success -> {
                        updateState {
                            copy(
                                title = result.value.title,
                                messages =
                                    result.value.messages.map { message ->
                                        message.toChatMessageUiModel()
                                    },
                                isSending = false,
                            )
                        }
                    }

                    else -> {
                        // 질문은 화면에 남겨 두고 그 아래에 실패 안내와 다시 시도를 보여준다.
                        updateState { copy(isSending = false, failedQuestion = question) }
                    }
                }
            }
        }

        private fun ChatMessage.toChatMessageUiModel(): ChatMessageUiModel =
            ChatMessageUiModel(
                id = id,
                content = content,
                isUser = role == ChatMessageRole.User,
            )

        private fun updateState(transform: ChatDetailUiState.() -> ChatDetailUiState) {
            _uiState.value = _uiState.value.transform()
        }

        private companion object {
            const val FIRST_MESSAGE_PAGE = 0
            const val MESSAGE_PAGE_SIZE = 20
        }
    }
