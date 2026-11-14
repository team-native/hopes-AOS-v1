package com.example.hopes.feature.home.presentation

/**
 * 홈 인사말("서하님, 반가워요")에 넣을 이름을 만든다.
 * 세 글자 한글 이름은 성을 뺀 두 글자로 부르고, 그 외(두 글자·네 글자 이상·영문 등)는 그대로 쓴다.
 * 공백뿐이면 null을 돌려 이름 없는 인사말을 쓰게 한다.
 */
internal fun String.toHomeGreetingName(): String? {
    val name = trim()
    if (name.isEmpty()) return null

    val isThreeLetterKoreanName = name.length == KOREAN_FULL_NAME_LENGTH && name.all { it in '가'..'힣' }

    return if (isThreeLetterKoreanName) name.drop(1) else name
}

private const val KOREAN_FULL_NAME_LENGTH = 3
