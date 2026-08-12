package com.example.hopes.feature.home.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.home.presentation.content.HomeScreenContent
import com.example.hopes.navigation.HopesDestination

/** 홈 화면 콘텐츠에 단일 화면 이벤트를 전달한다. */
@Composable
fun HomeScreen(
    onEvent: (HomeScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HomeScreenContent(
        onStartChatClick = { onEvent(HomeScreenEvent.StartChatClicked) },
        onNavigate = onNavigate,
    )
}
