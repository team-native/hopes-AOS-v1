package com.example.hopes.feature.detail.presentation

/** 마이페이지의 프로필 입력·저장·설정 이동 사용자 의도다. */
sealed interface MyPageScreenEvent {
    data object AppSettingsClicked : MyPageScreenEvent

    data class ProfileNameChanged(val value: String) : MyPageScreenEvent

    data class ProfileIntroductionChanged(val value: String) : MyPageScreenEvent

    data object ProfileSaveClicked : MyPageScreenEvent

    data object ProfileRetryClicked : MyPageScreenEvent
}
