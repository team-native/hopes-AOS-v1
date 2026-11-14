package com.example.hopes.feature.detail.presentation

/**
 * 문의 답변을 받을 이메일 형식인지 확인한다. 학교 메일이 아니어도 되므로 "아이디@도메인.최상위도메인" 꼴만 본다.
 * JVM 단위 테스트에서도 돌도록 android.util.Patterns 대신 정규식을 쓴다.
 */
internal fun String.isValidContactEmail(): Boolean = contactEmailPattern.matches(trim())

private val contactEmailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
