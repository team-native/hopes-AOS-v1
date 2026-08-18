package com.example.hopes.core.validation

private const val MAX_SCHOOL_EMAIL_LENGTH = 255
private const val MAX_USERNAME_LENGTH = 50
private const val MIN_PASSWORD_LENGTH = 8
private const val MAX_PASSWORD_LENGTH = 15

private val schoolEmailPattern = Regex(
    pattern = "^[^@\\s]+@gsm\\.hs\\.kr$",
    option = RegexOption.IGNORE_CASE,
)

/** 광주소프트웨어마이스터고 이메일 형식과 최대 길이를 검사한다. */
fun String.isValidSchoolEmail(): Boolean {
    return length <= MAX_SCHOOL_EMAIL_LENGTH && schoolEmailPattern.matches(this)
}

/** 회원가입 이름의 필수 입력과 최대 길이를 검사한다. */
fun String.isValidUsername(): Boolean {
    return isNotBlank() && length <= MAX_USERNAME_LENGTH
}

/** 영문과 숫자로만 구성된 8~15자 비밀번호인지 검사한다. */
fun String.isValidSignupPassword(): Boolean {
    return length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH &&
        all { character -> character.isAsciiLetterOrDigit() } &&
        any { character -> character.isAsciiLetter() } &&
        any { character -> character.isDigit() }
}

private fun Char.isAsciiLetter(): Boolean {
    return this in 'a'..'z' || this in 'A'..'Z'
}

private fun Char.isAsciiLetterOrDigit(): Boolean {
    return isAsciiLetter() || isDigit()
}
