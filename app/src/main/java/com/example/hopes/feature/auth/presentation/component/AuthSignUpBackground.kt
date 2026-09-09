package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 회원가입 화면의 상단 그라디언트 배경이다. 상태바 뒤쪽까지 색을 채운다. */
@Composable
fun AuthSignUpBackground(modifier: Modifier = Modifier) {
    val extendedColors = LocalHopesExtendedColors.current
    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // 콘텐츠 좌표계 기준 250dp를 유지하도록 상태바 높이만큼 더 그린다.
            .height(250.dp + statusBarHeight)
            .background(
                Brush.verticalGradient(
                    listOf(
                        extendedColors.signupGradientStart,
                        extendedColors.signupGradientEnd,
                    ),
                ),
            ),
    )
}
