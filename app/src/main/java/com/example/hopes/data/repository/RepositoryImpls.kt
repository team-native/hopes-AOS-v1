package com.example.hopes.data.repository

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.core.session.SessionManager
import com.example.hopes.data.api.CreateChatRequestDto
import com.example.hopes.data.api.ContentRequestDto
import com.example.hopes.data.api.LoginRequestDto
import com.example.hopes.data.api.MyPageUpdateRequestDto
import com.example.hopes.data.api.SendMessageRequestDto
import com.example.hopes.data.api.SettingUpdateRequestDto
import com.example.hopes.data.api.ThemeRequestDto
import com.example.hopes.data.mapper.toApiValue
import com.example.hopes.data.mapper.toAppResult
import com.example.hopes.data.mapper.toAppTheme
import com.example.hopes.data.mapper.toDomain
import com.example.hopes.data.remote.AuthRemoteDataSource
import com.example.hopes.data.remote.ChatRemoteDataSource
import com.example.hopes.data.remote.SettingsRemoteDataSource
import com.example.hopes.domain.model.AppTheme
import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.model.ProfileUpdate
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/** 인증 실패 결과는 저장된 토큰을 폐기해 앱 최상위 상태와 일치시킨다. */
private suspend fun <T> NetworkResult<T>.expireSessionOnUnauthorized(
    sessionManager: SessionManager,
): NetworkResult<T> {
    if (this is NetworkResult.HttpError && statusCode == 401) {
        sessionManager.expireSession()
    }

    return this
}

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteDataSource,
    private val sessionManager: SessionManager,
) : AuthRepository {

    /** 로그인 성공 토큰을 저장한 뒤 Domain 모델로 변환한다. */
    override suspend fun login(username: String, password: String): AppResult<AuthToken> {
        val networkResult = remote.login(
            LoginRequestDto(
                username = username,
                password = password,
            ),
        )

        if (networkResult is NetworkResult.Success) {
            sessionManager.saveAccessToken(networkResult.value.accessToken)
        }

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }
}

class ChatRepositoryImpl @Inject constructor(
    private val remote: ChatRemoteDataSource,
    private val sessionManager: SessionManager,
) : ChatRepository {

    override suspend fun getChats(keyword: String?, page: Int, size: Int): AppResult<ChatPage> {
        val networkResult = remote.main(keyword, page, size)
            .expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun getChat(chatId: Long, page: Int, size: Int): AppResult<Chat> {
        val networkResult = remote.chat(chatId, page, size)
            .expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun createChat(title: String?): AppResult<Chat> {
        val networkResult = remote.create(CreateChatRequestDto(title))
            .expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun sendMessage(chatId: Long, content: String): AppResult<Chat> {
        val networkResult = remote.send(
            chatId,
            SendMessageRequestDto(content),
        ).expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }
}

class SettingsRepositoryImpl @Inject constructor(
    private val remote: SettingsRemoteDataSource,
    private val sessionManager: SessionManager,
) : SettingsRepository {

    override suspend fun logout(): AppResult<Unit> {
        val networkResult = remote.logout()
        sessionManager.expireSession()

        return networkResult.toAppResult {
            Unit
        }
    }

    override suspend fun updateTheme(theme: AppTheme): AppResult<AppTheme> {
        val networkResult = remote.theme(ThemeRequestDto(theme.toApiValue()))
            .expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.theme.toAppTheme()
        }
    }

    override suspend fun getProfile(): AppResult<UserProfile> {
        val networkResult = remote.profile().expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun updateProfile(update: ProfileUpdate): AppResult<UserProfile> {
        val networkResult = remote.updateProfile(
            MyPageUpdateRequestDto(
                username = update.username,
                nickname = update.nickname,
                profileInfo = update.profileInfo,
                profileImage = update.profileImage,
            ),
        ).expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun getSettings(): AppResult<UserSettings> {
        val networkResult = remote.settings().expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun updateCustomPrompt(customPrompt: String): AppResult<UserSettings> {
        val networkResult = remote.updateSettings(
            SettingUpdateRequestDto(
                customPrompt = customPrompt,
            ),
        ).expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult { response ->
            response.toDomain()
        }
    }

    override suspend fun submitInquiry(content: String): AppResult<Unit> {
        val networkResult = remote.inquiry(ContentRequestDto(content))
            .expireSessionOnUnauthorized(sessionManager)

        return networkResult.toAppResult {
            Unit
        }
    }
}
