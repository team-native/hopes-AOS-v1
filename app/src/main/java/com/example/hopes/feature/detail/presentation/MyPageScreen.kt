package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.detail.presentation.content.MyPageScreenContent
import com.example.hopes.navigation.HopesDestination

/** 마이페이지 상태와 사용자 의도를 콘텐츠로 전달한다. */
@Composable
fun MyPageScreen(
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    MyPageScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onNavigate = onNavigate,
    )
}
