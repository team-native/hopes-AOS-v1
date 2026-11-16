package com.example.hopes.feature.chat.view.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 추천 질문 영역 위의 "○○ · ○○ 추천 질문" 문구와 오른쪽 새로 고침 버튼이다. */
@Composable
fun ChatRecommendationHeader(
    label: String,
    onRefreshClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )

        if (onRefreshClick != null) {
            // 피그마 icon/refresh: 18dp. 누르는 영역은 시각 크기를 그대로 따른다.
            Icon(
                painter = painterResource(R.drawable.ic_refresh),
                contentDescription = stringResource(R.string.chat_recommendation_refresh),
                tint = LocalHopesExtendedColors.current.authFieldHint,
                modifier =
                    Modifier
                        .size(REFRESH_ICON_SIZE)
                        .clickable(role = Role.Button, onClick = onRefreshClick),
            )
        }
    }
}

/** 아이콘·질문·오른쪽 화살표로 된 추천 질문 카드다. 누르면 질문이 입력창에 채워진다. */
@Composable
fun ChatRecommendedQuestionCard(
    @DrawableRes icon: Int,
    question: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

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
                // 피그마 05 List Row: 안쪽 여백 16/12에 테두리 1을 더해 높이가 62다.
                .padding(horizontal = CARD_PADDING_HORIZONTAL, vertical = CARD_PADDING_VERTICAL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChatQuestionIconTile(icon = icon)

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Text(
            text = question,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Body,
        )

        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = LocalHopesExtendedColors.current.authFieldHint,
            modifier = Modifier.size(ROW_ICON_SIZE),
        )
    }
}

/**
 * 학과·전공을 고르기 전(피그마 05-3) "기본 추천 질문"을 한 장의 카드 안에 구분선으로 이어 붙여 보여준다.
 *
 * @param questions 아이콘 리소스와 질문 문구 쌍이다.
 */
@Composable
fun ChatDefaultQuestionCard(
    questions: List<Pair<Int, String>>,
    onQuestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Panel)
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                .clip(cardShape)
                // 피그마 05-3 Card: 좌우 16·위아래 4에 테두리 1을 더한 값이다.
                .padding(horizontal = CARD_PADDING_HORIZONTAL, vertical = DEFAULT_CARD_PADDING_VERTICAL),
    ) {
        questions.forEachIndexed { index, (icon, question) ->
            if (index > 0) {
                HorizontalDivider(color = LocalHopesExtendedColors.current.authFieldBorder)
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button, onClick = { onQuestionClick(question) })
                        .padding(vertical = AppSpacing.Item),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ChatQuestionIconTile(icon = icon)
                Spacer(modifier = Modifier.width(AppSpacing.Item))
                Text(
                    text = question,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = HopesTextStyles.Body,
                )
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = LocalHopesExtendedColors.current.authFieldHint,
                    modifier = Modifier.size(ROW_ICON_SIZE),
                )
            }
        }
    }
}

/** 추천 질문을 불러오는 동안 보여주는 회색 자리 표시 카드다(피그마 05-4). */
@Composable
fun ChatRecommendationSkeleton(
    modifier: Modifier = Modifier,
    rowCount: Int = SKELETON_ROW_COUNT,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)
    val placeholderColor = MaterialTheme.colorScheme.outline.copy(alpha = PLACEHOLDER_ALPHA)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).padding(AppSpacing.Item),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium),
    ) {
        repeat(rowCount) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier =
                        Modifier
                            .size(ICON_TILE_SIZE)
                            .background(placeholderColor, RoundedCornerShape(AppRadius.SmallButton)),
                )

                Spacer(modifier = Modifier.width(AppSpacing.Item))

                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small + AppSpacing.XSmall)) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(LONG_LINE_FRACTION)
                                .height(PLACEHOLDER_LINE_HEIGHT)
                                .background(placeholderColor, CircleShape),
                    )

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth(SHORT_LINE_FRACTION)
                                .height(PLACEHOLDER_LINE_HEIGHT)
                                .background(placeholderColor, CircleShape),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatQuestionIconTile(
    @DrawableRes icon: Int,
) {
    Box(
        modifier =
            Modifier
                .size(ICON_TILE_SIZE)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(AppRadius.IconChip),
                ),
        contentAlignment = Alignment.Center,
    ) {
        // 바로 옆 질문 문구가 의미를 전달하므로 아이콘은 장식으로 취급한다.
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(ROW_ICON_SIZE),
        )
    }
}

// 피그마 List Row: 아이콘 칩 36, 아이콘·화살표 18, 한 줄 안쪽 여백 16/12(+테두리 1). 기본 질문 카드는 위아래 4(+1).
private val ICON_TILE_SIZE = 36.dp
private val ROW_ICON_SIZE = 18.dp
private val REFRESH_ICON_SIZE = 18.dp
private val CARD_PADDING_HORIZONTAL = 17.dp
private val CARD_PADDING_VERTICAL = 13.dp
private val DEFAULT_CARD_PADDING_VERTICAL = 5.dp
private val PLACEHOLDER_LINE_HEIGHT = 10.dp
private const val PLACEHOLDER_ALPHA = 0.5f
private const val LONG_LINE_FRACTION = 0.7f
private const val SHORT_LINE_FRACTION = 0.4f
private const val SKELETON_ROW_COUNT = 4
