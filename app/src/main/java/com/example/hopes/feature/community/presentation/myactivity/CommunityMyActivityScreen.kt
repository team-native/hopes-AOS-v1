package com.example.hopes.feature.community.presentation.myactivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesActionSheet
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.core.designsystem.component.HopesSheetAction
import com.example.hopes.core.designsystem.component.HopesStatusMessage
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityMyComment
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.presentation.component.CommunityBoardTag
import com.example.hopes.feature.community.presentation.component.CommunityFilterChip
import com.example.hopes.feature.community.presentation.component.CommunityPostCard
import com.example.hopes.feature.community.presentation.component.communityTimeText
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R
import java.time.LocalDateTime

/**
 * 마이페이지·커뮤니티에서 여는 "내 활동" 화면이다(피그마 18, 18-2, 18-3).
 * 작성글·댓글 탭으로 내가 쓴 글과 댓글을 보여주고, 내 글은 "…"에서 수정·삭제할 수 있다.
 */
@Composable
fun CommunityMyActivityScreen(
    uiState: CommunityMyActivityUiState,
    onBackClick: () -> Unit,
    onTabSelect: (CommunityMyActivityTab) -> Unit,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit = {},
    onPostMoreClick: (Long) -> Unit,
    onPostMoreDismiss: () -> Unit,
    onEditPostClick: (Long) -> Unit,
    onDeletePostClick: (Long) -> Unit,
    onDeletePostConfirm: () -> Unit,
    onDeletePostDismiss: () -> Unit,
    onWriteClick: () -> Unit,
    onRetryClick: () -> Unit = {},
) {
    val now = remember(uiState.posts, uiState.comments) { LocalDateTime.now() }

    HopesSubScreenFrame(
        title = stringResource(R.string.community_my_activity),
        subtitle = stringResource(R.string.community_my_activity_description),
        onBackClick = onBackClick,
    ) {
        Row(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
        ) {
            CommunityFilterChip(
                text = stringResource(R.string.community_my_posts_tab, uiState.posts.size),
                isSelected = uiState.selectedTab == CommunityMyActivityTab.Posts,
                onClick = { onTabSelect(CommunityMyActivityTab.Posts) },
            )

            CommunityFilterChip(
                text = stringResource(R.string.community_my_comments_tab, uiState.comments.size),
                isSelected = uiState.selectedTab == CommunityMyActivityTab.Comments,
                onClick = { onTabSelect(CommunityMyActivityTab.Comments) },
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        when (uiState.selectedTab) {
            CommunityMyActivityTab.Posts -> {
                if (uiState.isLoading) {
                    androidx.compose.material3.CircularProgressIndicator()
                } else if (uiState.postsLoadError) {
                    HopesStatusMessage(
                        icon = Icons.Outlined.Refresh,
                        title = stringResource(R.string.community_load_error_title),
                        description = stringResource(R.string.community_load_error_description),
                        actionText = stringResource(R.string.community_retry),
                        onActionClick = onRetryClick,
                    )
                } else if (uiState.posts.isEmpty()) {
                    CommunityMyActivityEmpty(
                        title = stringResource(R.string.community_my_posts_empty_title),
                        description = stringResource(R.string.community_my_posts_empty_description),
                        actionText = stringResource(R.string.community_write),
                        onActionClick = onWriteClick,
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
                        uiState.posts.forEach { post ->
                            CommunityPostCard(
                                post = post,
                                now = now,
                                onClick = { onPostClick(post.id) },
                                onLikeClick = { onLikeClick(post.id) },
                                onMoreClick = { onPostMoreClick(post.id) },
                            )
                        }
                    }
                }
            }

            CommunityMyActivityTab.Comments -> {
                if (uiState.isLoading) {
                    androidx.compose.material3.CircularProgressIndicator()
                } else if (uiState.commentsLoadError) {
                    HopesStatusMessage(
                        icon = Icons.Outlined.Refresh,
                        title = stringResource(R.string.community_load_error_title),
                        description = stringResource(R.string.community_load_error_description),
                        actionText = stringResource(R.string.community_retry),
                        onActionClick = onRetryClick,
                    )
                } else if (uiState.comments.isEmpty()) {
                    CommunityMyActivityEmpty(
                        title = stringResource(R.string.community_my_comments_empty_title),
                        description = stringResource(R.string.community_my_comments_empty_description),
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
                        uiState.comments.forEach { myComment ->
                            CommunityMyCommentCard(
                                myComment = myComment,
                                now = now,
                                onClick = { onPostClick(myComment.comment.postId) },
                            )
                        }
                    }
                }
            }
        }
    }

    val morePostId = uiState.morePostId
    if (morePostId != null) {
        HopesActionSheet(
            actions =
                listOf(
                    HopesSheetAction(
                        text = stringResource(R.string.community_post_edit),
                        icon = Icons.Outlined.Edit,
                        onClick = { onEditPostClick(morePostId) },
                    ),
                    HopesSheetAction(
                        text = stringResource(R.string.community_post_delete),
                        icon = Icons.Outlined.Delete,
                        onClick = { onDeletePostClick(morePostId) },
                        isDestructive = true,
                    ),
                ),
            onDismiss = onPostMoreDismiss,
        )
    }

    if (uiState.pendingDeletePostId != null) {
        HopesAlertDialog(
            title = stringResource(R.string.community_post_delete_title),
            message = stringResource(R.string.community_post_delete_message),
            dismissText = stringResource(R.string.cancel),
            confirmText = stringResource(R.string.community_delete),
            onDismiss = onDeletePostDismiss,
            onConfirm = onDeletePostConfirm,
            isConfirmDestructive = true,
        )
    }
}

/** 내 댓글 한 장이다(피그마 18-2). 댓글이 달린 글의 게시판·제목, 댓글 내용, 시간을 보여준다. */
@Composable
private fun CommunityMyCommentCard(
    myComment: CommunityMyComment,
    now: LocalDateTime,
    onClick: () -> Unit,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .shapeClickable(shape = cardShape, role = Role.Button, onClick = onClick)
                .padding(AppSpacing.Medium),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunityBoardTag(cohort = myComment.postCohort)

            Spacer(modifier = Modifier.width(AppSpacing.Compact))

            Text(
                text = myComment.postTitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = HopesTextStyles.Caption,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Text(
            text = myComment.comment.content,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = HopesTextStyles.BodyStrong,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = communityTimeText(myComment.comment.createdAt, now),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Caption,
        )
    }
}

@Composable
private fun CommunityMyActivityEmpty(
    title: String,
    description: String,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = EMPTY_MESSAGE_TOP_PADDING),
        contentAlignment = Alignment.Center,
    ) {
        HopesStatusMessage(
            icon = if (actionText != null) Icons.Outlined.Edit else Icons.AutoMirrored.Outlined.Chat,
            title = title,
            description = description,
            actionText = actionText,
            onActionClick = onActionClick,
        )
    }
}

