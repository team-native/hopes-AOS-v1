package com.example.hopes.feature.alert.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.alert.presentation.AlertNotificationUiModel

/** 피그마 간격에 맞춰 알림 행 목록을 배치한다. */
@Composable
fun AlertNotificationList(
    notifications: List<AlertNotificationUiModel>,
    isReadAll: Boolean,
    onActionClick: (String) -> Unit,
) {
    notifications.forEachIndexed { notificationIndex, notification ->
        AlertNotificationRow(
            notification = notification,
            isReadAll = isReadAll,
            modifier = Modifier.offset(
                x = 24.dp,
                y = (150 + (notificationIndex * 84)).dp,
            ),
            onActionClick = { onActionClick(notification.id) },
        )
    }
}
