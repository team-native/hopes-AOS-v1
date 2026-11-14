package com.example.hopes.feature.chat.detail.viewmodel

/**
 * 서버 대화 상세의 현재 표시 상태다.
 *
 * @param isLoadError 기존 대화를 불러오지 못했다. 목록 대신 불러오기 오류 안내를 보여준다.
 * @param failedQuestion 답변을 받지 못한 질문이다. 그 아래에 실패(08-3) 또는 중지 안내와 다시 시도를 보여준다.
 * @param pendingQuestion 오프라인이라 아직 못 보낸 질문이다. "연결되면 전송돼요"를 붙이고, 연결되면 자동으로 보낸다(08-5).
 * @param followUpQuestions 마지막 답변 아래 "추가로 많이 묻는 질문"이다. API 연동 전이라 지금은 비어 있어 카드가 숨겨진다.
 * @param likedMessageIds 좋아요를 누른 답변 id다. API 연동 전이라 화면에서만 유지한다.
 */
data class ChatDetailUiState(
    val title: String = "",
    val messages: List<ChatMessageUiModel> = emptyList(),
    val replyText: String = "",
    val isLoading: Boolean = true,
    val isLoadError: Boolean = false,
    val isSending: Boolean = false,
    val failedQuestion: String? = null,
    val failureReason: ChatAnswerFailureReason = ChatAnswerFailureReason.Error,
    val pendingQuestion: String? = null,
    val isOffline: Boolean = false,
    val followUpQuestions: List<String> = emptyList(),
    val likedMessageIds: Set<Long> = emptySet(),
) {
    /** 답변을 기다리는 중이거나, 대화를 불러오지 못했거나, 오프라인이면 추가 질문을 막는다. */
    val isReplyEnabled: Boolean
        get() = !isLoading && !isLoadError && !isSending && !isOffline && pendingQuestion == null
}

/** 답변을 받지 못한 이유다. 안내 문구만 달라지고 다시 시도 동작은 같다. */
enum class ChatAnswerFailureReason {
    Error,
    Stopped,
}

data class ChatMessageUiModel(
    val id: Long?,
    val content: String,
    val isUser: Boolean,
)
