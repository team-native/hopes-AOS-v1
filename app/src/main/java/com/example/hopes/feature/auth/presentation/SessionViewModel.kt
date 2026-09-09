package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.core.session.SessionManager
import com.example.hopes.core.session.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** 앱 진입 시 저장된 세션을 복원하고 인증 게이트에 상태를 제공한다. */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    val sessionState: StateFlow<SessionState> = sessionManager.state

    init {
        viewModelScope.launch {
            sessionManager.restoreSession()
        }
    }
}
