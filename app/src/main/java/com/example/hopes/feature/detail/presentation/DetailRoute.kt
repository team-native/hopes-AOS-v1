package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.navigation.HopesDestination

/**
 * Navigation scope가 보관한 로컬 상태를 각 상세 화면으로 전달한다.
 * 실제 상태 갱신과 back stack 변경은 호출한 navigation route가 처리한다.
 */
@Composable
fun DetailRoute(
    screenType: DetailScreenType,
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    when (screenType) {
        DetailScreenType.ChatDetail -> {
            ChatDetailScreen(
                uiState = uiState,
                onEvent = onEvent,
                onNavigate = onNavigate,
            )
        }

        DetailScreenType.MyPage -> {
            MyPageScreen(
                uiState = uiState,
                onEvent = onEvent,
                onNavigate = onNavigate,
            )
        }

        DetailScreenType.PersonalSettings -> {
            PersonalSettingsScreen(
                uiState = uiState,
                onEvent = onEvent,
                onNavigate = onNavigate,
            )
        }

        DetailScreenType.Contact -> {
            ContactScreen(
                uiState = uiState,
                onEvent = onEvent,
                onNavigate = onNavigate,
            )
        }
    }
}
