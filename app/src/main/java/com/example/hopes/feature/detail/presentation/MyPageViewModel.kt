package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetProfileUseCase
import com.example.hopes.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 마이페이지 프로필 조회와 저장의 단방향 상태 흐름을 관리한다. */
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    /** 입력 변경과 저장 요청을 처리한다. */
    fun onEvent(event: MyPageScreenEvent) {
        when (event) {
            MyPageScreenEvent.AppSettingsClicked -> Unit
            is MyPageScreenEvent.ProfileNameChanged -> updateState {
                copy(
                    profileName = event.value,
                    isProfileSaved = false,
                    isProfileSaveFailed = false,
                )
            }
            is MyPageScreenEvent.ProfileIntroductionChanged -> updateState {
                copy(
                    profileIntroduction = event.value,
                    isProfileSaved = false,
                    isProfileSaveFailed = false,
                )
            }
            MyPageScreenEvent.ProfileSaveClicked -> saveProfile()
            MyPageScreenEvent.ProfileRetryClicked -> loadProfile()
        }
    }

    /** GET /api/mypage 결과를 편집 화면에 반영한다. */
    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isProfileLoading = true, isProfileLoadFailed = false) }
            when (val result = getProfileUseCase()) {
                is AppResult.Success -> updateProfileState(result.value)
                else -> updateState {
                    copy(isProfileLoading = false, isProfileLoadFailed = true)
                }
            }
        }
    }

    /** PATCH /api/mypage로 별명과 자기소개를 저장한다. */
    private fun saveProfile() {
        val currentState = _uiState.value
        if (currentState.isProfileLoading || currentState.isProfileSaving) return

        viewModelScope.launch {
            updateState {
                copy(
                    isProfileSaving = true,
                    isProfileSaved = false,
                    isProfileSaveFailed = false,
                )
            }
            when (
                val result = updateProfileUseCase(
                    ProfileUpdateRequest(
                        nickname = currentState.profileName,
                        profileInfo = currentState.profileIntroduction,
                    ),
                )
            ) {
                is AppResult.Success -> updateProfileState(result.value, isProfileSaved = true)
                else -> updateState {
                    copy(isProfileSaving = false, isProfileSaveFailed = true)
                }
            }
        }
    }

    /** 서버 프로필을 화면 모델로 변환하고 요청 상태를 종료한다. */
    private fun updateProfileState(profile: UserProfile, isProfileSaved: Boolean = false) {
        updateState {
            copy(
                profileName = profile.nickname,
                profileIntroduction = profile.profileInfo,
                email = profile.email,
                major = profile.major,
                isProfileLoading = false,
                isProfileLoadFailed = false,
                isProfileSaving = false,
                isProfileSaved = isProfileSaved,
                isProfileSaveFailed = false,
            )
        }
    }

    private fun updateState(transform: MyPageUiState.() -> MyPageUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
