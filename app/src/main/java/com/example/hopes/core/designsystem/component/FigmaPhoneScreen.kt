package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.min

/** 반응형 Figma 뷰포트의 실제 렌더링 정보를 제공한다. */
data class FigmaViewportMetrics(
    val scale: Float,
)

/**
 * 402×874 Figma iPhone 프레임을 실제 Android 화면에 비례해 표시하거나,
 * 실제 화면 크기를 그대로 사용하는 콘텐츠 영역을 제공한다.
 * 배경과 오버레이는 전체 화면을 차지해 기기 비율이 달라도 빈 공간이 보이지 않는다.
 */
@Composable
fun FigmaPhoneScreen(
    modifier: Modifier = Modifier,
    navigationBarColor: Color? = null,
    useFigmaViewport: Boolean = true,
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
        val scale = min(
            maxWidth.value / FIGMA_PHONE_WIDTH,
            maxHeight.value / FIGMA_PHONE_HEIGHT,
        )
        val viewportMetrics = FigmaViewportMetrics(scale = scale)

        background()

        if (navigationBarColor != null) {
            FigmaNavigationBarBackground(color = navigationBarColor)
        }

        if (useFigmaViewport) {
            Box(
                modifier = Modifier
                    .width(402.dp)
                    .height(874.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f),
                    ),
                contentAlignment = Alignment.TopCenter,
            ) {
                content()
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                content()
            }
        }

        overlay(viewportMetrics)
    }
}

const val FIGMA_PHONE_WIDTH = 402f
const val FIGMA_PHONE_HEIGHT = 874f
