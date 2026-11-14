package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.core.session.SessionManager
import com.example.hopes.core.session.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 앱 진입 시 저장된 세션을 복원하고 인증 게이트에 상태를 제공한다. */
@HiltViewModel
class SessionViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
    ) : ViewModel() {
        val sessionState: StateFlow<SessionState> = sessionManager.state

        init {
            viewModelScope.launch {
                sessionManager.restoreSession()
            }
        }

        /** 로그인 만료 안내 다이얼로그에서 확인을 눌렀을 때 인증 화면으로 넘긴다. */
        fun acknowledgeExpiredSession() {
            sessionManager.acknowledgeExpiredSession()
        }
    }
