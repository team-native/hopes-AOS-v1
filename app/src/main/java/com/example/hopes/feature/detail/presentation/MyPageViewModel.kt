package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 마이페이지 프로필 요약을 불러온다. 프로필 수정은 ProfileEditViewModel이 맡는다. */
@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val getProfileUseCase: GetProfileUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MyPageUiState())
        val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

        init {
            refreshProfile()
        }

        /** 프로필 수정 화면에서 돌아왔을 때처럼 서버 프로필을 다시 읽어 요약을 갱신한다. */
        fun refreshProfile() {
            viewModelScope.launch {
                _uiState.update { it.copy(isProfileLoading = true, isProfileLoadFailed = false) }
                when (val result = getProfileUseCase()) {
                    is AppResult.Success -> {
                        val profile = result.value
                        _uiState.update {
                            it.copy(
                                profileName = profile.nickname,
                                email = profile.email,
                                major = profile.major,
                                isProfileLoading = false,
                            )
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isProfileLoading = false, isProfileLoadFailed = true) }
                    }
                }
            }
        }
    }
