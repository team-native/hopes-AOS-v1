package com.example.hopes.feature.detail.presentation

/** 마이페이지에서 발생하는 프로필 편집·저장·설정 이동 의도다. */
sealed interface MyPageScreenEvent {
    data object AppSettingsClicked : MyPageScreenEvent

    data class ProfileNameChanged(val value: String) : MyPageScreenEvent

    data class ProfileIntroductionChanged(val value: String) : MyPageScreenEvent

    data object ProfileSaveClicked : MyPageScreenEvent
}
