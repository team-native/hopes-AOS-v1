package com.example.hopes.data.remote

import com.example.hopes.core.network.ApiExecutor
import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.AuthApiService
import com.example.hopes.data.api.ChatApiService
import com.example.hopes.data.api.ChatResponseDto
import com.example.hopes.data.api.ContentRequestDto
import com.example.hopes.data.api.CreateChatRequestDto
import com.example.hopes.data.api.DeleteAccountRequestDto
import com.example.hopes.data.api.EmailCodeRequestDto
import com.example.hopes.data.api.EmailRequestDto
import com.example.hopes.data.api.LoginRequestDto
import com.example.hopes.data.api.MainResponseDto
import com.example.hopes.data.api.MessageEnvelopeDto
import com.example.hopes.data.api.MyPageUpdateRequestDto
import com.example.hopes.data.api.PasswordResetRequestDto
import com.example.hopes.data.api.SendMessageRequestDto
import com.example.hopes.data.api.SettingMainResponseDto
import com.example.hopes.data.api.SettingUpdateRequestDto
import com.example.hopes.data.api.SettingsApiService
import com.example.hopes.data.api.SignupRequestDto
import com.example.hopes.data.api.ThemeRequestDto
import com.example.hopes.data.api.ThemeResponseDto
import com.example.hopes.data.api.TokenResponseDto
import com.example.hopes.data.api.UserResponseDto
import javax.inject.Inject

/** 인증 API의 원격 호출 계약을 정의한다. */
interface AuthRemoteDataSource {
    suspend fun login(body: LoginRequestDto): NetworkResult<TokenResponseDto>
    suspend fun signUp(body: SignupRequestDto): NetworkResult<TokenResponseDto>
    suspend fun sendSignupCode(body: EmailRequestDto): NetworkResult<MessageEnvelopeDto>
    suspend fun confirmSignupCode(body: EmailCodeRequestDto): NetworkResult<MessageEnvelopeDto>
    suspend fun requestPasswordReset(body: EmailRequestDto): NetworkResult<MessageEnvelopeDto>
    suspend fun resetPassword(body: PasswordResetRequestDto): NetworkResult<MessageEnvelopeDto>
}

/** AuthApiService 호출을 ApiExecutor로 공통 오류 결과에 정규화한다. */
class AuthRemoteDataSourceImpl @Inject constructor(
    private val apiExecutor: ApiExecutor,
    private val authApiService: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun login(body: LoginRequestDto): NetworkResult<TokenResponseDto> {
        return apiExecutor.execute {
            authApiService.login(body)
        }
    }

    override suspend fun signUp(body: SignupRequestDto): NetworkResult<TokenResponseDto> {
        return apiExecutor.execute {
            authApiService.signUp(body)
        }
    }

    override suspend fun sendSignupCode(body: EmailRequestDto): NetworkResult<MessageEnvelopeDto> {
        return apiExecutor.execute {
            authApiService.sendSignupVerification(body)
        }
    }

    override suspend fun confirmSignupCode(body: EmailCodeRequestDto): NetworkResult<MessageEnvelopeDto> {
        return apiExecutor.execute {
            authApiService.confirmSignupVerification(body)
        }
    }

    override suspend fun requestPasswordReset(body: EmailRequestDto): NetworkResult<MessageEnvelopeDto> {
        return apiExecutor.execute {
            authApiService.requestPasswordReset(body)
        }
    }

    override suspend fun resetPassword(body: PasswordResetRequestDto): NetworkResult<MessageEnvelopeDto> {
        return apiExecutor.execute {
            authApiService.resetPassword(body)
        }
    }
}

/** 채팅 API의 원격 호출 계약을 정의한다. */
interface ChatRemoteDataSource {
    suspend fun getMain(keyword: String?, page: Int, size: Int): NetworkResult<MainResponseDto>
    suspend fun getChat(id: Long, page: Int, size: Int): NetworkResult<ChatResponseDto>
    suspend fun createChat(body: CreateChatRequestDto): NetworkResult<ChatResponseDto>
    suspend fun sendMessage(id: Long, body: SendMessageRequestDto): NetworkResult<ChatResponseDto>
}

