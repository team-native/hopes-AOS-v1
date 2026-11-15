package com.example.hopes.feature.chat.viewmodel

sealed interface ChatScreenEvent {
    data class QuestionChanged(
        val question: String,
    ) : ChatScreenEvent

    data object QuestionSubmitted : ChatScreenEvent

    /** 추천 질문을 누르면 바로 보내지 않고 입력창에 채운다(피그마 05-5). */
    data class SuggestionClicked(
        val question: String,
    ) : ChatScreenEvent

    data object NewChatClicked : ChatScreenEvent

    data class DepartmentSelected(
        val department: ChatDepartment?,
    ) : ChatScreenEvent

    data class MajorSelected(
        val major: ChatMajor?,
    ) : ChatScreenEvent

    data object RecommendationsRefreshClicked : ChatScreenEvent
}
