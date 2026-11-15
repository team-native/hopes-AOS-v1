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
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        // 피그마 30의 오른쪽 아래 구름이다. 위가 조금 더 진한 하늘색 원을 흐리게 번지게 그린다.
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = DECORATION_OFFSET_X, y = DECORATION_OFFSET_Y)
                    .size(DECORATION_SIZE)
                    .blur(DECORATION_BLUR, BlurredEdgeTreatment.Unbounded)
                    .background(
                        brush = Brush.verticalGradient(listOf(DECORATION_TOP_COLOR, DECORATION_BOTTOM_COLOR)),
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

// 피그마 30 홈 카드 오른쪽 아래에 걸친 구름이다. 카드 안에는 폭 약 130, 높이 약 70만 보인다(흐림 포함).
private val DECORATION_SIZE = 196.dp
private val DECORATION_OFFSET_X = 68.dp
private val DECORATION_OFFSET_Y = 112.dp
private val DECORATION_BLUR = 10.dp
private val DECORATION_TOP_COLOR = Color(0x80A3C4FF)
private val DECORATION_BOTTOM_COLOR = Color(0x66B1D0FF)
private const val BUTTON_CONTAINER_ALPHA = 0.12f
