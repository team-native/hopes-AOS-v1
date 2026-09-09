package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.detail.presentation.content.ContactScreenContent
import com.example.hopes.navigation.HopesDestination

/** 문의 입력 상태와 사용자 의도를 콘텐츠로 전달한다. */
@Composable
fun ContactScreen(
    uiState: ContactUiState,
    onEvent: (ContactScreenEvent) -> Unit,
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    ContactScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onBackClick = onBackClick,
        onNavigate = onNavigate,
    )
}
