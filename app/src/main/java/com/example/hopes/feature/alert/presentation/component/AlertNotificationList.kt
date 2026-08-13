package com.example.hopes.feature.alert.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    Column(
        modifier = Modifier.padding(start = 24.dp, top = 150.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        notifications.forEach { notification ->
            AlertNotificationRow(
                notification = notification,
                isReadAll = isReadAll,
                onActionClick = { onActionClick(notification.id) },
            )
        }
    }
}
