package com.example.hopes.feature.detail.presentation

/**
 * 문의하기 입력과 전송 요청 상태를 표현한다.
 *
 * @param isEmailError 보내기를 눌렀을 때 이메일 형식이 틀렸으면 true다. 이메일을 고치면 다시 false가 된다.
 * @param isMessageError 보내기를 눌렀을 때 문의 내용이 비어 있었으면 true다.
 * @param isSent 전송에 성공해 완료 화면(피그마 29-4)을 보여줘야 하면 true다.
 */
data class ContactUiState(
    val email: String = "",
    val message: String = "",
    val isEmailError: Boolean = false,
    val isMessageError: Boolean = false,
    val isSending: Boolean = false,
    val isSent: Boolean = false,
    val isSendFailed: Boolean = false,
)
