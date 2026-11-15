package com.example.hopes.feature.community.presentation.write

import com.example.hopes.feature.community.model.COMMUNITY_COHORTS

/**
 * 글쓰기·글 수정 화면 상태다(피그마 15 ~ 15-7).
 *
 * @param editingPostId 수정 중인 글이다. null이면 새 글이다.
 * @param cohort 글을 올릴 게시판이다. null이면 "전체"다.
 * @param isSubmitFailed 등록·저장에 실패했으면 true다. 위에 빨간 안내를 띄운다(피그마 15-7).
 */
data class CommunityWriteUiState(
    val editingPostId: Long? = null,
    val cohorts: List<Int> = COMMUNITY_COHORTS,
    val cohort: Int? = null,
    val title: String = "",
    val content: String = "",
    val isTitleError: Boolean = false,
    val isContentError: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSubmitFailed: Boolean = false,
    val isBoardSheetVisible: Boolean = false,
    val isExitDialogVisible: Boolean = false,
    val result: CommunityWriteResult? = null,
) {
    val isEditMode: Boolean get() = editingPostId != null
}

/** 화면을 닫을 이유다. */
enum class CommunityWriteResult {
    Created,
    Updated,
    Closed,
}

sealed interface CommunityWriteEvent {
    data object CloseClicked : CommunityWriteEvent

    data object ExitConfirmed : CommunityWriteEvent

    data object ExitDismissed : CommunityWriteEvent

    data object BoardClicked : CommunityWriteEvent

    data class BoardSelected(
        val cohort: Int?,
    ) : CommunityWriteEvent

    data object BoardSheetDismissed : CommunityWriteEvent

    data class TitleChanged(
        val title: String,
    ) : CommunityWriteEvent

    data class ContentChanged(
        val content: String,
    ) : CommunityWriteEvent

    data object SubmitClicked : CommunityWriteEvent
}
