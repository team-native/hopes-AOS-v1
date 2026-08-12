package com.example.hopes.data.repository

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.core.session.SessionManager
import com.example.hopes.data.api.*
import com.example.hopes.data.remote.*
import com.example.hopes.domain.repository.*
import javax.inject.Inject

private suspend fun <T> NetworkResult<T>.expireSessionOnUnauthorized(sessionManager: SessionManager): NetworkResult<T> { if (this is NetworkResult.HttpError && statusCode == 401) sessionManager.expireSession(); return this }
class AuthRepositoryImpl @Inject constructor(private val remote: AuthRemoteDataSource, private val sessionManager: SessionManager) : AuthRepository {
    override suspend fun login(username: String, password: String) = remote.login(LoginRequestDto(username, password)).also { if (it is NetworkResult.Success) sessionManager.saveAccessToken(it.value.accessToken) }
    override suspend fun signUp(request: SignupRequestDto) = remote.signUp(request).also { if (it is NetworkResult.Success) sessionManager.saveAccessToken(it.value.accessToken) }
    override suspend fun sendSignupCode(email: String) = remote.sendSignupCode(EmailRequestDto(email)); override suspend fun confirmSignupCode(email: String, code: String) = remote.confirmSignupCode(EmailCodeRequestDto(email, code)); override suspend fun requestPasswordReset(email: String) = remote.requestPasswordReset(EmailRequestDto(email)); override suspend fun resetPassword(request: PasswordResetRequestDto) = remote.resetPassword(request)
}
class ChatRepositoryImpl @Inject constructor(private val remote: ChatRemoteDataSource, private val sessionManager: SessionManager) : ChatRepository { override suspend fun getMain(keyword: String?, page: Int, size: Int) = remote.main(keyword,page,size).expireSessionOnUnauthorized(sessionManager); override suspend fun getChat(id: Long,page: Int,size: Int)=remote.chat(id,page,size).expireSessionOnUnauthorized(sessionManager); override suspend fun createChat(title: String?)=remote.create(CreateChatRequestDto(title)).expireSessionOnUnauthorized(sessionManager); override suspend fun sendMessage(id: Long,content: String)=remote.send(id,SendMessageRequestDto(content)).expireSessionOnUnauthorized(sessionManager) }
class SettingsRepositoryImpl @Inject constructor(private val remote: SettingsRemoteDataSource, private val sessionManager: SessionManager) : SettingsRepository { override suspend fun logout(): NetworkResult<MessageEnvelopeDto> { val result=remote.logout(); sessionManager.expireSession(); return result }; override suspend fun updateTheme(theme:String)=remote.theme(ThemeRequestDto(theme)).expireSessionOnUnauthorized(sessionManager); override suspend fun getProfile()=remote.profile().expireSessionOnUnauthorized(sessionManager); override suspend fun updateProfile(request:MyPageUpdateRequestDto)=remote.updateProfile(request).expireSessionOnUnauthorized(sessionManager); override suspend fun getSettings()=remote.settings().expireSessionOnUnauthorized(sessionManager); override suspend fun updateSettings(request:SettingUpdateRequestDto)=remote.updateSettings(request).expireSessionOnUnauthorized(sessionManager); override suspend fun submitInquiry(content:String)=remote.inquiry(ContentRequestDto(content)).expireSessionOnUnauthorized(sessionManager) }
