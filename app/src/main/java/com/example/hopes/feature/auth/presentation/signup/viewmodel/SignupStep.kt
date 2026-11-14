package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 화면 상단 진행 바에 표시되는 회원가입 단계다. */
enum class SignupStep(
    val number: Int,
) {
    Email(number = 1),
    Password(number = 2),
    Profile(number = 3),
}
