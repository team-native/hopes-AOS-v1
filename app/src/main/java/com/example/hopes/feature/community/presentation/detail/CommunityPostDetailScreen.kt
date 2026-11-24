package com.example.hopes.feature.community.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesActionSheet
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.core.designsystem.component.HopesSheetAction
import com.example.hopes.core.designsystem.component.HopesStatusMessage
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesToast
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.presentation.component.CommunityReportDoneSheet
import com.example.hopes.feature.community.presentation.component.CommunityReportSheet
import com.example.hopes.feature.community.presentation.component.communityBoardName
import com.example.hopes.feature.community.presentation.detail.component.CommunityCommentInput
import com.example.hopes.feature.community.presentation.detail.component.CommunityCommentList
import com.example.hopes.feature.community.presentation.detail.component.CommunityPostBody
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R
import java.time.LocalDateTime

/**
 * 게시글 상세다(피그마 14 ~ 14-10, 17 ~ 17-3). 본문, 댓글, 아래 댓글 입력줄과 더보기·신고 시트, 삭제 확인을 그린다.
 *
 * @param myCohort 등록 실패한 내 댓글의 작성자 표시에 쓴다.
 */
@Composable
fun CommunityPostDetailScreen(
    uiState: CommunityPostDetailUiState,
    myCohort: Int,
    onEvent: (CommunityPostDetailEvent) -> Unit,
) {
    val post = uiState.post
    val now = remember(post, uiState.comments) { LocalDateTime.now() }

    HopesSubScreenFrame(
        title = stringResource(R.string.community_post_title),
        subtitle = post?.let { stringResource(R.string.community_board_title, communityBoardName(it.cohort)) },
        onBackClick = { onEvent(CommunityPostDetailEvent.BackClicked) },
        bottomButton =
            if (post != null) {
                {
                    CommunityCommentInput(
                        value = uiState.commentInput,
                        onValueChange = { onEvent(CommunityPostDetailEvent.CommentInputChanged(it)) },
                        onSendClick = { onEvent(CommunityPostDetailEvent.CommentSendClicked) },
                        isEditing = uiState.editingCommentId != null,
                        onEditCancel = { onEvent(CommunityPostDetailEvent.CommentEditCancelled) },
                        isSending = uiState.isSendingComment,
                    )
                }
            } else {
                null
            },
        toast =
            uiState.toast?.let { toast ->
                {
                    HopesToast(
                        message = toast.message(),
                        onDismiss = { onEvent(CommunityPostDetailEvent.ToastDismissed) },
                    )
                }
            },
    ) {
        when {
            uiState.isLoading -> {
                Unit
            }

            uiState.isLoadError -> {
                HopesStatusMessage(
                    icon = Icons.Outlined.Delete,
                    title = stringResource(R.string.community_load_failed),
                    description = stringResource(R.string.community_load_failed_description),
                    actionText = stringResource(R.string.community_retry),
                    onActionClick = { onEvent(CommunityPostDetailEvent.RetryLoadClicked) },
                )
            }

            post == null -> {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = DELETED_MESSAGE_TOP_PADDING),
                    contentAlignment = Alignment.Center,
                ) {
                    HopesStatusMessage(
                        icon = Icons.Outlined.Delete,
                        title = stringResource(R.string.community_post_deleted_title),
                        description = stringResource(R.string.community_post_deleted_description),
                        actionText = stringResource(R.string.community_back_to_list),
                        onActionClick = { onEvent(CommunityPostDetailEvent.BackToListClicked) },
                    )
                }
            }

            else -> {
                CommunityPostBody(
                    post = post,
                    commentCount = uiState.commentCount,
                    now = now,
                    onLikeClick = { onEvent(CommunityPostDetailEvent.LikeClicked) },
                    onMoreClick = { onEvent(CommunityPostDetailEvent.PostMoreClicked) },
                )

                // 피그마 14: 본문 카드·"댓글 3"·댓글 카드 사이 간격은 모두 10이다.
                Spacer(modifier = Modifier.height(DETAIL_GAP))

                Text(
                    text = stringResource(R.string.community_comment_header, uiState.commentCount),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Label,
                )

                if (uiState.commentCount > 0) {
                    Spacer(modifier = Modifier.height(DETAIL_GAP))

                    CommunityCommentList(
                        comments = uiState.comments,
                        failedComments = uiState.failedComments,
                        myCohort = myCohort,
                        now = now,
                        onMoreClick = { onEvent(CommunityPostDetailEvent.CommentMoreClicked(it)) },
                        onRetryClick = { onEvent(CommunityPostDetailEvent.FailedCommentRetryClicked(it)) },
                    )
                }
            }
        }
    }

    CommunityDetailSheets(uiState = uiState, onEvent = onEvent)
    CommunityDetailDialogs(dialog = uiState.dialog, onEvent = onEvent)
}

