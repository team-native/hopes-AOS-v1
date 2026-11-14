package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 비밀번호 입력란 아래 체크리스트의 조건별 충족 여부다. */
data class SignupPasswordRules(
    val hasMinimumLength: Boolean,
    val hasLetterAndDigit: Boolean,
    val hasSpecialCharacter: Boolean,
)
