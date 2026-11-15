package com.example.hopes.core.designsystem.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** Figma 원본의 0 8 22 -12 rgba(13, 26, 46, .09) 카드 그림자를 재현한다. */
fun Modifier.figmaRaisedShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 22.dp.toPx()
        spread = (-12).dp.toPx()
        offset = Offset(x = 0f, y = 8.dp.toPx())
        color = FIGMA_SHADOW_COLOR
        alpha = 0.09f
    }

/** Figma 02 로그인 배경 로고의 0 8 18 -10 rgba(13, 26, 46, .08) 그림자다. */
fun Modifier.figmaLoginLogoShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 18.dp.toPx()
        spread = (-10).dp.toPx()
        offset = Offset(x = 0f, y = 8.dp.toPx())
        color = FIGMA_SHADOW_COLOR
        alpha = 0.08f
    }

/** 피그마 v1.5 Cloud/sm: (0, 4, blur 16, #1A4080 5%)와 (0, 1, blur 3, #1A4080 3%) 두 겹이다. */
fun Modifier.figmaSubtleShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 16.dp.toPx()
        offset = Offset(x = 0f, y = 4.dp.toPx())
        color = FIGMA_CLOUD_COLOR
        alpha = 0.05f
    }.dropShadow(shape) {
        radius = 3.dp.toPx()
        offset = Offset(x = 0f, y = 1.dp.toPx())
        color = FIGMA_CLOUD_COLOR
        alpha = 0.03f
    }

/** 피그마 v1.5 Cloud/md: (0, 8, blur 28, #1A4080 9%)와 (0, 2, blur 6, #1A4080 4%) 두 겹이다. */
fun Modifier.figmaMediumShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 28.dp.toPx()
        offset = Offset(x = 0f, y = 8.dp.toPx())
        color = FIGMA_CLOUD_COLOR
        alpha = 0.09f
    }.dropShadow(shape) {
        radius = 6.dp.toPx()
        offset = Offset(x = 0f, y = 2.dp.toPx())
        color = FIGMA_CLOUD_COLOR
        alpha = 0.04f
    }

/** 로그인 시트에 쓰이는 Figma 원본의 0 14 32 -16 rgba(13, 26, 46, .12) 그림자다. */
fun Modifier.figmaSheetShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 32.dp.toPx()
        spread = (-16).dp.toPx()
        offset = Offset(x = 0f, y = 14.dp.toPx())
        color = FIGMA_SHADOW_COLOR
        alpha = 0.12f
    }

/** 피그마 02 로그인 Sheet의 위쪽 그림자다: drop-shadow(0, -4, blur 12, #1A3366 6%). */
fun Modifier.figmaTopSheetShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 12.dp.toPx()
        spread = 0f
        offset = Offset(x = 0f, y = (-4).dp.toPx())
        color = Color(0xFF1A3366)
        alpha = 0.06f
    }

/** 로그인 안내에 노출되는 피크 시트의 Figma 원본 그림자다. */
fun Modifier.figmaPeekSheetShadow(shape: Shape): Modifier =
    dropShadow(shape) {
        radius = 28.dp.toPx()
        spread = 0f
        offset = Offset(x = 0f, y = (-12).dp.toPx())
        color = Color(0xFF0A1F38)
        alpha = 0.16f
    }

private val FIGMA_SHADOW_COLOR = Color(0xFF0D1A2E)

private val FIGMA_CLOUD_COLOR = Color(0xFF1A4080)
