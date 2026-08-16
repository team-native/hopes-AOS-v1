package com.example.hopes.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/** 설정 화면에서 한 번만 소비할 탐색 효과를 정의한다. */
sealed interface SettingsEffect {
    data object LoggedOut : SettingsEffect
}

/** 설정 화면의 로그아웃 요청과 화면 전환 효과를 관리한다. */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect: SharedFlow<SettingsEffect> = _effect.asSharedFlow()

    /** 로그아웃 버튼에서 호출되어 세션 종료 후 인증 화면 전환 효과를 전달한다. */
    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.emit(SettingsEffect.LoggedOut)
        }
    }
}
