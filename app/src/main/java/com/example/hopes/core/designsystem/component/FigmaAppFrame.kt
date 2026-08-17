package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.hopes.navigation.HopesDestination

/** 피그마 iPhone 17 Pro(402×874) 좌표계를 유지하는 앱 화면 프레임이다. */
@Composable
fun FigmaAppFrame(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    fixedTopContent: (@Composable () -> Unit)? = null,
    fixedBottomContent: (@Composable () -> Unit)? = null,
    isBottomNavigationVisible: Boolean = true,
    useFigmaViewport: Boolean = true,
    background: @Composable BoxScope.() -> Unit = {},
    contentBackgroundColor: Color = MaterialTheme.colorScheme.background,
    scaffoldContainerColor: Color = MaterialTheme.colorScheme.background,
    imeOverlay: @Composable BoxScope.(FigmaViewportMetrics) -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldContainerColor),
    ) {
        // 시스템바 아래까지 화면 배경을 그려 Scaffold의 inset 영역과 색이 달라지지 않게 한다.
        background()

        HopesScaffold(
            selectedDestination = selectedDestination,
            onNavigate = onNavigate,
            fixedTopContent = fixedTopContent,
            fixedBottomContent = fixedBottomContent,
            isBottomNavigationVisible = isBottomNavigationVisible,
            containerColor = Color.Transparent,
        ) { innerPadding ->
            FigmaPhoneScreen(
                modifier = Modifier.padding(innerPadding),
                useFigmaViewport = useFigmaViewport,
                isScrollable = false,
                background = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(contentBackgroundColor),
                    )
                },
                overlay = imeOverlay,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(contentBackgroundColor),
                ) {
                    content()
                }
            }
        }
    }
}
