package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/** 반응형 Figma 뷰포트의 실제 렌더링 정보를 제공한다. */
data class FigmaViewportMetrics(
    val scale: Float,
)

/**
 * 402×874 Figma 프레임을 화면 폭에 맞춰 표시하거나 실제 화면 크기를 사용한다.
 * 화면별 콘텐츠 스크롤은 각 화면의 메시지·목록 영역에서 직접 관리한다.
 */
@Composable
fun FigmaPhoneScreen(
    modifier: Modifier = Modifier,
    navigationBarColor: Color? = null,
    useFigmaViewport: Boolean = true,
    isScrollable: Boolean = true,
    // 배경은 시스템바 뒤까지 그려진 채로 유지하고, 이 값이 true면 콘텐츠에만
    // statusBarsPadding을 적용해 화면마다 직접 상태바 인셋을 처리하지 않아도 된다.
    // 네비게이션 바 쪽은 패딩하지 않아, 시트 등 하단 콘텐츠가 네비바 영역까지 표시된다.
    applyStatusBarsPadding: Boolean = false,
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

        if (navigationBarColor != null) {
            FigmaNavigationBarBackground(color = navigationBarColor)
        }

        if (useFigmaViewport) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (isScrollable) {
                            Modifier.verticalScroll(rememberScrollState())
                        } else {
                            Modifier
                        },
                    ),
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
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (applyStatusBarsPadding) Modifier.statusBarsPadding() else Modifier),
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
