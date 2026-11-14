package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 회원가입 화면에서 Route로 전달되는 사용자 이벤트다. */
sealed interface SignupScreenEvent {
    data object BackClicked : SignupScreenEvent

    /** 하단 버튼. 현재 단계와 상태에 따라 발송·확인·재발송·다음·가입 완료 중 하나로 동작한다. */
    data object PrimaryActionClicked : SignupScreenEvent

    data class EmailChanged(
        val value: String,
    ) : SignupScreenEvent

    data object ChangeEmailClicked : SignupScreenEvent

    data class CodeChanged(
        val value: String,
    ) : SignupScreenEvent

    data object ResendCodeClicked : SignupScreenEvent

    data class PasswordChanged(
        val value: String,
    ) : SignupScreenEvent

    data class PasswordConfirmChanged(
        val value: String,
    ) : SignupScreenEvent

    data class NameChanged(
        val value: String,
    ) : SignupScreenEvent

    data object DepartmentFieldClicked : SignupScreenEvent

    data object GenerationFieldClicked : SignupScreenEvent

    data class TermsAgreementChanged(
        val isAgreed: Boolean,
    ) : SignupScreenEvent
}
