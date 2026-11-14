package com.example.hopes.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

sealed interface SessionState {
    data object Loading : SessionState

    data object Unauthenticated : SessionState

    data object Authenticated : SessionState

    /** 서버가 401로 세션을 끊었다. 사용자가 "다시 로그인해 주세요" 안내를 확인하면 [Unauthenticated]로 넘어간다. */
    data object Expired : SessionState
}

/** 앱 전체의 세션 인증 정보 저장·조회·만료를 단일 책임으로 관리한다. */
@Singleton
class SessionManager
    @Inject
    constructor(
        private val encryptedTokenStore: EncryptedTokenStore,
    ) {
        private val tokenReference = AtomicReference<String?>(null)
        private val tokenTypeReference = AtomicReference<String?>(null)
        private val _state = MutableStateFlow<SessionState>(SessionState.Loading)
        val state: StateFlow<SessionState> = _state.asStateFlow()
        private val isRestored = AtomicBoolean(false)

        /** Interceptor가 블로킹 없이 읽는 현재 access token이다. */
        fun currentAccessToken(): String? = tokenReference.get()

        /** Interceptor가 블로킹 없이 읽는 현재 tokenType이다. */
        fun currentTokenType(): String? = tokenTypeReference.get()

        /** 앱 시작 시 DataStore의 세션 인증 정보를 메모리 세션과 인증 상태로 복원한다. */
        suspend fun restoreSession() {
            if (!isRestored.compareAndSet(false, true)) {
                return
            }

            val storedSession = encryptedTokenStore.read()
            if (storedSession == null) {
                tokenReference.set(null)
                tokenTypeReference.set(null)
                _state.value = SessionState.Unauthenticated
                return
            }

            tokenReference.set(storedSession.accessToken)
            tokenTypeReference.set(storedSession.tokenType)
            _state.value = SessionState.Authenticated
        }

        /** 로그인·회원가입 성공 직후 access token과 tokenType을 암호화해 저장한다. */
        suspend fun saveSession(
            accessToken: String,
            tokenType: String,
        ) {
            encryptedTokenStore.save(accessToken, tokenType)
            tokenReference.set(accessToken)
            tokenTypeReference.set(tokenType)
            _state.value = SessionState.Authenticated
        }

        /** 로그아웃·회원탈퇴처럼 사용자가 직접 세션을 끝낼 때 인증 정보를 삭제하고 비인증 상태로 전환한다. */
        suspend fun expireSession() {
            clearCredentials()
            _state.value = SessionState.Unauthenticated
        }

        /**
         * 서버가 401로 인증을 거부했을 때 인증 정보를 삭제하고 [SessionState.Expired]로 전환한다.
         * 화면은 로그인 만료 안내를 띄운 뒤 [acknowledgeExpiredSession]으로 인증 화면에 넘어간다.
         */
        suspend fun expireSessionByServer() {
            clearCredentials()
            // 이미 로그아웃된 뒤 늦게 도착한 401은 만료 안내를 다시 띄우지 않는다.
            if (_state.value == SessionState.Authenticated) {
                _state.value = SessionState.Expired
            }
        }

        /** 로그인 만료 안내를 확인했으므로 인증 화면으로 넘어간다. */
        fun acknowledgeExpiredSession() {
            if (_state.value == SessionState.Expired) {
                _state.value = SessionState.Unauthenticated
            }
        }

        private suspend fun clearCredentials() {
            encryptedTokenStore.clear()
            tokenReference.set(null)
            tokenTypeReference.set(null)
        }
    }
