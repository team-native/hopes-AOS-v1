package com.example.hopes.feature.community.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSkeleton
import com.example.hopes.core.designsystem.component.HopesSkeletonLine
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R
import java.time.LocalDateTime

/**
 * 게시글 목록의 카드 한 장이다(피그마 13, 16, 18). 게시판 태그, 제목, 본문 두 줄, 작성자·시간, 좋아요·댓글 수를 보여준다.
 *
 * @param onMoreClick 있으면 오른쪽 위에 "…" 버튼을 붙인다(내 활동).
 */
@Composable
fun CommunityPostCard(
    post: CommunityPost,
    now: LocalDateTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onMoreClick: (() -> Unit)? = null,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .shapeClickable(shape = cardShape, role = Role.Button, onClick = onClick)
                .padding(AppSpacing.Medium),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CommunityBoardTag(cohort = post.cohort)

            Spacer(modifier = Modifier.weight(1f))

            if (onMoreClick != null) {
                CommunityMoreButton(onClick = onMoreClick)
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Text(
            text = post.title,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = HopesTextStyles.FieldStrong,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = post.content,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = HopesTextStyles.Subtitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text =
                    stringResource(
                        R.string.community_meta,
                        communityAuthorName(post.authorCohort, post.isMine),
                        communityTimeText(post.createdAt, now),
                    ),
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )

            CommunityCount(
                icon = Icons.Outlined.FavoriteBorder,
                count = post.likeCount,
                description = stringResource(R.string.community_like_count, post.likeCount),
            )

            Spacer(modifier = Modifier.width(AppSpacing.Compact))

            CommunityCount(
                icon = Icons.AutoMirrored.Outlined.Chat,
                count = post.commentCount,
                description = stringResource(R.string.community_comment_count, post.commentCount),
            )
        }
    }
}

@Composable
private fun CommunityCount(
    icon: ImageVector,
    count: Int,
    description: String,
) {
    Row(
        modifier = Modifier.clearAndSetSemantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Inline),
        )

        Spacer(modifier = Modifier.width(AppSpacing.XSmall))

        Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Caption,
        )
    }
}

/** 글·댓글 오른쪽의 회색 "…" 버튼이다. */
@Composable
fun CommunityMoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.community_more)

    Box(
        modifier =
            modifier
                .size(MORE_BUTTON_SIZE)
                .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onClick)
                .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.MoreHoriz,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Small),
        )
    }
}

/** 글 목록을 불러오는 동안 카드 세 장 자리에 놓는 회색 자리 표시다(피그마 13-2). */
@Composable
fun CommunityPostListSkeleton(modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        repeat(SKELETON_CARD_COUNT) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, cardShape)
                        .padding(AppSpacing.Medium),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
            ) {
                HopesSkeleton(
                    modifier = Modifier.size(width = SKELETON_TAG_WIDTH, height = AppSpacing.Medium),
                )
                HopesSkeletonLine(modifier = Modifier.fillMaxWidth(SKELETON_TITLE_FRACTION))
                HopesSkeletonLine(modifier = Modifier.fillMaxWidth())
                HopesSkeletonLine(modifier = Modifier.fillMaxWidth(SKELETON_META_FRACTION))
            }
        }
    }
}

/** 오른쪽 아래에 떠 있는 파란 원형 글쓰기 버튼이다(피그마 13). */
@Composable
fun CommunityWriteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.community_write)

    Box(
        modifier =
            modifier
                .size(AppSize.Field + AppSpacing.Compact)
                .figmaRaisedShadow(CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onClick)
                .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(AppIconSize.Large),
        )
    }
}

private val MORE_BUTTON_SIZE = 28.dp
private val SKELETON_TAG_WIDTH = 36.dp
private const val SKELETON_CARD_COUNT = 3
private const val SKELETON_TITLE_FRACTION = 0.5f
private const val SKELETON_META_FRACTION = 0.7f

@Preview(showBackground = true)
@Composable
private fun CommunityPostCardPreview() {
    val now = LocalDateTime.now()

    HopesTheme {
        CommunityPostCard(
            post =
                CommunityPost(
                    id = 1,
                    cohort = 10,
                    authorCohort = 10,
                    title = "기숙사 와이파이 다들 잘 돼?",
                    content = "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서… 다른 층은 어떤지 궁금해.",
                    createdAt = now.minusMinutes(10),
                    likeCount = 12,
                    commentCount = 5,
                ),
            now = now,
            onClick = {},
            onMoreClick = {},
            modifier = Modifier.padding(AppSpacing.Medium),
        )
    }
}
