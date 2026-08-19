package com.example.hopes.feature.detail.presentation

/** 서버에 저장된 시스템 프롬프트와 개인 설정 요청 상태를 표현한다. */
data class PersonalSettingsUiState(
    val personalPrompt: String = "",
    val isPromptLoading: Boolean = true,
    val isPromptLoadFailed: Boolean = false,
    val isPromptSaving: Boolean = false,
    val isPromptSaved: Boolean = false,
    val isPromptSaveFailed: Boolean = false,
)
