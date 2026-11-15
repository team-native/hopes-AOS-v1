package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.home.presentation.HomePopularPost
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 커뮤니티 인기글 한 줄이다. 기수 태그, 제목, 좋아요 수를 보여준다. */
@Composable
fun HomePopularPostCard(
    post: HomePopularPost,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)
    val likeDescription = stringResource(R.string.home_post_like_count, post.likeCount)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                .shapeClickable(shape = cardShape, role = Role.Button, onClick = onClick)
                .padding(horizontal = CARD_PADDING_HORIZONTAL, vertical = CARD_PADDING_VERTICAL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                    ).padding(horizontal = BADGE_PADDING_HORIZONTAL, vertical = AppSpacing.Small),
        ) {
            Text(
                text =
                    if (post.cohort != null) {
                        stringResource(R.string.community_cohort, post.cohort)
                    } else {
                        stringResource(R.string.community_cohort_all)
                    },
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.Tag,
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Compact + AppSpacing.XSmall))

        Text(
            text = post.title,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = HopesTextStyles.Body,
        )

        Spacer(modifier = Modifier.width(AppSpacing.Compact + AppSpacing.XSmall))

        Row(
            modifier = Modifier.semantics { contentDescription = likeDescription },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_heart),
                contentDescription = null,
                tint = LocalHopesExtendedColors.current.authFieldHint,
                modifier = Modifier.size(AppIconSize.Inline),
            )

            Spacer(modifier = Modifier.width(AppSpacing.Small))

            Text(
                text = post.likeCount.toString(),
                color = LocalHopesExtendedColors.current.authFieldHint,
                style = HopesTextStyles.Caption,
            )
        }
    }
}

// 피그마 Post Row: 안쪽 여백 16/14 + 테두리 1로 한 줄 높이가 54, 배지는 가로 10·세로 4다.
private val CARD_PADDING_HORIZONTAL = 17.dp
private val CARD_PADDING_VERTICAL = 15.dp
private val BADGE_PADDING_HORIZONTAL = 10.dp
