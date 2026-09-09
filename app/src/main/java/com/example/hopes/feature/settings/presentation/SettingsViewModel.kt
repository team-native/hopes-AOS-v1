package com.example.hopes.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.DeleteAccountUseCase
import com.example.hopes.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 설정 화면에서 한 번만 소비할 탐색 효과를 정의한다. */
sealed interface SettingsEffect {
    data object LoggedOut : SettingsEffect

    data object AccountDeleted : SettingsEffect
}

/** 설정 화면의 로그아웃 요청과 화면 전환 효과를 관리한다. */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect: SharedFlow<SettingsEffect> = _effect.asSharedFlow()

    /** 로그아웃 버튼에서 호출되어 세션 종료 후 인증 화면 전환 효과를 전달한다. */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.emit(SettingsEffect.LoggedOut)
        }
    }

    /** 화면 이벤트에 따라 로그아웃과 회원탈퇴 요청을 분기한다. */
    fun onEvent(event: SettingsScreenEvent) {
        when (event) {
            SettingsScreenEvent.LogoutClicked -> logout()
            SettingsScreenEvent.DeleteAccountClicked -> clearAccountDeletionError()
        }
    }

    /** 현재 비밀번호로 회원탈퇴를 요청하고 성공 시 세션 종료 효과를 전달한다. */
    fun deleteAccount(password: String) {
        if (password.isBlank() || _uiState.value.isDeletingAccount) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDeletingAccount = true,
                    accountDeletionError = null,
                )
            }

            when (val result = deleteAccountUseCase(password)) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isDeletingAccount = false) }
                    _effect.emit(SettingsEffect.AccountDeleted)
                }

                is AppResult.HttpError -> {
                    _uiState.update {
                        it.copy(
                            isDeletingAccount = false,
                            accountDeletionError = AccountDeletionError.Http(
                                statusCode = result.statusCode,
                                serverMessage = result.message,
                            ),
                        )
                    }
                }

                AppResult.NetworkError -> {
                    _uiState.update {
                        it.copy(
                            isDeletingAccount = false,
                            accountDeletionError = AccountDeletionError.Network,
                        )
                    }
                }

                AppResult.SerializationError -> {
                    _uiState.update {
                        it.copy(
                            isDeletingAccount = false,
                            accountDeletionError = AccountDeletionError.Serialization,
                        )
                    }
                }
            }
        }
    }

    /** 회원탈퇴 BottomSheet를 다시 열 때 이전 실패 문구를 제거한다. */
    fun clearAccountDeletionError() {
        _uiState.update { it.copy(accountDeletionError = null) }
    }
}
