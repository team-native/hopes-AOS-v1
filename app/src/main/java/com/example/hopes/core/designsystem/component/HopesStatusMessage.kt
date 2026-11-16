package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/**
 * 목록 대신 화면 가운데에 띄우는 원형 아이콘 + 제목 + 설명 + 작은 버튼 묶음이다
 * (커뮤니티 19, 기록 24-3·24-4, 알림 25-3의 빈 화면·불러오기 오류).
 *
 * @param actionText null이면 버튼 없이 안내만 보여준다.
 */
@Composable
fun HopesStatusMessage(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    HopesStatusMessage(
        iconPainter = rememberVectorPainter(icon),
        title = title,
        description = description,
        modifier = modifier,
        actionText = actionText,
        onActionClick = onActionClick,
    )
}

/** [Painter]로 아이콘을 받는 버전이다. 피그마 SVG에서 옮긴 벡터 리소스를 그대로 쓸 때 사용한다. */
@Composable
fun HopesStatusMessage(
    iconPainter: Painter,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    val actionShape = RoundedCornerShape(AppRadius.SmallButton)

    Column(
        modifier = modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ICON_CIRCLE_SIZE)
                    .figmaRaisedShadow(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    ).border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            // 아래 제목이 같은 상황을 설명하므로 아이콘은 장식으로 취급한다.
            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Heading,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Label,
        )

        if (actionText == null) return@Column

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Box(
            modifier =
                Modifier
                    .height(AppSize.SmallButton)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = actionShape,
                    ).shapeClickable(shape = actionShape, role = Role.Button, onClick = onActionClick)
                    .padding(horizontal = ACTION_PADDING_HORIZONTAL),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = actionText,
                color = MaterialTheme.colorScheme.onPrimary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

// 피그마 19·24-3·25-3 빈 화면의 원형 아이콘 배경과 아이콘 크기다.
private val ICON_CIRCLE_SIZE = 88.dp
private val ICON_SIZE = 36.dp
private val ACTION_PADDING_HORIZONTAL = 14.dp
