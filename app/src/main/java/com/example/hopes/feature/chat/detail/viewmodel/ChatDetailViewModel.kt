package com.example.hopes.feature.chat.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.core.connectivity.NetworkMonitor
import com.example.hopes.domain.model.Chat
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel
    @Inject
    constructor(
        private val createChatUseCase: CreateChatUseCase,
        private val getChatUseCase: GetChatUseCase,
        private val sendChatMessageUseCase: SendChatMessageUseCase,
        private val networkMonitor: NetworkMonitor,
    ) : ViewModel() {
        /** 대화가 생성되면 이후의 추가 질문 전송이 실제 chatId를 쓰도록 갱신한다. */
        private var chatId: Long = NEW_CHAT_ID
        private var isInitialized = false

        private val _uiState = MutableStateFlow(ChatDetailUiState())
        val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

        private var loadChatJob: Job? = null
        private var sendJob: Job? = null

        /** 지금 답변을 기다리는 질문이다. 답변 중지 시 다시 시도할 질문으로 남긴다. */
        private var inFlightQuestion: String? = null

        init {
            observeNetwork()
        }

        /** NavKey로 전달받은 chatId/question을 1회만 반영해 대화 생성 또는 로드를 시작한다. */
        fun initialize(
            chatId: Long,
            question: String,
        ) {
            if (isInitialized) return
            isInitialized = true
            this.chatId = chatId
            if (chatId == NEW_CHAT_ID && question.isNotEmpty()) {
                _uiState.update { it.copy(isLoading = false) }
                sendQuestion(question, attachments = emptyList(), baseMessages = emptyList())
            } else {
                loadChat()
            }
        }

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

                ChatDetailScreenEvent.StopClicked -> {
                    stopAnswer()
                }

                is ChatDetailScreenEvent.ReplyChanged -> {
                    _uiState.update { it.copy(replyText = event.value) }
                }

                ChatDetailScreenEvent.ReplySubmitted -> {
                    val state = _uiState.value
                    val trimmedReply = state.replyText.trim()
                    if (trimmedReply.isNotEmpty() && state.isReplyEnabled && state.canSendDraftAttachments) {
                        _uiState.update { it.copy(replyText = "", draftAttachments = emptyList()) }
                        // 업로드에 실패한 첨부는 빼고 보낸다(피그마 10-3 "빼고 보낼 수 있어요").
                        val sendableAttachments =
                            state.draftAttachments.filter {
                                it.status ==
                                    ChatAttachmentStatus.Ready
                            }
                        sendQuestion(trimmedReply, attachments = sendableAttachments, baseMessages = state.messages)
                    }
                }

                is ChatDetailScreenEvent.FollowUpQuestionClicked -> {
                    _uiState.update { it.copy(replyText = event.question) }
                }

                is ChatDetailScreenEvent.AttachmentsAdded -> {
                    _uiState.update { state ->
                        val existingUris = state.draftAttachments.map { it.uri }.toSet()
                        val newAttachments = event.attachments.filter { it.uri !in existingUris }
                        state.copy(draftAttachments = (state.draftAttachments + newAttachments).limitAttachmentCount())
                    }
                }

                is ChatDetailScreenEvent.AttachmentRemoved -> {
                    // 하나를 빼면 개수 제한에 걸렸던 첨부가 다시 들어갈 수 있다.
                    _uiState.update { state ->
                        state.copy(
                            draftAttachments =
                                state.draftAttachments.filterNot { it.uri == event.uri }.limitAttachmentCount(),
                        )
                    }
                }

                // 업로드 API 연동 전이라 업로드 실패 상태가 생기지 않는다. 연동 시 여기서 다시 올린다.
                is ChatDetailScreenEvent.AttachmentRetryClicked -> {
                    Unit
                }

                // 위치 기능 연동 전이라 권한 요청·측정은 아직 없다. 연동 시 여기서 권한을 묻고 위치를 측정한다.
                ChatDetailScreenEvent.LocationAllowClicked,
                ChatDetailScreenEvent.LocationRetryClicked,
                -> {
                    Unit
                }

                ChatDetailScreenEvent.LocationLaterClicked -> {
                    _uiState.update { it.copy(location = null) }
                }

                is ChatDetailScreenEvent.LikeClicked -> {
                    // 좋아요 API 연동 전이라 화면에서만 표시한다.
                    _uiState.update { state ->
                        val liked = state.likedMessageIds
                        state.copy(
                            likedMessageIds =
                                if (event.messageId in
                                    liked
                                ) {
                                    liked - event.messageId
                                } else {
                                    liked + event.messageId
                                },
                        )
                    }
                }
            }
        }

        /** 기존 대화의 첫 메시지 페이지를 불러온다. */
        private fun loadChat() {
            loadChatJob?.cancel()
            loadChatJob =
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true, isLoadError = false, failedQuestion = null) }
                    when (val result = getChatUseCase(chatId, FIRST_MESSAGE_PAGE, MESSAGE_PAGE_SIZE)) {
                        is AppResult.Success -> {
                            _uiState.update { it.copy(isLoading = false).withChat(result.value) }
                        }

                        else -> {
                            _uiState.update { it.copy(isLoading = false, isLoadError = true) }
                        }
                    }
                }
        }

        /**
         * 질문을 보내고 답변을 기다린다. 서버 응답 전에 질문을 먼저 보여주고, 그 아래에 답변 생성 중 표시를 띄운다.
         * 아직 대화가 없으면(채팅 홈에서 바로 들어온 경우) 대화를 만든 뒤 보낸다.
         */
        private fun sendQuestion(
            question: String,
            attachments: List<ChatAttachmentUiModel>,
            baseMessages: List<ChatMessageUiModel>,
        ) {
            sendJob?.cancel()
            inFlightQuestion = question
            sendJob =
                viewModelScope.launch {
                    val optimisticQuestion =
                        ChatMessageUiModel(id = null, content = question, isUser = true, attachments = attachments)
                    _uiState.update {
                        it.copy(
                            messages = baseMessages + optimisticQuestion,
                            isSending = true,
                            failedQuestion = null,
                            pendingQuestion = null,
                        )
                    }

                    // 첨부 업로드 API 연동 전이라 질문 글만 보낸다. 첨부는 화면의 내 질문에만 붙여 둔다.
                    when (val result = requestAnswer(question)) {
                        is AppResult.Success -> {
                            _uiState.update {
                                it
                                    .copy(
                                        isSending = false,
                                    ).withChat(result.value)
                                    .withAttachmentsOn(question, attachments)
                            }
                        }

                        AppResult.NetworkError if _uiState.value.isOffline -> {
                            // 기기 연결이 끊겨 못 보냈으면 질문을 남겨 두고, 다시 연결되면 자동으로 보낸다(피그마 08-5).
                            _uiState.update { it.copy(isSending = false, pendingQuestion = question) }
                        }

                        else -> {
                            _uiState.update {
                                it.copy(
                                    isSending = false,
                                    failedQuestion = question,
                                    failureReason = ChatAnswerFailureReason.Error,
                                )
                            }
                        }
                    }
                    inFlightQuestion = null
                }
        }

        private suspend fun requestAnswer(question: String): AppResult<Chat> {
            if (chatId == NEW_CHAT_ID) {
                when (val createResult = createChatUseCase(question)) {
                    is AppResult.Success -> chatId = createResult.value.id
                    is AppResult.HttpError -> return createResult
                    AppResult.NetworkError -> return AppResult.NetworkError
                    AppResult.SerializationError -> return AppResult.SerializationError
                }
            }
            return sendChatMessageUseCase(chatId, question)
        }

        /**
         * 답변 기다리기를 멈춘다(피그마 08-2 "답변 중지"). 서버 생성까지 취소하는 API는 없어서
         * 화면에서만 기다림을 끝내고, 같은 질문을 다시 보낼 수 있게 남긴다.
         */
        private fun stopAnswer() {
            val question = inFlightQuestion ?: return
            sendJob?.cancel()
            inFlightQuestion = null
            _uiState.update {
                it.copy(
                    isSending = false,
                    failedQuestion = question,
                    failureReason = ChatAnswerFailureReason.Stopped,
                )
            }
        }

        /** 답변을 받지 못한 질문을 화면에서 빼고 같은 질문(첨부 포함)으로 다시 보낸다. */
        private fun resendFailedQuestion() {
            val failedQuestion = _uiState.value.failedQuestion ?: return
            if (_uiState.value.isSending) return

            resend(failedQuestion)
        }

        private fun resendPendingQuestion() {
            val pendingQuestion = _uiState.value.pendingQuestion ?: return
            if (_uiState.value.isSending) return

            resend(pendingQuestion)
        }

        private fun resend(question: String) {
            val messages = _uiState.value.messages
            val trailingQuestion = messages.lastOrNull()?.takeIf { it.isUser }
            sendQuestion(
                question = question,
                attachments = trailingQuestion?.attachments.orEmpty(),
                baseMessages = messages.withoutTrailingQuestion(),
            )
        }

        private fun observeNetwork() {
            viewModelScope.launch {
                networkMonitor.isOnline.collect { isOnline ->
                    _uiState.update { it.copy(isOffline = !isOnline) }
                    if (isOnline) resendPendingQuestion()
                }
            }
        }

        /** 마지막 메시지가 아직 답을 못 받은 내 질문이면 뺀다. 다시 보낼 때 같은 질문이 두 번 보이지 않게 한다. */
        private fun List<ChatMessageUiModel>.withoutTrailingQuestion(): List<ChatMessageUiModel> =
            if (lastOrNull()?.isUser == true) dropLast(1) else this

        private fun ChatDetailUiState.withChat(chat: Chat): ChatDetailUiState =
            copy(
                title = chat.title,
                messages = chat.messages.map { message -> message.toChatMessageUiModel() },
            )

        /** 서버 목록으로 바뀐 뒤에도 방금 보낸 질문에 붙인 첨부가 화면에 남도록 다시 붙인다. */
        private fun ChatDetailUiState.withAttachmentsOn(
            question: String,
            attachments: List<ChatAttachmentUiModel>,
        ): ChatDetailUiState {
            if (attachments.isEmpty()) return this
            val targetIndex = messages.indexOfLast { it.isUser && it.content == question }
            if (targetIndex < 0) return this

            return copy(
                messages =
                    messages.mapIndexed { index, message ->
                        if (index == targetIndex) message.copy(attachments = attachments) else message
                    },
            )
        }

        private fun ChatMessage.toChatMessageUiModel(): ChatMessageUiModel =
            ChatMessageUiModel(
                id = id,
                content = content,
                isUser = role == ChatMessageRole.User,
            )

        private companion object {
            const val FIRST_MESSAGE_PAGE = 0
            const val MESSAGE_PAGE_SIZE = 20
        }
    }