// 빈 안내를 화면 가운데쯤에 두기 위한 위쪽 여백이다. 본문이 세로 스크롤이라 가운데 정렬을 쓸 수 없다.
private val EMPTY_MESSAGE_TOP_PADDING = 120.dp

@Composable
private fun CommunityMyActivityPreview(uiState: CommunityMyActivityUiState) {
    HopesTheme {
        CommunityMyActivityScreen(
            uiState = uiState,
            onBackClick = {},
            onTabSelect = {},
            onPostClick = {},
            onPostMoreClick = {},
            onPostMoreDismiss = {},
            onEditPostClick = {},
            onDeletePostClick = {},
            onDeletePostConfirm = {},
            onDeletePostDismiss = {},
            onWriteClick = {},
        )
    }
}

private fun previewMyActivityState(): CommunityMyActivityUiState {
    val now = LocalDateTime.now()
    return CommunityMyActivityUiState(
        posts =
            listOf(
                CommunityPost(
                    id = 1,
                    cohort = 10,
                    authorCohort = 10,
                    title = "기숙사 와이파이 다들 잘 돼?",
                    content = "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서… 다른 층은 어떤지 궁금해.",
                    createdAt = now.minusMinutes(10),
                    likeCount = 12,
                    commentCount = 5,
                    isMine = true,
                ),
            ),
        comments =
            listOf(
                CommunityMyComment(
                    comment = CommunityComment(1, 1, 10, "오 고마워, 내일 말씀드려 볼게.", now, isMine = true),
                    postCohort = 10,
                    postTitle = "기숙사 와이파이 다들 잘 돼?",
                ),
            ),
    )
}

@Preview(showBackground = true, name = "18 내 활동 작성글")
@Composable
private fun CommunityMyActivityPostsPreview() {
    CommunityMyActivityPreview(previewMyActivityState())
}

@Preview(showBackground = true, name = "18-2 내 활동 댓글")
@Composable
private fun CommunityMyActivityCommentsPreview() {
    CommunityMyActivityPreview(previewMyActivityState().copy(selectedTab = CommunityMyActivityTab.Comments))
}

@Preview(showBackground = true, name = "18-3 내 활동 빈 상태")
@Composable
private fun CommunityMyActivityEmptyPreview() {
    CommunityMyActivityPreview(CommunityMyActivityUiState())
}