private val DETAIL_GAP = 10.dp

@Composable
private fun CommunityDetailSheets(
    uiState: CommunityPostDetailUiState,
    onEvent: (CommunityPostDetailEvent) -> Unit,
) {
    val dismiss = { onEvent(CommunityPostDetailEvent.SheetDismissed) }

    when (val sheet = uiState.sheet) {
        null -> {
            Unit
        }

        CommunityDetailSheet.PostMore -> {
            HopesActionSheet(
                actions =
                    if (uiState.post?.isMine == true) {
                        listOf(
                            HopesSheetAction(
                                text = stringResource(R.string.community_post_edit),
                                icon = Icons.Outlined.Edit,
                                onClick = { onEvent(CommunityPostDetailEvent.EditPostClicked) },
                            ),
                            HopesSheetAction(
                                text = stringResource(R.string.community_post_delete),
                                icon = Icons.Outlined.Delete,
                                onClick = { onEvent(CommunityPostDetailEvent.DeletePostClicked) },
                                isDestructive = true,
                            ),
                        )
                    } else {
                        listOf(reportAction { onEvent(CommunityPostDetailEvent.ReportPostClicked) })
                    },
                onDismiss = dismiss,
            )
        }

        is CommunityDetailSheet.CommentMore -> {
            val comment = sheet.comment
            HopesActionSheet(
                actions =
                    if (comment.isMine) {
                        listOf(
                            HopesSheetAction(
                                text = stringResource(R.string.community_comment_edit),
                                icon = Icons.Outlined.Edit,
                                onClick = { onEvent(CommunityPostDetailEvent.EditCommentClicked(comment)) },
                            ),
                            HopesSheetAction(
                                text = stringResource(R.string.community_comment_delete),
                                icon = Icons.Outlined.Delete,
                                onClick = { onEvent(CommunityPostDetailEvent.DeleteCommentClicked(comment.id)) },
                                isDestructive = true,
                            ),
                        )
                    } else {
                        listOf(reportAction { onEvent(CommunityPostDetailEvent.ReportCommentClicked(comment.id)) })
                    },
                onDismiss = dismiss,
            )
        }

        is CommunityDetailSheet.Report -> {
            CommunityReportSheet(
                onReport = { reason -> onEvent(CommunityPostDetailEvent.ReportSubmitted(reason)) },
                onDismiss = dismiss,
            )
        }

        CommunityDetailSheet.ReportDone -> {
            CommunityReportDoneSheet(onDismiss = dismiss)
        }
    }
}

@Composable
private fun reportAction(onClick: () -> Unit): HopesSheetAction =
    HopesSheetAction(
        text = stringResource(R.string.community_report),
        icon = Icons.Outlined.Flag,
        onClick = onClick,
        isDestructive = true,
    )

