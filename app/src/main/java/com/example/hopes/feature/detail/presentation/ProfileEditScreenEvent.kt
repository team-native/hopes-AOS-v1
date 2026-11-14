package com.example.hopes.feature.detail.presentation

/** 프로필 수정 화면의 입력·저장·재시도 사용자 의도다. */
sealed interface ProfileEditScreenEvent {
    data class NameChanged(
        val value: String,
    ) : ProfileEditScreenEvent

    data class IntroductionChanged(
        val value: String,
    ) : ProfileEditScreenEvent

    data object SaveClicked : ProfileEditScreenEvent

    data object RetryClicked : ProfileEditScreenEvent
}

/** 프로필 수정 화면의 일회성 효과다. */
sealed interface ProfileEditEffect {
    /** 저장에 성공해 마이페이지로 돌아가 토스트를 띄워야 한다. */
    data object Saved : ProfileEditEffect
}
