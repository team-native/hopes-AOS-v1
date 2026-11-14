package com.example.hopes.feature.home.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.home.presentation.content.HomeScreenContent
import com.example.hopes.navigation.HopesDestination

/** 홈 화면 상태와 이벤트를 콘텐츠에 전달한다. */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HomeScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onNavigate = onNavigate,
    )
}
