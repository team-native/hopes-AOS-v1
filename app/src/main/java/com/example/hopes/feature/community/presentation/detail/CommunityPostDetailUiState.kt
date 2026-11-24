package com.example.hopes.feature.community.presentation.detail

import com.example.hopes.feature.community.data.CommunityReportTarget
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityPost
import java.time.LocalDateTime

/**
 * @param post 글이다. 불러온 뒤에도 null이면 삭제된 글로 보고 안내를 띄운다(피그마 14-10).
 * @param failedComments 등록하지 못한 내 댓글이다. 빨간 "전송 실패 · 다시 시도"로 보여준다(피그마 14-9).
 * @param editingCommentId 수정 중인 댓글이다. 입력창 위에 "댓글 수정 중"을 띄운다(피그마 14-8).
 * @param isPostDeleted 내가 글을 지웠으면 true다. 화면을 닫는다.
 */
data class CommunityPostDetailUiState(
    val isLoading: Boolean = true,
    val isLoadError: Boolean = false,
    val post: CommunityPost? = null,
    val comments: List<CommunityComment> = emptyList(),
    val failedComments: List<CommunityFailedComment> = emptyList(),
    val commentInput: String = "",
    val editingCommentId: Long? = null,
    val isSendingComment: Boolean = false,
    val sheet: CommunityDetailSheet? = null,
    val dialog: CommunityDetailDialog? = null,
    val toast: CommunityDetailToast? = null,
    val isPostDeleted: Boolean = false,
) {
    val commentCount: Int get() = comments.size + failedComments.size
}

/** 등록하지 못한 댓글이다. [localId]는 다시 시도할 때 찾는 화면 안 번호다. */
data class CommunityFailedComment(
    val localId: Long,
    val content: String,
    val createdAt: LocalDateTime,
)

/** 지금 띄운 시트다. */
sealed interface CommunityDetailSheet {
    /** 글 "…"(피그마 14-2 내 글, 14-3 다른 사람 글) */
    data object PostMore : CommunityDetailSheet

    /** 댓글 "…"(피그마 14-7 내 댓글). 다른 사람 댓글이면 신고만 보여준다. */
    data class CommentMore(
        val comment: CommunityComment,
    ) : CommunityDetailSheet

    /** 신고 사유 선택(피그마 17) */
    data class Report(
        val target: CommunityReportTarget,
    ) : CommunityDetailSheet

    /** 신고 완료(피그마 17-2) */
    data object ReportDone : CommunityDetailSheet
}

sealed interface CommunityDetailDialog {
    /** 피그마 14-4 */
    data object DeletePost : CommunityDetailDialog

    data class DeleteComment(
        val commentId: Long,
    ) : CommunityDetailDialog
}

enum class CommunityDetailToast {
    /** 피그마 17-3 */
    AlreadyReportedPost,
    AlreadyReportedComment,
    CommentEditFailed,
    ReportFailed,
}
