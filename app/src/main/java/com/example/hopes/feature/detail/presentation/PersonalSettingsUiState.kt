package com.example.hopes.feature.detail.presentation

/** 서버에 저장된 시스템 프롬프트와 개인 설정 요청 상태를 표현한다. */
data class PersonalSettingsUiState(
    val personalPrompt: String = "",
    val isPromptLoading: Boolean = true,
    val isPromptLoadFailed: Boolean = false,
    val isPromptSaving: Boolean = false,
    val isPromptSaved: Boolean = false,
    val isPromptSaveFailed: Boolean = false,
) {
    /** 불러오기가 끝났고 저장 중이 아닐 때만 저장할 수 있다. 빈 프롬프트 저장은 기본 스타일로 되돌리는 의미라 허용한다. */
    val isSaveEnabled: Boolean
        get() = !isPromptLoading && !isPromptLoadFailed && !isPromptSaving
}