/**
 * 첨부 개수 제한을 넘은 첨부를 [ChatAttachmentStatus.OverLimit]로 바꾼다(피그마 10-5).
 * 형식·용량·손상 때문에 이미 막힌 첨부는 개수에 넣지 않는다.
 */
internal fun List<ChatAttachmentUiModel>.limitAttachmentCount(): List<ChatAttachmentUiModel> {
    var acceptedCount = 0
    return map { attachment ->
        val isRejectedForOtherReason =
            attachment.status.isRejected && attachment.status != ChatAttachmentStatus.OverLimit
        when {
            isRejectedForOtherReason -> {
                attachment
            }

            acceptedCount < MAX_ATTACHMENT_COUNT -> {
                acceptedCount++
                if (attachment.status == ChatAttachmentStatus.OverLimit) {
                    attachment.copy(status = ChatAttachmentStatus.Ready)
                } else {
                    attachment
                }
            }

            else -> {
                attachment.copy(status = ChatAttachmentStatus.OverLimit)
            }
        }
    }
}

// 피그마 10-5에서 네 번째 이미지가 막히는 것을 기준으로 정했다. 서버 제한이 정해지면 맞춘다.
internal const val MAX_ATTACHMENT_COUNT = 3

// 파일 한 개 용량 제한이다. 서버 제한이 정해지면 맞춘다.
internal const val MAX_ATTACHMENT_SIZE_BYTES = 20L * 1024 * 1024
