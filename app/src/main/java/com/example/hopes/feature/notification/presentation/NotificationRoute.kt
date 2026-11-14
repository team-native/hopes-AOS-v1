package com.example.hopes.feature.notification.presentation

import androidx.compose.runtime.Composable

/**
 * 알림 화면이다. 알림 서버 기능이 없어 피그마 시안의 예시 알림을 보여준다.
 * "보기"는 알림 상세로 가고, 편집·모두 읽음 처리는 연동 전까지 동작하지 않는다.
 */
@Composable
fun NotificationRoute(
    onBackClick: () -> Unit,
    onNotificationClick: (Long) -> Unit,
) {
    NotificationScreen(
        items = sampleNotifications(),
        isLoading = false,
        onBackClick = onBackClick,
        onEditClick = {},
        onViewClick = { item -> onNotificationClick(item.id) },
        onMarkAllReadClick = {},
    )
}
