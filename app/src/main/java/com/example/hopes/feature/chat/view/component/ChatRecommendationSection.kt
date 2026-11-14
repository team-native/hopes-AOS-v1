package com.example.hopes.feature.chat.view.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
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
            style = HopesTextStyles.CaptionStrong,
        )

        if (onRefreshClick != null) {
            Box(
                modifier =
                    Modifier
                        .size(AppSize.IconButton)
                        .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onRefreshClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = stringResource(R.string.chat_recommendation_refresh),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AppIconSize.Small),
                )
            }
        }
    }
}

/** 아이콘·질문·오른쪽 화살표로 된 추천 질문 카드다. 누르면 질문이 입력창에 채워진다. */
@Composable
fun ChatRecommendedQuestionCard(
    icon: ImageVector,
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
                ).shapeClickable(shape = cardShape, role = Role.Button, onClick = onClick)
                .padding(horizontal = AppSpacing.Item, vertical = AppSpacing.Item),
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
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Medium),
        )
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
private fun ChatQuestionIconTile(icon: ImageVector) {
    Box(
        modifier =
            Modifier
                .size(ICON_TILE_SIZE)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(AppRadius.SmallButton),
                ),
        contentAlignment = Alignment.Center,
    ) {
        // 바로 옆 질문 문구가 의미를 전달하므로 아이콘은 장식으로 취급한다.
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppIconSize.Small),
        )
    }
}

private val ICON_TILE_SIZE = 32.dp
private val PLACEHOLDER_LINE_HEIGHT = 10.dp
private const val PLACEHOLDER_ALPHA = 0.5f
private const val LONG_LINE_FRACTION = 0.7f
private const val SHORT_LINE_FRACTION = 0.4f
private const val SKELETON_ROW_COUNT = 4
