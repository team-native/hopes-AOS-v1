package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 서버 요청이 실패한 이유다. 서버 메시지가 비어 있으면 화면에서 기본 문구로 대신한다. */
sealed interface SignupRequestError {
    data class Server(
        val message: String,
    ) : SignupRequestError

    data object Network : SignupRequestError
}
