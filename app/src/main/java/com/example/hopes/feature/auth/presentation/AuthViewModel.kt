package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthEffect {
    data object Authenticated : AuthEffect
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    /** 화면 이벤트로 입력값과 인증 요청을 처리한다. */
    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            is AuthScreenEvent.EmailChanged -> updateState { copy(email = event.value, errorMessage = null) }
            is AuthScreenEvent.PasswordChanged -> updateState { copy(password = event.value, errorMessage = null) }
            is AuthScreenEvent.NameChanged -> updateState { copy(name = event.value, errorMessage = null) }
            AuthScreenEvent.LoginClicked -> login()
            AuthScreenEvent.SignUpClicked -> login()
            is AuthScreenEvent.SignUpRequested -> updateState {
                copy(
                    authStep = AuthStep.SignUp,
                    email = email.ifBlank { event.sampleEmail },
                    name = name.ifBlank { event.sampleName },
                )
            }
            AuthScreenEvent.LoginRequested -> updateState { copy(authStep = AuthStep.Login) }
            AuthScreenEvent.LoginDismissed -> updateState { copy(authStep = AuthStep.Guide) }
        }
    }

    /** 로그인 클릭 시 UseCase를 실행하고 결과를 UiState 또는 일회성 효과로 전달한다. */
    private fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (loginUseCase(currentState.email, currentState.password)) {
                is AppResult.Success -> {
                    updateState { copy(isLoading = false) }
                    _effect.emit(AuthEffect.Authenticated)
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    private fun updateState(transform: AuthUiState.() -> AuthUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
