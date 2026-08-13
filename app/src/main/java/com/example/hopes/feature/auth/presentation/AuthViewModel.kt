package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /** 이메일 입력 이벤트에서 이전 오류를 지우고 화면 상태를 갱신한다. */
    fun updateUsername(value: String) {
        _uiState.value = _uiState.value.copy(
            username = value,
            error = null,
        )
    }

    /** 비밀번호 입력 이벤트에서 이전 오류를 지우고 화면 상태를 갱신한다. */
    fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value,
            error = null,
        )
    }

    /** 로그인 버튼 이벤트에서 서버 인증을 실행하고 성공·실패 상태를 반영한다. */
    fun login() {
        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            when (val result = loginUseCase(
                username = _uiState.value.username,
                password = _uiState.value.password,
            )) {
                is AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                    )
                }

                is AppResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error,
                    )
                }
            }
        }
    }
}
