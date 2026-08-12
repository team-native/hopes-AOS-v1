package com.example.hopes.feature.alert.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.alert.presentation.content.AlertScreenContent
import com.example.hopes.navigation.HopesDestination

/** 알림 화면 상태와 단일 이벤트 흐름을 콘텐츠에 전달한다. */
@Composable
fun AlertScreen(
    uiState: AlertUiState,
    onEvent: (AlertScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    AlertScreenContent(
        uiState = uiState,
        onEditClick = { onEvent(AlertScreenEvent.EditClicked) },
        onNotificationActionClick = { notificationId ->
            onEvent(AlertScreenEvent.NotificationActionClicked(notificationId))
        },
        onReadAllClick = { onEvent(AlertScreenEvent.ReadAllClicked) },
        onNavigate = onNavigate,
    )
}
