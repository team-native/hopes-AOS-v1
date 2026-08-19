package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 화면 전체를 차지하는 배경과 콘텐츠를 조합한다. 화면별 콘텐츠 스크롤은 각 화면의
 * 메시지·목록 영역에서 직접 관리한다.
 */
@Composable
fun FigmaPhoneScreen(
    modifier: Modifier = Modifier,
    navigationBarColor: Color? = null,
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
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        background()

        if (navigationBarColor != null) {
            FigmaNavigationBarBackground(color = navigationBarColor)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (applyStatusBarsPadding) Modifier.statusBarsPadding() else Modifier),
            contentAlignment = Alignment.TopCenter,
        ) {
            content()
        }
    }
}
