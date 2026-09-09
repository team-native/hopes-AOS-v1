package com.example.hopes.feature.settings.presentation

/** 설정 화면에서 회원탈퇴 진행 상태와 실패 상태를 표현한다. */
data class SettingsUiState(
    val isDeletingAccount: Boolean = false,
    val accountDeletionError: AccountDeletionError? = null,
)

/** 회원탈퇴 요청 실패 원인을 화면에서 표시 가능한 상태로 표현한다. */
sealed interface AccountDeletionError {
    data class Http(
        val statusCode: Int,
        val serverMessage: String?,
    ) : AccountDeletionError

    data object Network : AccountDeletionError

    data object Serialization : AccountDeletionError
}
