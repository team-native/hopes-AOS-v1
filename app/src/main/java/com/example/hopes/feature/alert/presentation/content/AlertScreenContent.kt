package com.example.hopes.feature.alert.presentation.content

import androidx.compose.runtime.Composable
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.alert.presentation.AlertUiState
import com.example.hopes.feature.alert.presentation.component.AlertHeader
import com.example.hopes.feature.alert.presentation.component.AlertNotificationList
import com.example.hopes.feature.alert.presentation.component.AlertReadAllButton
import com.example.hopes.navigation.HopesDestination

/** 피그마 09 알림 프레임의 헤더·목록·읽음 처리 버튼을 조합한다. */
@Composable
fun AlertScreenContent(
    uiState: AlertUiState,
    onEditClick: () -> Unit,
    onNotificationActionClick: (String) -> Unit,
    onReadAllClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Home,
        onNavigate = onNavigate,
    ) {
        AlertHeader(
            isEditing = uiState.isEditing,
            onEditClick = onEditClick,
        )
        AlertNotificationList(
            notifications = uiState.notifications,
            isReadAll = uiState.isReadAll,
            onActionClick = onNotificationActionClick,
        )
        AlertReadAllButton(
            isReadAll = uiState.isReadAll,
            onClick = onReadAllClick,
        )
    }
}
