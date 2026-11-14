package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** "궁금한 점이 있다면, 언제든 물어보세요." 안내와 채팅 시작 버튼을 담은 홈 상단 카드다. 오른쪽 아래에 장식용 원이 걸친다. */
@Composable
fun HomeHeroCard(
    onStartChatClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)
    val buttonShape = RoundedCornerShape(AppRadius.SmallButton)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val accentColor = MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaRaisedShadow(cardShape)
                .clip(cardShape)
                .background(Brush.linearGradient(listOf(surfaceColor, accentColor))),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = DECORATION_OFFSET, y = DECORATION_OFFSET)
                    .size(DECORATION_SIZE)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = DECORATION_ALPHA),
                        shape = CircleShape,
                    ),
        )

        Column(modifier = Modifier.padding(AppSpacing.Section - AppSpacing.Small)) {
            Text(
                text = stringResource(R.string.home_hero_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.Headline,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            Text(
                text = stringResource(R.string.home_hero_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Subtitle,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            Box(
                modifier =
                    Modifier
                        .height(AppSize.SmallButton)
                        .testTag("home_start_chat_button")
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = BUTTON_CONTAINER_ALPHA),
                            shape = buttonShape,
                        ).shapeClickable(shape = buttonShape, role = Role.Button, onClick = onStartChatClick)
                        .padding(horizontal = AppSpacing.Item),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.start_chat),
                    color = MaterialTheme.colorScheme.primary,
                    style = HopesTextStyles.LabelStrong,
                )
            }
        }
    }
}

// 피그마 30 홈 카드 오른쪽 아래에 반쯤 걸친 장식 원이다.
private val DECORATION_SIZE = 120.dp
private val DECORATION_OFFSET = 28.dp
private const val DECORATION_ALPHA = 0.08f
private const val BUTTON_CONTAINER_ALPHA = 0.12f
