package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.component.AuthBackground
import com.example.hopes.feature.auth.presentation.content.AuthGuideSheetContent

/** 피그마 01 인증 안내 화면 진입점이다. */
@Composable
fun AuthGuideScreen(onNavigateLogin: () -> Unit) {
    FigmaPhoneScreen(
        applyStatusBarsPadding = true,
        background = {
            AuthBackground(modifier = Modifier.fillMaxSize())
        },
    ) {
        AuthGuideSheetContent(onNavigateLogin = onNavigateLogin)
    }
}
