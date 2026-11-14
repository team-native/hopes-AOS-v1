package com.example.hopes.core.validation

private const val MAX_SCHOOL_EMAIL_LENGTH = 255
private const val MAX_USERNAME_LENGTH = 50
private const val MIN_PASSWORD_LENGTH = 8
private const val SCHOOL_EMAIL_DOMAIN = "gsm.hs.kr"

private val schoolEmailPattern =
    Regex(
        pattern = "^[^@\\s]+@gsm\\.hs\\.kr$",
        option = RegexOption.IGNORE_CASE,
    )

/** 광주소프트웨어마이스터고 이메일 형식과 최대 길이를 검사한다. */
fun String.isValidSchoolEmail(): Boolean = length <= MAX_SCHOOL_EMAIL_LENGTH && schoolEmailPattern.matches(this)

/**
 * 입력 중인 이메일이 아직 학교 이메일이 될 수 있는지 검사한다. `@` 뒤가 `gsm.hs.kr`의 앞부분이면 true다.
 * 입력 도중(`s26055@gs`)에는 오류를 띄우지 않고, `@gmail.com`처럼 다른 도메인이 확정되면 바로 알리기 위해 쓴다.
 */
fun String.canStillBecomeSchoolEmail(): Boolean {
    val domain = substringAfter(delimiter = "@", missingDelimiterValue = "")
    if ('@' !in this) return true
    return SCHOOL_EMAIL_DOMAIN.startsWith(domain, ignoreCase = true) || isValidSchoolEmail()
}

/** 회원가입 이름의 필수 입력과 최대 길이를 검사한다. */
fun String.isValidUsername(): Boolean = isNotBlank() && length <= MAX_USERNAME_LENGTH

/** 회원가입 비밀번호가 8자 이상인지 검사한다. */
fun String.hasSignupPasswordMinimumLength(): Boolean = length >= MIN_PASSWORD_LENGTH

/** 회원가입 비밀번호에 영문과 숫자가 모두 들어 있는지 검사한다. */
fun String.containsLetterAndDigit(): Boolean =
    any { character -> character.isAsciiLetter() } &&
        any { character -> character.isDigit() }

/** 회원가입 비밀번호에 `!@#$%` 같은 ASCII 특수문자가 하나 이상 들어 있는지 검사한다. 한글 등은 특수문자로 치지 않는다. */
fun String.containsSpecialCharacter(): Boolean = any { character -> character.isAsciiSymbol() }

/** 8자 이상이며 영문·숫자·특수문자를 모두 포함하고 공백이 없는 비밀번호인지 검사한다. */
fun String.isValidSignupPassword(): Boolean =
    hasSignupPasswordMinimumLength() &&
        containsLetterAndDigit() &&
        containsSpecialCharacter() &&
        none { character -> character.isWhitespace() }

private fun Char.isAsciiLetter(): Boolean = this in 'a'..'z' || this in 'A'..'Z'

private fun Char.isAsciiSymbol(): Boolean = this in '!'..'~' && !isAsciiLetter() && this !in '0'..'9'
