package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.detail.presentation.MyPageActivityStats
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 작성글·댓글·대화 수를 세로 구분선으로 나눠 보여주는 카드다(피그마 31). */
@Composable
fun MyPageActivityStatsCard(
    stats: MyPageActivityStats,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Panel)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(CARD_HEIGHT)
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                // 피그마 Stats Card: 높이 100, 좌우 8.
                .padding(horizontal = AppSpacing.Compact),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MyPageStatItem(
            count = stats.postCount,
            label = stringResource(R.string.my_page_stat_posts),
            modifier = Modifier.weight(1f),
        )

        StatDivider()

        MyPageStatItem(
            count = stats.commentCount,
            label = stringResource(R.string.my_page_stat_comments),
            modifier = Modifier.weight(1f),
        )

        StatDivider()

        MyPageStatItem(
            count = stats.chatCount,
            label = stringResource(R.string.my_page_stat_chats),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MyPageStatItem(
    count: Int?,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clearAndSetSemantics { contentDescription = "$label ${count ?: UNKNOWN_COUNT}" },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = count?.toString() ?: UNKNOWN_COUNT,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Headline,
        )

        Spacer(modifier = Modifier.height(AppSpacing.XSmall))

        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Caption,
        )
    }
}

@Composable
private fun StatDivider() {
    // 피그마 Divider: 폭 1, 높이 36.
    Box(
        modifier =
            Modifier
                .width(AppBorderWidth.Default)
                .height(DIVIDER_HEIGHT)
                .background(LocalHopesExtendedColors.current.authFieldBorder),
    )
}

// 대화 수를 아직 세지 못했거나 불러오지 못했을 때 숫자 자리에 둔다.
private const val UNKNOWN_COUNT = "-"

private val CARD_HEIGHT = 100.dp
private val DIVIDER_HEIGHT = 36.dp
