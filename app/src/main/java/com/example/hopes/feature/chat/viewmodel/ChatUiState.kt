package com.example.hopes.feature.chat.viewmodel

import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentStatus
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel

/**
 * 채팅 홈 상태다.
 *
 * @param department 고른 학과다. null이면 "학과 선택" 칩을 보여준다.
 * @param major 고른 관심 전공이다. null이면 "전공 선택" 칩을 보여준다.
 * @param draftAttachments 입력창 위에 붙여 둔 첨부 자료다(피그마 10). 질문을 보내면 채팅 상세로 함께 넘어간다.
 */
data class ChatUiState(
    val questionText: String = "",
    val department: ChatDepartment? = null,
    val major: ChatMajor? = null,
    val recommendationState: ChatRecommendationState = ChatRecommendationState.Loaded,
    val draftAttachments: List<ChatAttachmentUiModel> = emptyList(),
) {
    /** 보낼 수 없는 첨부(형식·개수·용량·손상)가 있으면 전송을 막는다. */
    val canSendDraftAttachments: Boolean
        get() = draftAttachments.none { it.status is ChatAttachmentStatus.Uploading || it.status.isRejected }

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
