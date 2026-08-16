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
    background: @Composable BoxScope.() -> Unit = {},
    contentBackgroundColor: Color = MaterialTheme.colorScheme.background,
    scaffoldContainerColor: Color = MaterialTheme.colorScheme.background,
    imeOverlay: @Composable BoxScope.(FigmaViewportMetrics) -> Unit = {},
    content: @Composable () -> Unit,
) {
    HopesScaffold(
        selectedDestination = selectedDestination,
        onNavigate = onNavigate,
        containerColor = scaffoldContainerColor,
    ) { innerPadding ->
        FigmaPhoneScreen(
            modifier = Modifier.padding(innerPadding),
            background = {
                // 콘텐츠 영역의 여백을 앱 배경으로 채워 Figma 캔버스 밖에 빈 띠가 생기지 않게 한다.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(contentBackgroundColor),
                )
                background()
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
