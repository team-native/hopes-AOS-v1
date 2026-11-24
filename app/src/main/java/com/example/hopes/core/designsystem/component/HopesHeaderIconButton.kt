package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
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
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/**
 * 화면 제목 오른쪽에 놓이는 흰색 둥근 사각형 아이콘 버튼이다.
 */
@Composable
fun HopesHeaderIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HopesHeaderIconButton(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
    )
}

/** [Painter]로 아이콘을 받는 버전이다. 피그마 SVG에서 옮긴 벡터 리소스를 그대로 쓸 때 사용한다. */
@Composable
fun HopesHeaderIconButton(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
    }
}

@Preview
@Composable
private fun HopesHeaderIconButtonPreview() {
    HopesTheme {
        HopesHeaderIconButton(
            imageVector = Icons.Outlined.Search,
            contentDescription = "",
            onClick = {},
        )
    }
}
