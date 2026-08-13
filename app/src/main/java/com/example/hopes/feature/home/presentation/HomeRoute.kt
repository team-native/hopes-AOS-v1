package com.example.hopes.feature.home.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.navigation.HopesDestination

/** 홈 화면 이벤트를 앱 탐색 동작에 연결한다. */
@Composable
fun HomeRoute(onNavigate: (HopesDestination) -> Unit) {
    HomeScreen(
        onEvent = { event ->
            when (event) {
                HomeScreenEvent.StartChatClicked -> onNavigate(HopesDestination.Chat)
            }
        },
        onNavigate = onNavigate,
    )
}
