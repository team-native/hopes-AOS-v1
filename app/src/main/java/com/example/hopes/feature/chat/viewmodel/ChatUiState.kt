package com.example.hopes.feature.chat.viewmodel

/**
 * 채팅 홈 상태다.
 *
 * @param department 고른 학과다. null이면 "학과 선택" 칩을 보여준다.
 * @param major 고른 관심 전공이다. null이면 "전공 선택" 칩을 보여준다.
 */
data class ChatUiState(
    val questionText: String = "",
    val department: ChatDepartment? = null,
    val major: ChatMajor? = null,
    val recommendationState: ChatRecommendationState = ChatRecommendationState.Loaded,
) {
    /** 학과와 전공을 모두 골라 맞춤 추천 질문을 보여주는 상태인지 여부다. */
    val isPersonalized: Boolean
        get() = department != null && major != null

    val recommendedTopics: List<ChatQuestionTopic>
        get() =
            if (recommendationState == ChatRecommendationState.Loaded) {
                recommendedQuestionTopics(department, major)
            } else {
                recommendedQuestionTopics(department = null, major = null)
            }
}
