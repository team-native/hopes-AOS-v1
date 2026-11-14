package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 회원가입 API에 보내는 학과 코드다. 화면 표시 문구는 view 계층이 결정한다. */
enum class SignupDepartment(
    val apiCode: String,
) {
    Software(apiCode = "SOFTWARE"),
    Iot(apiCode = "IOT"),
    ArtificialIntelligence(apiCode = "AI"),
}