@Composable
private fun CommunityDetailDialogs(
    dialog: CommunityDetailDialog?,
    onEvent: (CommunityPostDetailEvent) -> Unit,
) {
    when (dialog) {
        null -> {
            Unit
        }

        CommunityDetailDialog.DeletePost -> {
            HopesAlertDialog(
                title = stringResource(R.string.community_post_delete_title),
                message = stringResource(R.string.community_post_delete_message),
                dismissText = stringResource(R.string.cancel),
                confirmText = stringResource(R.string.community_delete),
                onDismiss = { onEvent(CommunityPostDetailEvent.DialogDismissed) },
                onConfirm = { onEvent(CommunityPostDetailEvent.DeletePostConfirmed) },
                isConfirmDestructive = true,
            )
        }

        is CommunityDetailDialog.DeleteComment -> {
            HopesAlertDialog(
                title = stringResource(R.string.community_comment_delete_title),
                message = stringResource(R.string.community_comment_delete_message),
                dismissText = stringResource(R.string.cancel),
                confirmText = stringResource(R.string.community_delete),
                onDismiss = { onEvent(CommunityPostDetailEvent.DialogDismissed) },
                onConfirm = { onEvent(CommunityPostDetailEvent.DeleteCommentConfirmed) },
                isConfirmDestructive = true,
            )
        }
    }
}

@Composable
private fun CommunityDetailToast.message(): String =
    stringResource(
        when (this) {
            CommunityDetailToast.AlreadyReportedPost -> R.string.community_report_already_post
            CommunityDetailToast.AlreadyReportedComment -> R.string.community_report_already_comment
            CommunityDetailToast.CommentEditFailed -> R.string.community_comment_edit_failed
            CommunityDetailToast.ReportFailed -> R.string.community_report_failed
        },
    )

// 삭제된 글 안내(피그마 14-10)를 화면 가운데쯤에 두기 위한 위쪽 여백이다. 본문이 세로 스크롤이라 가운데 정렬을 쓸 수 없다.
private val DELETED_MESSAGE_TOP_PADDING = 160.dp

@Composable
private fun CommunityPostDetailPreview(uiState: CommunityPostDetailUiState) {
    HopesTheme {
        CommunityPostDetailScreen(uiState = uiState, myCohort = 10, onEvent = {})
    }
}

private fun previewDetailState(): CommunityPostDetailUiState {
    val now = LocalDateTime.now()
    return CommunityPostDetailUiState(
        isLoading = false,
        post =
            CommunityPost(
                id = 1,
                cohort = 10,
                authorCohort = 10,
                title = "기숙사 와이파이 다들 잘 돼?",
                content =
                    "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서 과제 제출이 힘들어. " +
                        "다른 층은 어떤지, 해결 방법 아는 사람 있으면 알려줘!",
                createdAt = now.minusMinutes(10),
                likeCount = 12,
            ),
        comments =
            listOf(
                CommunityComment(1, 1, 10, "2층도 저녁엔 느려. 공유기 위치 문제 같아.", now.minusMinutes(5)),
                CommunityComment(2, 1, 9, "사감 선생님께 말씀드리면 점검해 주신대!", now.minusMinutes(3)),
                CommunityComment(3, 1, 10, "오 고마워, 내일 말씀드려 볼게.", now, isMine = true),
            ),
    )
}

@Preview(showBackground = true, name = "14 게시글 상세")
@Composable
private fun CommunityPostDetailContentPreview() {
    CommunityPostDetailPreview(previewDetailState())
}

@Preview(showBackground = true, name = "14-5 좋아요 누름")
@Composable
private fun CommunityPostDetailLikedPreview() {
    val state = previewDetailState()
    CommunityPostDetailPreview(state.copy(post = state.post?.copy(isLiked = true, likeCount = 13)))
}

@Preview(showBackground = true, name = "14-8 댓글 수정 중")
@Composable
private fun CommunityPostDetailEditingPreview() {
    CommunityPostDetailPreview(
        previewDetailState().copy(editingCommentId = 3, commentInput = "오 고마워, 내일 아침에 말씀드려 볼게."),
    )
}

@Preview(showBackground = true, name = "14-9 댓글 등록 실패")
@Composable
private fun CommunityPostDetailCommentFailedPreview() {
    CommunityPostDetailPreview(
        previewDetailState().copy(
            failedComments = listOf(CommunityFailedComment(1, "3층 공용 공유기 쪽은 괜찮더라", LocalDateTime.now())),
        ),
    )
}

@Preview(showBackground = true, name = "14-10 삭제된 게시글")
@Composable
private fun CommunityPostDetailDeletedPreview() {
    CommunityPostDetailPreview(CommunityPostDetailUiState(isLoading = false))
}
