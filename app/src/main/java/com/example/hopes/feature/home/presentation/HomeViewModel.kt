package com.example.hopes.feature.home.presentation

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

/** 홈 인사말에 쓸 프로필 이름을 불러온다. 실패하면 이름 없는 인사말을 그대로 둔다. */
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getProfileUseCase: GetProfileUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        init {
            loadUserName()
        }

        private fun loadUserName() {
            viewModelScope.launch {
                val result = getProfileUseCase()
                _uiState.update { state ->
                    state.copy(
                        userName = (result as? AppResult.Success)?.value?.nickname?.toHomeGreetingName(),
                        isLoading = false,
                    )
                }
            }
        }
    }
