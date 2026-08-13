package com.example.hopes.data.repository

import com.example.hopes.core.session.SessionManager
import com.example.hopes.data.mapper.toAppResult
import com.example.hopes.data.mapper.toDomain
import com.example.hopes.data.mapper.toDto
import com.example.hopes.data.api.ContentRequestDto
import com.example.hopes.data.api.CreateChatRequestDto
import com.example.hopes.data.api.EmailRequestDto
import com.example.hopes.data.api.LoginRequestDto
import com.example.hopes.data.api.SendMessageRequestDto
import com.example.hopes.data.api.SettingUpdateRequestDto
import com.example.hopes.data.api.ThemeRequestDto
import com.example.hopes.data.remote.AuthRemoteDataSource
import com.example.hopes.data.remote.ChatRemoteDataSource
import com.example.hopes.data.remote.SettingsRemoteDataSource
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.model.PasswordResetRequest
import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/** 인증 만료 응답을 앱 전체 세션 종료로 연결한다. */
private suspend fun <T> AppResult<T>.expireSessionOnUnauthorized(
    sessionManager: SessionManager,
): AppResult<T> {
    if (this is AppResult.HttpError && statusCode == UNAUTHORIZED_STATUS_CODE) {
        sessionManager.expireSession()
    }

    return this
}

/** 인증 원격 데이터와 access token 세션을 조합한다. */
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val sessionManager: SessionManager,
) : AuthRepository {
    override suspend fun login(username: String, password: String): AppResult<AuthToken> {
        val result = authRemoteDataSource.login(LoginRequestDto(username, password)).toAppResult { tokenDto ->
            tokenDto.toDomain()
        }

        if (result is AppResult.Success) {
            sessionManager.saveAccessToken(result.value.accessToken)
        }

        return result
    }

    override suspend fun signUp(request: SignUpRequest): AppResult<AuthToken> {
        val result = authRemoteDataSource.signUp(request.toDto()).toAppResult { tokenDto ->
            tokenDto.toDomain()
        }

        if (result is AppResult.Success) {
            sessionManager.saveAccessToken(result.value.accessToken)
        }

        return result
    }

    override suspend fun sendSignupCode(email: String): AppResult<Unit> {
        return authRemoteDataSource.sendSignupCode(EmailRequestDto(email)).toAppResult { Unit }
    }

    override suspend fun requestPasswordReset(email: String): AppResult<Unit> {
        return authRemoteDataSource.requestPasswordReset(EmailRequestDto(email)).toAppResult { Unit }
    }

    override suspend fun resetPassword(request: PasswordResetRequest): AppResult<Unit> {
        return authRemoteDataSource.resetPassword(request.toDto()).toAppResult { Unit }
    }
}

/** 채팅 원격 데이터를 도메인 저장소 계약으로 제공한다. */
class ChatRepositoryImpl @Inject constructor(
    private val chatRemoteDataSource: ChatRemoteDataSource,
    private val sessionManager: SessionManager,
) : ChatRepository {
    override suspend fun getChats(keyword: String?, page: Int, size: Int): AppResult<ChatPage> {
        return chatRemoteDataSource.getMain(keyword, page, size).toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun getChat(id: Long, page: Int, size: Int): AppResult<Chat> {
        return chatRemoteDataSource.getChat(id, page, size).toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun createChat(title: String?): AppResult<Chat> {
        return chatRemoteDataSource.createChat(CreateChatRequestDto(title)).toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun sendMessage(id: Long, content: String): AppResult<Chat> {
        return chatRemoteDataSource.sendMessage(id, SendMessageRequestDto(content)).toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }
}

/** 설정 원격 데이터와 세션 종료 정책을 조합한다. */
class SettingsRepositoryImpl @Inject constructor(
    private val settingsRemoteDataSource: SettingsRemoteDataSource,
    private val sessionManager: SessionManager,
) : SettingsRepository {
    override suspend fun logout(): AppResult<Unit> {
        val result = settingsRemoteDataSource.logout().toAppResult { Unit }
        sessionManager.expireSession()
        return result
    }

    override suspend fun updateTheme(theme: String): AppResult<String> {
        return settingsRemoteDataSource.updateTheme(ThemeRequestDto(theme)).toAppResult { responseDto ->
            responseDto.theme
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun getProfile(): AppResult<UserProfile> {
        return settingsRemoteDataSource.getProfile().toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun updateProfile(request: ProfileUpdateRequest): AppResult<UserProfile> {
        return settingsRemoteDataSource.updateProfile(request.toDto()).toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun getSettings(): AppResult<UserSettings> {
        return settingsRemoteDataSource.getSettings().toAppResult { responseDto ->
            responseDto.toDomain()
        }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun updateCustomPrompt(customPrompt: String?): AppResult<UserSettings> {
        return settingsRemoteDataSource.updateSettings(SettingUpdateRequestDto(customPrompt = customPrompt))
            .toAppResult { responseDto ->
                responseDto.toDomain()
            }
            .expireSessionOnUnauthorized(sessionManager)
    }

    override suspend fun submitInquiry(content: String): AppResult<Unit> {
        return settingsRemoteDataSource.submitInquiry(ContentRequestDto(content)).toAppResult { Unit }
            .expireSessionOnUnauthorized(sessionManager)
    }
}

private const val UNAUTHORIZED_STATUS_CODE = 401
