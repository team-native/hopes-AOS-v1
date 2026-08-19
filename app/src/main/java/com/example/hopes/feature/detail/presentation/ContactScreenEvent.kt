package com.example.hopes.feature.detail.presentation

/** 문의하기 화면의 입력·전송 사용자 의도다. */
sealed interface ContactScreenEvent {
    data class ContactEmailChanged(val value: String) : ContactScreenEvent

    data class ContactMessageChanged(val value: String) : ContactScreenEvent

    data object ContactSendClicked : ContactScreenEvent
}
