package com.example.hopes.feature.detail.presentation

/** 문의하기 입력과 전송 요청 상태를 표현한다. */
data class ContactUiState(
    val email: String = "",
    val message: String = "",
    val isSending: Boolean = false,
    val isSent: Boolean = false,
    val isSendFailed: Boolean = false,
)
