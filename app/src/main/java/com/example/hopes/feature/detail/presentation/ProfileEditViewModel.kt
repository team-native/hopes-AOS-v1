package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetProfileUseCase
import com.example.hopes.domain.usecase.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 프로필 조회(GET /api/mypage)와 이름·자기소개 저장(PATCH /api/mypage)을 관리한다. */
@HiltViewModel
class ProfileEditViewModel
    @Inject
    constructor(
        private val getProfileUseCase: GetProfileUseCase,
        private val updateProfileUseCase: UpdateProfileUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProfileEditUiState())
        val uiState: StateFlow<ProfileEditUiState> = _uiState.asStateFlow()

        // Route가 잠시 수집하지 않는 동안(화면 회전 등) 발생한 효과도 잃지 않도록 버퍼가 있는 Channel을 쓴다.
        private val _effect = Channel<ProfileEditEffect>(Channel.BUFFERED)
        val effect: Flow<ProfileEditEffect> = _effect.receiveAsFlow()

        init {
            loadProfile()
        }

        fun onEvent(event: ProfileEditScreenEvent) {
            when (event) {
                is ProfileEditScreenEvent.NameChanged -> {
                    _uiState.update { it.copy(name = event.value, isSaveFailed = false) }
                }

                is ProfileEditScreenEvent.IntroductionChanged -> {
                    _uiState.update { it.copy(introduction = event.value, isSaveFailed = false) }
                }

                ProfileEditScreenEvent.SaveClicked -> {
                    saveProfile()
                }

                ProfileEditScreenEvent.RetryClicked -> {
                    loadProfile()
                }
            }
        }

        private fun loadProfile() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, isLoadFailed = false) }
                when (val result = getProfileUseCase()) {
                    is AppResult.Success -> {
                        val profile = result.value
                        _uiState.update {
                            it.copy(
                                name = profile.nickname,
                                introduction = profile.profileInfo,
                                email = profile.email,
                                major = profile.major,
                                isLoading = false,
                            )
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isLoading = false, isLoadFailed = true) }
                    }
                }
            }
        }

        private fun saveProfile() {
            val currentState = _uiState.value
            if (!currentState.isSaveEnabled) return

            viewModelScope.launch {
                _uiState.update { it.copy(isSaving = true, isSaveFailed = false) }
                val result =
                    updateProfileUseCase(
                        ProfileUpdateRequest(
                            nickname = currentState.name.trim(),
                            profileInfo = currentState.introduction,
                        ),
                    )
                if (result is AppResult.Success) {
                    _uiState.update { it.copy(isSaving = false) }
                    _effect.send(ProfileEditEffect.Saved)
                } else {
                    _uiState.update { it.copy(isSaving = false, isSaveFailed = true) }
                }
            }
        }
    }
