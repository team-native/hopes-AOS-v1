package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.auth.presentation.content.AUTH_LOGIN_SHEET_PEEK_HEIGHT
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import kotlin.random.Random

/** 접힘·펼침 로그인 시트가 공통으로 사용하는 인증 배경이다. */
@Composable
fun AuthSharedBackdrop(
    sheetExpansionProgress: Float,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(modifier = modifier) {
        AuthBackground(modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .authBackgroundNoise(),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AUTH_LOGIN_SHEET_PEEK_HEIGHT)
                .statusBarsPadding(),
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, top = 25.dp)) {
                FigmaAuthBrandHeader(logoShadowStyle = FigmaAuthLogoShadowStyle.Login)

                Spacer(modifier = Modifier.height(74.dp))

                AuthHeroCopy()
            }

            Spacer(modifier = Modifier.weight(23f))

            AuthSwipeHint(
                extendedColors = extendedColors,
                modifier = Modifier.graphicsLayer {
                    alpha = 1f - sheetExpansionProgress
                },
            )

            Spacer(modifier = Modifier.weight(5f))
        }
    }
}

/** 완만한 공용 배경 위에 blur가 식별될 수 있는 옅은 점 노이즈를 캐시해 그린다. */
private fun Modifier.authBackgroundNoise(): Modifier = drawWithCache {
    val random = Random(20260821)
    val dotCount = (size.width * size.height / 4000f).toInt().coerceAtLeast(1)
    val dots = List(dotCount) {
        Triple(
            Offset(random.nextFloat() * size.width, random.nextFloat() * size.height),
            random.nextFloat() * 2.5f + 0.5f,
            random.nextFloat() * 0.12f + 0.04f,
        )
    }

    onDrawBehind {
        dots.forEach { (offset, radius, alpha) ->
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = radius,
                center = offset,
            )
        }
    }
}
