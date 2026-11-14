package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.detail.presentation.MyPageActivityStats
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 작성글·댓글·대화 수를 세로 구분선으로 나눠 보여주는 카드다(피그마 31). */
@Composable
fun MyPageActivityStatsCard(
    stats: MyPageActivityStats,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).padding(vertical = AppSpacing.Medium + AppSpacing.Small),
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
    count: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.clearAndSetSemantics { contentDescription = "$label $count" },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Headline,
        )

        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Caption,
        )
    }
}

@Composable
private fun StatDivider() {
    VerticalDivider(
        modifier =
            Modifier
                .fillMaxHeight()
                .padding(vertical = AppSpacing.Small),
        color = MaterialTheme.colorScheme.outline,
    )
}
