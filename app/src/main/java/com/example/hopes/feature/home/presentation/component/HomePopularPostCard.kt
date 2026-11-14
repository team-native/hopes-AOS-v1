package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.home.presentation.HomePopularPost
import com.example.hopes.ui.theme.HopesTextStyles
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
                ).shapeClickable(shape = cardShape, role = Role.Button, onClick = onClick)
                .padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Item + AppSpacing.XSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(AppSpacing.Small + AppSpacing.XSmall),
                    ).padding(horizontal = AppSpacing.Compact, vertical = AppSpacing.XSmall),
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

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Row(
            modifier = Modifier.semantics { contentDescription = likeDescription },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(AppIconSize.Inline),
            )

            Spacer(modifier = Modifier.width(AppSpacing.XSmall))

            Text(
                text = post.likeCount.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
        }
    }
}
