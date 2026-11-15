package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
    val cardShape = RoundedCornerShape(AppRadius.Hero)
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
        // 피그마 Decor/Cloud Soft 에셋(240×126 영역, 번짐 때문에 에셋은 오른쪽·아래로 조금 더 크다)이다.
        Image(
            painter = painterResource(R.drawable.decor_cloud_soft),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .offset(x = CLOUD_LEFT, y = CLOUD_TOP)
                    .requiredSize(width = CLOUD_IMAGE_WIDTH, height = CLOUD_IMAGE_HEIGHT),
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
private val CLOUD_LEFT = 176.dp
private val CLOUD_TOP = 60.dp
private val CLOUD_IMAGE_WIDTH = 259.4.dp
private val CLOUD_IMAGE_HEIGHT = 134.dp
private const val BUTTON_CONTAINER_ALPHA = 0.12f
