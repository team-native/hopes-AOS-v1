package com.example.hopes.feature.detail.presentation

/** 개인 설정·문의 화면이 표시할 서버 연동 및 입력 상태다. */
data class DetailUiState(
    val personalPrompt: String = "",
    val isPromptLoading: Boolean = false,
    val isPromptLoadError: Boolean = false,
    val isPromptSaving: Boolean = false,
    val isPromptSaved: Boolean = false,
    val isPromptSaveError: Boolean = false,
    val contactEmail: String = "",
    val contactMessage: String = "",
    val isContactSubmitting: Boolean = false,
    val isContactSent: Boolean = false,
    val isContactSubmitError: Boolean = false,
)
