package com.example.hopes.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** 피그마 원본의 보조 색상을 의미 기반으로 제공한다. */
@Immutable
data class HopesExtendedColors(
    val authBackdropScrim: Color,
    val authDescription: Color,
    val brandSubtitleOnBlue: Color,
    val authFieldBorder: Color,
    val authFieldHint: Color,
    val authHandle: Color,
    val signupGradientStart: Color,
    val signupGradientEnd: Color,
    val authSubtitle: Color,
    val replyFieldBackground: Color,
    val toggleTrackOff: Color,
    val logoutContainer: Color,
    val logoutText: Color,
    val onboardingStepContainer: Color,
    val onboardingStepText: Color,
)

val LocalHopesExtendedColors = staticCompositionLocalOf<HopesExtendedColors> {
    error("HopesExtendedColors is not provided.")
}

internal val lightHopesExtendedColors = HopesExtendedColors(
    authBackdropScrim = Color(0x1A000000),
    authDescription = Color(0xFFD6EDFF),
    brandSubtitleOnBlue = Color(0xFFD9F0FF),
    authFieldBorder = Color(0xFFD9D9D9),
    authFieldHint = Color(0xFF6B7A94),
    authHandle = Color(0xFFD1DEED),
    signupGradientStart = Color(0xFF2EA1ED),
    signupGradientEnd = Color(0xFF0D6BC7),
    authSubtitle = Color(0xFF718096),
    replyFieldBackground = Color(0xFFFBFCFF),
    toggleTrackOff = Color(0x4D3C3C43),
    logoutContainer = Color(0xFFFFEDED),
    logoutText = Color(0xFFE0211C),
    // Figma node 14:524의 #0A5A96 15% 원형 배경과 숫자 색상이다.
    onboardingStepContainer = Color(0x260A5A96),
    onboardingStepText = Color(0xFF0A5A96),
)

internal val darkHopesExtendedColors = HopesExtendedColors(
    authBackdropScrim = Color(0x66000000),
    authDescription = Color(0xFFD6EDFF),
    brandSubtitleOnBlue = Color(0xFFD9F0FF),
    authFieldBorder = Color(0xFF596878),
    authFieldHint = Color(0xFFB3C2D6),
    authHandle = Color(0xFF8FA8C2),
    signupGradientStart = Color(0xFF2EA1ED),
    signupGradientEnd = Color(0xFF0D6BC7),
    authSubtitle = Color(0xFFB3C2D6),
    replyFieldBackground = Color(0xFF171D23),
    toggleTrackOff = Color(0x665A6370),
    logoutContainer = Color(0xFF542323),
    logoutText = Color(0xFFFFB4AB),
    onboardingStepContainer = Color(0x403AA9E8),
    onboardingStepText = Color(0xFF8CD5FF),
)
