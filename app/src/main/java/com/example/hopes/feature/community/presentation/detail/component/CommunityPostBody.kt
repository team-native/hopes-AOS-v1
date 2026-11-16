package com.example.hopes.feature.community.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
import com.example.hopes.ui.theme.LocalHopesExtendedColors
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
    val cardShape = RoundedCornerShape(AppRadius.Panel)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                .padding(AppSpacing.Medium),
        // 피그마 Post: 안쪽 여백 16, 요소 간격 8.
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunityBoardTag(cohort = post.cohort)

            Spacer(modifier = Modifier.weight(1f))

            CommunityMoreButton(onClick = onMoreClick)
        }

        Text(
            text = post.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Heading,
        )

        CommunityMetaText(
            author = communityAuthorName(post.authorCohort, post.isMine),
            isMine = post.isMine,
            time = communityTimeText(post.createdAt, now),
            isEdited = post.isEdited,
        )

        Text(
            text = post.content,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Body,
        )

        // 피그마 Actions: 위 여백 4, 칩 간격 12.
        Row(modifier = Modifier.padding(top = AppSpacing.Small), verticalAlignment = Alignment.CenterVertically) {
            CommunityLikeChip(
                count = post.likeCount,
                isLiked = post.isLiked,
                onClick = onLikeClick,
            )

            Spacer(modifier = Modifier.width(AppSpacing.Item))

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
    val hintColor = LocalHopesExtendedColors.current.authFieldHint
    val authorColor = if (isMine) MaterialTheme.colorScheme.primary else hintColor
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
        color = hintColor,
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
                // 피그마 Like: 가로 12·세로 6, 아이콘 16, 간격 6.
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
            painter =
                if (isLiked) {
                    rememberVectorPainter(Icons.Filled.Favorite)
                } else {
                    painterResource(R.drawable.ic_heart)
                },
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(COUNT_ICON_GAP))

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

    val chipShape = RoundedCornerShape(COMMENT_CHIP_RADIUS)

    // 피그마 Comments: 흰 배경에 그림자, 가로 17·세로 14, 반경 20.
    Row(
        modifier =
            Modifier
                .figmaSubtleShadow(chipShape)
                .background(MaterialTheme.colorScheme.surface, chipShape)
                .clearAndSetSemantics { contentDescription = description }
                .padding(horizontal = COMMENT_CHIP_PADDING_HORIZONTAL, vertical = COMMENT_CHIP_PADDING_VERTICAL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_comment),
            contentDescription = null,
            tint = LocalHopesExtendedColors.current.authFieldHint,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(COUNT_ICON_GAP))

        Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.LabelStrong,
        )
    }
}

private val COUNT_ICON_GAP = 6.dp
private val COMMENT_CHIP_RADIUS = 20.dp
private val COMMENT_CHIP_PADDING_HORIZONTAL = 17.dp
private val COMMENT_CHIP_PADDING_VERTICAL = 14.dp
