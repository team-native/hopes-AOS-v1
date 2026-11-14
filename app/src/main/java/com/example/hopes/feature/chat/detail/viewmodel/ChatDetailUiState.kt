package com.example.hopes.feature.chat.detail.viewmodel

/**
 * 서버 대화 상세의 현재 표시 상태다.
 *
 * @param isLoadError 기존 대화를 불러오지 못했다. 목록 대신 불러오기 오류 안내를 보여준다.
 * @param failedQuestion 답변을 받지 못한 질문이다. 그 질문 아래에 "답변을 만들지 못했어요" 안내와 다시 시도를 보여준다(피그마 08-3).
 */
data class ChatDetailUiState(
    val title: String = "",
    val messages: List<ChatMessageUiModel> = emptyList(),
    val replyText: String = "",
    val isLoading: Boolean = true,
    val isLoadError: Boolean = false,
    val isSending: Boolean = false,
    val failedQuestion: String? = null,
) {
    /** 답변을 기다리는 중이거나 대화를 불러오지 못했으면 추가 질문을 막는다. */
    val isReplyEnabled: Boolean
        get() = !isLoading && !isLoadError && !isSending
}

data class ChatMessageUiModel(
    val id: Long?,
    val content: String,
    val isUser: Boolean,
)
