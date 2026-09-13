package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role

/**
 * 버튼의 시각적 shape와 클릭 indication의 경계를 동일하게 맞춘다.
 *
 * 일반 버튼은 별도 눌림 그림자를 사용하지 않으므로 indication을 그리지 않는다.
 * 하단 NavigationBar의 Material indication은 HopesBottomNavigation에서 그대로 유지한다.
 */
fun Modifier.shapeClickable(
    shape: Shape,
    enabled: Boolean = true,
    role: Role? = Role.Button,
    onClick: () -> Unit,
): Modifier = clip(shape).clickable(
    interactionSource = null,
    indication = null,
    enabled = enabled,
    role = role,
    onClick = onClick,
)
