package com.example.hopes.feature.detail.presentation

/** 프로필 수정 화면의 입력값과 요청 상태다. 이메일·전공은 서버에서 받아 보여주기만 한다. */
data class ProfileEditUiState(
    val name: String = "",
    val introduction: String = "",
    val email: String = "",
    val major: String? = null,
    val isLoading: Boolean = true,
    val isLoadFailed: Boolean = false,
    val isSaving: Boolean = false,
    val isSaveFailed: Boolean = false,
) {
    /** 불러오기가 끝났고 저장 중이 아니며 이름이 비어 있지 않을 때만 저장할 수 있다. */
    val isSaveEnabled: Boolean
        get() = !isLoading && !isLoadFailed && !isSaving && name.isNotBlank()
}
