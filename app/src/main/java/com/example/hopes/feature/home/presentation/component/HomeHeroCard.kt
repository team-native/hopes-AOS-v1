package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
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
                .background(Brush.linearGradient(listOf(surfaceColor, accentColor)))
                .border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape),
    ) {
        // 피그마 Hero Card의 구름 인스턴스다. 카드 안에 보이는 부분(185×115)만 렌더링해 오른쪽 아래에 붙인다.
        Image(
            painter = painterResource(R.drawable.decor_cloud_soft),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .requiredSize(width = CLOUD_WIDTH, height = CLOUD_HEIGHT),
        )

        // 피그마 Hero Card(높이 176, 테두리 1): 글자 시작 x 22·위 24, 버튼 아래 22.
        Column(
            modifier =
                Modifier.padding(
                    start = HERO_PADDING_START,
                    top = HERO_PADDING_TOP,
                    end = HERO_PADDING_START,
                    bottom = HERO_PADDING_BOTTOM,
                ),
        ) {
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

            Spacer(modifier = Modifier.height(HERO_BUTTON_GAP))

            Box(
                modifier =
                    Modifier
                        .height(AppSize.SmallButton)
                        .testTag("home_start_chat_button")
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = buttonShape,
                        ).shapeClickable(shape = buttonShape, role = Role.Button, onClick = onStartChatClick)
                        .padding(horizontal = HERO_BUTTON_PADDING_HORIZONTAL),
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

// 피그마 30 홈 카드 오른쪽 아래의 구름이다(카드 안에 보이는 크기).
private val CLOUD_WIDTH = 185.dp
private val CLOUD_HEIGHT = 115.dp
private val HERO_PADDING_START = 21.dp
private val HERO_PADDING_TOP = 23.dp
private val HERO_PADDING_BOTTOM = 21.dp
private val HERO_BUTTON_GAP = 6.dp
private val HERO_BUTTON_PADDING_HORIZONTAL = 14.dp
