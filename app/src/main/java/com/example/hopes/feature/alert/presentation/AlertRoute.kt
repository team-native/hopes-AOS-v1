package com.example.hopes.feature.alert.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.navigation.HopesDestination

/** 알림 화면의 로컬 읽음 처리 상태와 화면 이벤트를 연결한다. */
@Composable
fun AlertRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToChatDetail: (String) -> Unit,
) {
    var isReadAll by rememberSaveable { mutableStateOf(false) }
    var isEditing by rememberSaveable { mutableStateOf(false) }

    val chatQuestion = stringResource(R.string.alert_answer_arrived_question)

    AlertScreen(
        uiState = AlertUiState(
            isReadAll = isReadAll,
            isEditing = isEditing,
        ),
        onEvent = { alertScreenEvent ->
            when (alertScreenEvent) {
                AlertScreenEvent.ReadAllClicked -> isReadAll = true
                AlertScreenEvent.EditClicked -> isEditing = !isEditing
                is AlertScreenEvent.NotificationActionClicked -> {
                    when (alertScreenEvent.notificationId) {
                        "new-answer" -> onNavigateToChatDetail(chatQuestion)
                        "profile-saved" -> onNavigate(HopesDestination.Settings)
                        "admission-guide" -> onNavigate(HopesDestination.Chat)
                        "system-notice" -> onNavigate(HopesDestination.Home)
                    }
                }
            }
        },
        onNavigate = onNavigate,
    )
}
