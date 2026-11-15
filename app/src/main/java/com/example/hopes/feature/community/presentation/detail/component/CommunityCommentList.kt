package com.example.hopes.feature.community.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.presentation.component.CommunityMoreButton
import com.example.hopes.feature.community.presentation.component.communityAuthorName
import com.example.hopes.feature.community.presentation.component.communityTimeText
import com.example.hopes.feature.community.presentation.detail.CommunityFailedComment
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R
import java.time.LocalDateTime

/**
 * 댓글 묶음 카드다(피그마 14). 등록하지 못한 내 댓글은 맨 아래에 빨간 "전송 실패 · 다시 시도"로 붙는다(피그마 14-9).
 *
 * @param myCohort 등록 실패한 댓글의 작성자 표시("10기 (나)")에 쓴다.
 */
@Composable
fun CommunityCommentList(
    comments: List<CommunityComment>,
    failedComments: List<CommunityFailedComment>,
    myCohort: Int,
    now: LocalDateTime,
    onMoreClick: (CommunityComment) -> Unit,
    onRetryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .padding(horizontal = AppSpacing.Medium + AppSpacing.Small, vertical = AppSpacing.Compact),
    ) {
        comments.forEachIndexed { index, comment ->
            if (index > 0) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            }

            CommunityCommentRow(
                comment = comment,
                now = now,
                onMoreClick = { onMoreClick(comment) },
            )
        }

        failedComments.forEachIndexed { index, failed ->
            if (comments.isNotEmpty() || index > 0) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            }

            CommunityFailedCommentRow(
                failed = failed,
                myCohort = myCohort,
                onRetryClick = { onRetryClick(failed.localId) },
            )
        }
    }
}

@Composable
private fun CommunityCommentRow(
    comment: CommunityComment,
    now: LocalDateTime,
    onMoreClick: () -> Unit,
) {
    Column(modifier = Modifier.padding(vertical = AppSpacing.Compact)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunityMetaText(
                author = communityAuthorName(comment.authorCohort, comment.isMine),
                isMine = comment.isMine,
                time = communityTimeText(comment.createdAt, now),
                modifier = Modifier.weight(1f),
                isEdited = comment.isEdited,
            )

            CommunityMoreButton(onClick = onMoreClick)
        }

        Text(
            text = comment.content,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.BodyStrong,
        )
    }
}

@Composable
private fun CommunityFailedCommentRow(
    failed: CommunityFailedComment,
    myCohort: Int,
    onRetryClick: () -> Unit,
) {
    val errorColor = MaterialTheme.colorScheme.error
    val retry = stringResource(R.string.retry)

    Column(
        modifier =
            Modifier
                .padding(vertical = AppSpacing.Compact + AppSpacing.Small)
                .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = communityAuthorName(myCohort, isMine = true),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.Caption,
            )

            Text(
                text = " · " + stringResource(R.string.community_comment_failed) + " · ",
                color = errorColor,
                style = HopesTextStyles.Caption,
            )

            Text(
                text = retry,
                modifier = Modifier.clickable(role = Role.Button, onClick = onRetryClick),
                color = errorColor,
                style = HopesTextStyles.CaptionStrong,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.XSmall))

        Text(
            text = failed.content,
            color = errorColor,
            style = HopesTextStyles.BodyStrong,
        )
    }
}
