package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/** 402×874 Figma iPhone 프레임을 실제 화면 폭에 비례해 표시한다. */
@Composable
fun FigmaPhoneScreen(content: @Composable () -> Unit) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        val scale = maxWidth.value / FIGMA_PHONE_WIDTH
        Box(
            modifier = Modifier
                .then(Modifier)
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
    }
}

const val FIGMA_PHONE_WIDTH = 402f
