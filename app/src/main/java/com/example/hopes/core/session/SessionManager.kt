package com.example.hopes.core.session

import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface SessionState { data object Unauthenticated : SessionState; data object Authenticated : SessionState }

/** 앱 전체의 access token 저장·조회·만료를 단일 책임으로 관리한다. */
@Singleton
class SessionManager @Inject constructor(
    private val encryptedTokenStore: EncryptedTokenStore,
) {
    private val tokenReference = AtomicReference<String?>(null)
    private val _state = MutableStateFlow<SessionState>(SessionState.Unauthenticated)
    val state: StateFlow<SessionState> = _state.asStateFlow()

    /** Interceptor가 블로킹 없이 읽는 현재 access token이다. */
    fun currentAccessToken(): String? = tokenReference.get()

    /** 로그인·회원가입 성공 직후 토큰을 암호화해 저장한다. */
    suspend fun saveAccessToken(accessToken: String) {
        encryptedTokenStore.save(accessToken)
        tokenReference.set(accessToken)
        _state.value = SessionState.Authenticated
    }

    /** 로그아웃 또는 인증 실패 시 토큰을 삭제하고 비인증 상태로 전환한다. */
    suspend fun expireSession() {
        encryptedTokenStore.clear()
        tokenReference.set(null)
        _state.value = SessionState.Unauthenticated
    }
}
