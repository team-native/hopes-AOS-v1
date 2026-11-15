package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/**
 * 화면 제목 오른쪽에 놓이는 흰색 둥근 사각형 아이콘 버튼이다(알림, 설정, 검색 등).
 *
 * @param hasBadge true면 오른쪽 위에 읽지 않음을 뜻하는 빨간 점을 표시한다.
 */
@Composable
fun HopesHeaderIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasBadge: Boolean = false,
) {
    HopesHeaderIconButton(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        hasBadge = hasBadge,
    )
}

/** [Painter]로 아이콘을 받는 버전이다. 피그마 SVG에서 옮긴 벡터 리소스를 그대로 쓸 때 사용한다. */
@Composable
fun HopesHeaderIconButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasBadge: Boolean = false,
) {
    val buttonShape = RoundedCornerShape(AppRadius.HeaderButton)

    Box(
        modifier =
            modifier
                .size(AppSize.HeaderButton)
                .figmaSubtleShadow(buttonShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = buttonShape,
                ).border(
                    AppBorderWidth.Default,
                    LocalHopesExtendedColors.current.authFieldBorder,
                    buttonShape,
                ).shapeClickable(shape = buttonShape, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(AppIconSize.Medium),
        )

        if (hasBadge) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = -BADGE_INSET_END, y = BADGE_INSET_TOP)
                        .size(BADGE_SIZE)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = CircleShape,
                        ).border(BADGE_BORDER, MaterialTheme.colorScheme.surface, CircleShape),
            )
        }
    }
}

// 피그마 알림 버튼의 빨간 점 크기와 모서리에서 떨어진 거리다.
// 피그마 알림 점: 9×9(흰 테두리 1.5 포함), 버튼 오른쪽에서 8, 위에서 6 떨어져 있다.
private val BADGE_SIZE = 9.dp
private val BADGE_BORDER = 1.5.dp
private val BADGE_INSET_END = 8.dp
private val BADGE_INSET_TOP = 6.dp

@Preview
@Composable
private fun HopesHeaderIconButtonPreview() {
    HopesTheme {
        HopesHeaderIconButton(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "",
            onClick = {},
            hasBadge = true,
        )
    }
}
