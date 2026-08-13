package com.example.hopes.feature.alert.presentation

/** 알림 화면에서 발생하는 사용자 의도를 정의한다. */
sealed interface AlertScreenEvent {
    data object EditClicked : AlertScreenEvent

    data object ReadAllClicked : AlertScreenEvent

    data class NotificationActionClicked(
        val notificationId: String,
    ) : AlertScreenEvent
}
