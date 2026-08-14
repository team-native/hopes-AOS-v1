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

    /** 입력 변경과 저장 클릭을 처리한다. 저장은 변경 사항을 서버에 반영한다. */
    fun onEvent(event: MyPageScreenEvent) {
        when (event) {
            is MyPageScreenEvent.ProfileNameChanged -> {
                updateState {
                    copy(
                        profileName = event.value,
                        isProfileSaved = false,
                        isProfileSaveFailed = false,
                    )
                }
            }

            is MyPageScreenEvent.ProfileIntroductionChanged -> {
                updateState {
                    copy(
                        profileIntroduction = event.value,
                        isProfileSaved = false,
                        isProfileSaveFailed = false,
                    )
                }
            }

            MyPageScreenEvent.ProfileSaveClicked -> saveProfile()
            MyPageScreenEvent.AppSettingsClicked -> Unit
        }
    }

    // 마이페이지 진입 시 GET /api/mypage를 호출해 서버 프로필을 입력창에 반영한다.
    private fun loadProfile() {
        viewModelScope.launch {
            updateState {
                copy(
                    isProfileLoading = true,
                    isProfileLoadFailed = false,
                    isProfileSaveFailed = false,
                )
            }

            when (val result = getProfileUseCase()) {
                is AppResult.Success -> updateProfileState(result.value)
                else -> {
                    updateState {
                        copy(
                            isProfileLoading = false,
                            isProfileLoadFailed = true,
                        )
                    }
                }
            }
        }
    }

    // 저장 클릭 시 nickname과 profileInfo만 PATCH /api/mypage로 전송한다.
    private fun saveProfile() {
        val currentState = _uiState.value
        if (currentState.isProfileLoading || currentState.isProfileSaving) {
            return
        }

        val request = ProfileUpdateRequest(
            nickname = currentState.profileName,
            profileInfo = currentState.profileIntroduction,
        )

        viewModelScope.launch {
            updateState {
                copy(
                    isProfileSaving = true,
                    isProfileSaved = false,
                    isProfileSaveFailed = false,
                )
            }

            when (val result = updateProfileUseCase(request)) {
                is AppResult.Success -> {
                    updateProfileState(
                        profile = result.value,
                        isProfileSaved = true,
                    )
                }

                else -> {
                    updateState {
                        copy(
                            isProfileSaving = false,
                            isProfileSaved = false,
                            isProfileSaveFailed = true,
                        )
                    }
                }
            }
        }
    }

    // 서버 응답을 화면 입력 상태로 변환하고 네트워크 진행 상태를 종료한다.
    private fun updateProfileState(
        profile: UserProfile,
        isProfileSaved: Boolean = false,
    ) {
        updateState {
            copy(
                profileName = profile.nickname,
                profileIntroduction = profile.profileInfo,
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
