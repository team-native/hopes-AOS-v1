package com.example.hopes.feature.detail.presentation

/** 개인 설정 화면의 프롬프트 입력·저장·재시도 사용자 의도다. */
sealed interface PersonalSettingsScreenEvent {
    data class PersonalPromptChanged(val value: String) : PersonalSettingsScreenEvent

    data object PersonalPromptSaveClicked : PersonalSettingsScreenEvent

    data object PersonalPromptRetryClicked : PersonalSettingsScreenEvent
}
