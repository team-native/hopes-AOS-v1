package com.example.hopes.feature.community.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.presentation.component.CommunityBoardTag
import com.example.hopes.feature.community.presentation.component.CommunityMoreButton
import com.example.hopes.feature.community.presentation.component.communityAuthorName
import com.example.hopes.feature.community.presentation.component.communityTimeText
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R
import java.time.LocalDateTime

/** 게시글 상세의 본문 카드다(피그마 14). 태그, 제목, 작성자·시간, 본문, 좋아요·댓글 수를 보여준다. */
@Composable
fun CommunityPostBody(
    post: CommunityPost,
    commentCount: Int,
    now: LocalDateTime,
    onLikeClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .padding(AppSpacing.Medium + AppSpacing.Small),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunityBoardTag(cohort = post.cohort)

            Spacer(modifier = Modifier.weight(1f))

            CommunityMoreButton(onClick = onMoreClick)
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Text(
            text = post.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.SheetTitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        CommunityMetaText(
            author = communityAuthorName(post.authorCohort, post.isMine),
            isMine = post.isMine,
            time = communityTimeText(post.createdAt, now),
            isEdited = post.isEdited,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Text(
            text = post.content,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Description,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunityLikeChip(
                count = post.likeCount,
                isLiked = post.isLiked,
                onClick = onLikeClick,
            )

            Spacer(modifier = Modifier.width(AppSpacing.Compact))

            CommunityCommentChip(count = commentCount)
        }
    }
}

/** "10기 · 10분 전" 줄이다. 내 글·댓글이면 작성자를 파란 글자로 그리고, 수정했으면 "· 수정됨"을 붙인다. */
@Composable
fun CommunityMetaText(
    author: String,
    isMine: Boolean,
    time: String,
    modifier: Modifier = Modifier,
    isEdited: Boolean = false,
) {
    val edited = stringResource(R.string.community_edited)
    val authorColor = if (isMine) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val text =
        buildAnnotatedString {
            withStyle(SpanStyle(color = authorColor)) { append(author) }
            append(" · ")
            append(time)
            if (isEdited) {
                append(" · ")
                append(edited)
            }
        }

    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = HopesTextStyles.Caption,
    )
}

/** 좋아요 칩이다. 누르면 파란 배경에 채운 하트로 바뀐다(피그마 14-5). */
@Composable
private fun CommunityLikeChip(
    count: Int,
    isLiked: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (isLiked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
    val description = stringResource(R.string.community_like_count, count)

    Row(
        modifier =
            Modifier
                .height(AppSize.Chip)
                .clip(CircleShape)
                .background(
                    if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                ).toggleable(value = isLiked, role = Role.Checkbox, onValueChange = { onClick() })
                .semantics { contentDescription = description }
                .padding(horizontal = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = count.toString(),
            color = contentColor,
            style = HopesTextStyles.LabelStrong,
        )
    }
}

@Composable
private fun CommunityCommentChip(count: Int) {
    val description = stringResource(R.string.community_comment_count, count)

    Row(
        modifier =
            Modifier
                .height(AppSize.Chip)
                .border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, CircleShape)
                .clearAndSetSemantics { contentDescription = description }
                .padding(horizontal = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Chat,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.LabelStrong,
        )
    }
}
