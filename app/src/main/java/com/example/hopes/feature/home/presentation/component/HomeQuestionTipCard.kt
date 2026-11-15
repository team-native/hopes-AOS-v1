package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** "질문 잘하는 방법"의 번호 원 + 회색 윗줄 + 굵은 아랫줄로 된 카드다. */
@Composable
fun HomeQuestionTipCard(
    index: Int,
    topText: String,
    bottomText: String,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .testTag("home_tip_card_$index")
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                .padding(horizontal = CARD_PADDING_HORIZONTAL, vertical = CARD_PADDING_VERTICAL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(NUMBER_CIRCLE_SIZE)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString(),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.LabelStrong,
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Column {
            Text(
                text = topText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )

            Spacer(modifier = Modifier.height(AppSpacing.XSmall))

            Text(
                text = bottomText,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )
        }
    }
}

private val NUMBER_CIRCLE_SIZE = 28.dp

// 피그마 Tip Row: 안쪽 여백 16/14 + 테두리 1(Compose 테두리는 크기를 차지하지 않아 더한다)로 한 줄 높이가 72다.
private val CARD_PADDING_HORIZONTAL = 17.dp
private val CARD_PADDING_VERTICAL = 15.dp
