package com.example.hopes.feature.community.presentation.detail

import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityReportReason

sealed interface CommunityPostDetailEvent {
    data object BackClicked : CommunityPostDetailEvent

    data object LikeClicked : CommunityPostDetailEvent

    data object PostMoreClicked : CommunityPostDetailEvent

    data class CommentMoreClicked(
        val comment: CommunityComment,
    ) : CommunityPostDetailEvent

    data object SheetDismissed : CommunityPostDetailEvent

    /** 내 글 더보기의 "수정하기"다. 글쓰기 화면을 수정 모드로 연다. */
    data object EditPostClicked : CommunityPostDetailEvent

    data object DeletePostClicked : CommunityPostDetailEvent

    data object ReportPostClicked : CommunityPostDetailEvent

    data class EditCommentClicked(
        val comment: CommunityComment,
    ) : CommunityPostDetailEvent

    data class DeleteCommentClicked(
        val commentId: Long,
    ) : CommunityPostDetailEvent

    data class ReportCommentClicked(
        val commentId: Long,
    ) : CommunityPostDetailEvent

    data class ReportSubmitted(
        val reason: CommunityReportReason,
    ) : CommunityPostDetailEvent

    data object DialogDismissed : CommunityPostDetailEvent

    data object DeletePostConfirmed : CommunityPostDetailEvent

    data object DeleteCommentConfirmed : CommunityPostDetailEvent

    data class CommentInputChanged(
        val text: String,
    ) : CommunityPostDetailEvent

    data object CommentSendClicked : CommunityPostDetailEvent

    data object CommentEditCancelled : CommunityPostDetailEvent

    data class FailedCommentRetryClicked(
        val localId: Long,
    ) : CommunityPostDetailEvent

    data object ToastDismissed : CommunityPostDetailEvent

    /** 삭제된 글 안내의 "목록으로"다. */
    data object BackToListClicked : CommunityPostDetailEvent
}
