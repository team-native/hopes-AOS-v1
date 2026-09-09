package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/** 로그인 시트 뒤에서 시스템 바 영역까지 이어지는 전체 화면 배경을 구성한다. */
@Composable
fun AuthLoginBackdrop(modifier: Modifier = Modifier) {
    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    Box(modifier = modifier) {
        AuthBackground(modifier = Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .authBackgroundNoise(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, top = 25.dp)) {
                FigmaAuthBrandHeader(logoShadowStyle = FigmaAuthLogoShadowStyle.Login)

                Spacer(modifier = Modifier.height(74.dp))

                AuthHeroCopy()
            }
        }

        // Scrim이 status bar 아래에서 시작하면 같은 blur 위에 서로 다른 명암 경계가 생긴다.
        // 기존 하단 경계는 유지하면서 상단만 status bar 뒤까지 확장한다.
        AuthBackdropScrim(scrimHeight = 397.dp + statusBarHeight)
    }
}

/** 완만한 배경 위에 blur가 식별될 수 있는 옅은 점 노이즈를 캐시해 그린다. */
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
