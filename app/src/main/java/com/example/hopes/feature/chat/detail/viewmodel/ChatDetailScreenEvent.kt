package com.example.hopes.feature.chat.detail.viewmodel

sealed interface ChatDetailScreenEvent {
    data object BackClicked : ChatDetailScreenEvent

    /** 기존 대화 불러오기를 다시 시도한다. */
    data object RetryClicked : ChatDetailScreenEvent

    /** 답변을 받지 못한 질문을 같은 내용으로 다시 보낸다. */
    data object ResendClicked : ChatDetailScreenEvent

    /** 답변 기다리기를 멈춘다(피그마 08-2). */
    data object StopClicked : ChatDetailScreenEvent

    data class ReplyChanged(
        val value: String,
    ) : ChatDetailScreenEvent

    data object ReplySubmitted : ChatDetailScreenEvent

    /** "추가로 많이 묻는 질문"이나 "더 물어보기"를 누르면 그 질문을 입력창에 채운다. */
    data class FollowUpQuestionClicked(
        val question: String,
    ) : ChatDetailScreenEvent

    data class LikeClicked(
        val messageId: Long,
    ) : ChatDetailScreenEvent

    data class AttachmentsAdded(
        val attachments: List<ChatAttachmentUiModel>,
    ) : ChatDetailScreenEvent

    data class AttachmentRemoved(
        val uri: String,
    ) : ChatDetailScreenEvent
}
