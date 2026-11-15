package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSkeleton
import com.example.hopes.core.designsystem.component.HopesSkeletonLine
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.teamnative.hopes.R

/** 홈을 처음 불러오는 동안 카드 자리마다 회색 자리 표시를 그린다(피그마 30-2). 섹션 제목은 그대로 보여준다. */
@Composable
fun HomeLoadingContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        SkeletonCard {
            HopesSkeletonLine(modifier = Modifier.width(SHORT_LINE_WIDTH))
            HopesSkeletonLine(modifier = Modifier.fillMaxWidth(LONG_LINE_FRACTION))
            HopesSkeletonLine(modifier = Modifier.fillMaxWidth(MIDDLE_LINE_FRACTION))
            HopesSkeletonLine(modifier = Modifier.fillMaxWidth(SHORT_LINE_FRACTION))
        }

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        HomeSectionHeader(title = stringResource(R.string.home_question_tips_title))

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        SkeletonCard {
            repeat(TIP_ROW_COUNT) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HopesSkeleton(
                        modifier = Modifier.size(ICON_PLACEHOLDER_SIZE),
                        shape = RoundedCornerShape(AppRadius.SmallButton),
                    )

                    Spacer(modifier = Modifier.width(AppSpacing.Item))

                    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small + AppSpacing.XSmall)) {
                        HopesSkeletonLine(modifier = Modifier.fillMaxWidth(LONG_LINE_FRACTION))
                        HopesSkeletonLine(modifier = Modifier.fillMaxWidth(SHORT_LINE_FRACTION))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        HomeSectionHeader(title = stringResource(R.string.home_popular_posts_title))

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        SkeletonCard {
            HopesSkeletonLine(modifier = Modifier.width(SHORT_LINE_WIDTH))
            HopesSkeletonLine(modifier = Modifier.fillMaxWidth(LONG_LINE_FRACTION))
            HopesSkeletonLine(modifier = Modifier.fillMaxWidth(MIDDLE_LINE_FRACTION))
        }
    }
}

@Composable
private fun SkeletonCard(content: @Composable ColumnScope.() -> Unit) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).padding(AppSpacing.Medium + AppSpacing.Small),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        content = content,
    )
}

private val SHORT_LINE_WIDTH = 32.dp
private val ICON_PLACEHOLDER_SIZE = 32.dp
private const val LONG_LINE_FRACTION = 0.85f
private const val MIDDLE_LINE_FRACTION = 0.7f
private const val SHORT_LINE_FRACTION = 0.45f
private const val TIP_ROW_COUNT = 2
