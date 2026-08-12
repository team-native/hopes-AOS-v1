package com.example.hopes.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** 피그마 원본의 보조 색상을 의미 기반으로 제공한다. */
@Immutable
data class HopesExtendedColors(
    val authBackdropScrim: Color,
    val authDescription: Color,
    val authFieldBorder: Color,
    val authFieldHint: Color,
    val authHandle: Color,
    val authSubtitle: Color,
)

val LocalHopesExtendedColors = staticCompositionLocalOf<HopesExtendedColors> {
    error("HopesExtendedColors is not provided.")
}

internal val lightHopesExtendedColors = HopesExtendedColors(
    authBackdropScrim = Color(0x1A000000),
    authDescription = Color(0xFFD6EDFF),
    authFieldBorder = Color(0xFFD9D9D9),
    authFieldHint = Color(0xFF6B7A94),
    authHandle = Color(0xFFD1DEED),
    authSubtitle = Color(0xFF718096),
)

internal val darkHopesExtendedColors = HopesExtendedColors(
    authBackdropScrim = Color(0x66000000),
    authDescription = Color(0xFFD6EDFF),
    authFieldBorder = Color(0xFF596878),
    authFieldHint = Color(0xFFB3C2D6),
    authHandle = Color(0xFF8FA8C2),
    authSubtitle = Color(0xFFB3C2D6),
)
