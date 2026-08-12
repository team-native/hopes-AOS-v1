package com.example.hopes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val lightColorScheme = lightColorScheme(
    primary = HopesBlue,
    onPrimary = HopesSurface,
    primaryContainer = HopesBlueContainer,
    onPrimaryContainer = HopesBlue,
    background = HopesBackground,
    onBackground = HopesText,
    surface = HopesSurface,
    onSurface = HopesText,
    surfaceVariant = HopesBlueContainer,
    onSurfaceVariant = HopesMutedText,
    outline = HopesOutline,
)

private val darkColorScheme = darkColorScheme(
    primary = HopesBlueDark,
    onPrimary = HopesText,
    primaryContainer = HopesBlueContainerDark,
    onPrimaryContainer = HopesBlueDark,
    background = HopesBackgroundDark,
    onBackground = HopesTextDark,
    surface = HopesSurfaceDark,
    onSurface = HopesTextDark,
    surfaceVariant = HopesBlueContainerDark,
    onSurfaceVariant = HopesMutedTextDark,
    outline = HopesOutlineDark,
)

@Composable
fun HopesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val appColorScheme = if (darkTheme) {
        darkColorScheme
    } else {
        lightColorScheme
    }
    val extendedColors = if (darkTheme) {
        darkHopesExtendedColors
    } else {
        lightHopesExtendedColors
    }

    CompositionLocalProvider(LocalHopesExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = appColorScheme,
            typography = HopesTypography,
            content = content,
        )
    }
}
