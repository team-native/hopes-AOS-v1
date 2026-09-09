package com.example.hopes.core.designsystem.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** Figma 원본의 0 8 22 -12 rgba(13, 26, 46, .09) 카드 그림자를 재현한다. */
fun Modifier.figmaRaisedShadow(shape: Shape): Modifier = dropShadow(shape) {
    radius = 22.dp.toPx()
    spread = (-12).dp.toPx()
    offset = Offset(x = 0f, y = 8.dp.toPx())
    color = FIGMA_SHADOW_COLOR
    alpha = 0.09f
}

/** Figma 02 로그인 배경 로고의 0 8 18 -10 rgba(13, 26, 46, .08) 그림자다. */
fun Modifier.figmaLoginLogoShadow(shape: Shape): Modifier = dropShadow(shape) {
    radius = 18.dp.toPx()
    spread = (-10).dp.toPx()
    offset = Offset(x = 0f, y = 8.dp.toPx())
    color = FIGMA_SHADOW_COLOR
    alpha = 0.08f
}

/** Figma 원본의 0 6 16 -10 rgba(13, 26, 46, .07) 보조 카드 그림자를 재현한다. */
fun Modifier.figmaSubtleShadow(shape: Shape): Modifier = dropShadow(shape) {
    radius = 16.dp.toPx()
    spread = (-10).dp.toPx()
    offset = Offset(x = 0f, y = 6.dp.toPx())
    color = FIGMA_SHADOW_COLOR
    alpha = 0.07f
}

/** 로그인 시트에 쓰이는 Figma 원본의 0 14 32 -16 rgba(13, 26, 46, .12) 그림자다. */
fun Modifier.figmaSheetShadow(shape: Shape): Modifier = dropShadow(shape) {
    radius = 32.dp.toPx()
    spread = (-16).dp.toPx()
    offset = Offset(x = 0f, y = 14.dp.toPx())
    color = FIGMA_SHADOW_COLOR
    alpha = 0.12f
}

/** 로그인 안내에 노출되는 피크 시트의 Figma 원본 그림자다. */
fun Modifier.figmaPeekSheetShadow(shape: Shape): Modifier = dropShadow(shape) {
    radius = 28.dp.toPx()
    spread = 0f
    offset = Offset(x = 0f, y = (-12).dp.toPx())
    color = Color(0xFF0A1F38)
    alpha = 0.16f
}

private val FIGMA_SHADOW_COLOR = Color(0xFF0D1A2E)