/** ChatApiService 호출을 ApiExecutor를 통해 실행한다. */
class ChatRemoteDataSourceImpl @Inject constructor(
    private val apiExecutor: ApiExecutor,
    private val chatApiService: ChatApiService,
) : ChatRemoteDataSource {
    override suspend fun getMain(keyword: String?, page: Int, size: Int): NetworkResult<MainResponseDto> {
        return apiExecutor.execute {
            chatApiService.getMain(keyword, page, size)
        }
    }

    override suspend fun getChat(id: Long, page: Int, size: Int): NetworkResult<ChatResponseDto> {
        return apiExecutor.execute {
            chatApiService.getChat(id, page, size)
        }
    }

    override suspend fun createChat(body: CreateChatRequestDto): NetworkResult<ChatResponseDto> {
        return apiExecutor.execute {
            chatApiService.createChat(body)
        }
    }

    override suspend fun sendMessage(id: Long, body: SendMessageRequestDto): NetworkResult<ChatResponseDto> {
        return apiExecutor.execute {
            chatApiService.sendMessage(id, body)
        }
    }
}

/** 설정 API의 원격 호출 계약을 정의한다. */
interface SettingsRemoteDataSource {
    suspend fun logout(): NetworkResult<MessageEnvelopeDto>
    suspend fun deleteAccount(body: DeleteAccountRequestDto): NetworkResult<Unit>
    suspend fun updateTheme(body: ThemeRequestDto): NetworkResult<ThemeResponseDto>
    suspend fun getProfile(): NetworkResult<UserResponseDto>
    suspend fun updateProfile(body: MyPageUpdateRequestDto): NetworkResult<UserResponseDto>
    suspend fun getSettings(): NetworkResult<SettingMainResponseDto>
    suspend fun updateSettings(body: SettingUpdateRequestDto): NetworkResult<SettingMainResponseDto>
    suspend fun submitInquiry(body: ContentRequestDto): NetworkResult<MessageEnvelopeDto>
}

/** SettingsApiService 호출을 ApiExecutor를 통해 실행한다. */
class SettingsRemoteDataSourceImpl @Inject constructor(
    private val apiExecutor: ApiExecutor,
    private val settingsApiService: SettingsApiService,
) : SettingsRemoteDataSource {
    override suspend fun logout(): NetworkResult<MessageEnvelopeDto> = apiExecutor.execute {
        settingsApiService.logout()
    }

    /** 현재 비밀번호를 전송해 회원탈퇴를 요청하고 204 성공 응답을 공통 결과로 변환한다. */
    override suspend fun deleteAccount(body: DeleteAccountRequestDto): NetworkResult<Unit> {
        return apiExecutor.executeNoContent {
            settingsApiService.deleteAccount(body)
        }
    }

    override suspend fun updateTheme(body: ThemeRequestDto): NetworkResult<ThemeResponseDto> = apiExecutor.execute {
        settingsApiService.updateTheme(body)
    }

    override suspend fun getProfile(): NetworkResult<UserResponseDto> = apiExecutor.execute {
        settingsApiService.getMyPage()
    }

    override suspend fun updateProfile(body: MyPageUpdateRequestDto): NetworkResult<UserResponseDto> = apiExecutor.execute {
        settingsApiService.updateMyPage(body)
    }

    override suspend fun getSettings(): NetworkResult<SettingMainResponseDto> = apiExecutor.execute {
        settingsApiService.getSettings()
    }

    override suspend fun updateSettings(body: SettingUpdateRequestDto): NetworkResult<SettingMainResponseDto> = apiExecutor.execute {
        settingsApiService.updateSettings(body)
    }

    override suspend fun submitInquiry(body: ContentRequestDto): NetworkResult<MessageEnvelopeDto> = apiExecutor.execute {
        settingsApiService.submitInquiry(body)
    }
}
