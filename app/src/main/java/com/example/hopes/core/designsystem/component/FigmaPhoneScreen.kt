package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/** 반응형 Figma 뷰포트의 실제 렌더링 정보를 제공한다. */
data class FigmaViewportMetrics(
    val scale: Float,
)

/**
 * 402×874 Figma iPhone 프레임을 실제 Android 화면 폭에 맞춰 표시한다.
 * 화면 높이가 짧은 기기에서는 전체 화면을 축소하지 않고 세로 스크롤로 접근한다.
 */
@Composable
fun FigmaPhoneScreen(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        )
    },
    overlay: @Composable BoxScope.(FigmaViewportMetrics) -> Unit = {},
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        val viewportWidth = minOf(maxWidth, FIGMA_PHONE_WIDTH.dp)
        val scale = viewportWidth.value / FIGMA_PHONE_WIDTH
        val viewportHeight = (FIGMA_PHONE_HEIGHT * scale).dp
        val viewportMetrics = FigmaViewportMetrics(scale = scale)

        background()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .width(viewportWidth)
                    .height(viewportHeight),
            ) {
                Box(
                    modifier = Modifier
                        .width(FIGMA_PHONE_WIDTH.dp)
                        .height(FIGMA_PHONE_HEIGHT.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f),
                        ),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    content()
                }
            }
        }

        overlay(viewportMetrics)
    }
}

const val FIGMA_PHONE_WIDTH = 402f
const val FIGMA_PHONE_HEIGHT = 874f
