package com.example.hopes.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {
    @POST("api/signup") suspend fun signUp(@Body body: SignupRequestDto): Response<TokenResponseDto>
    @POST("api/login") suspend fun login(@Body body: LoginRequestDto): Response<TokenResponseDto>
    @POST("api/signup/email-verifications") suspend fun sendSignupVerification(@Body body: EmailRequestDto): Response<MessageEnvelopeDto>
    @POST("api/signup/email-verifications/confirm") suspend fun confirmSignupVerification(@Body body: EmailCodeRequestDto): Response<MessageEnvelopeDto>
    @POST("api/password/request") suspend fun requestPasswordReset(@Body body: EmailRequestDto): Response<MessageEnvelopeDto>
    @POST("api/password/reset") suspend fun resetPassword(@Body body: PasswordResetRequestDto): Response<MessageEnvelopeDto>
}
interface ChatApiService {
    @GET("api/main") suspend fun getMain(@Query("searchKeyword") keyword: String?, @Query("page") page: Int, @Query("size") size: Int): Response<MainResponseDto>
    @GET("api/chats/{id}") suspend fun getChat(@Path("id") id: Long, @Query("messagePage") page: Int, @Query("messageSize") size: Int): Response<ChatResponseDto>
    @POST("api/chats") suspend fun createChat(@Body body: CreateChatRequestDto): Response<ChatResponseDto>
    @POST("api/chats/{id}/messages") suspend fun sendMessage(@Path("id") id: Long, @Body body: SendMessageRequestDto): Response<ChatResponseDto>
}
interface SettingsApiService {
    @PATCH("api/general") suspend fun updateTheme(@Body body: ThemeRequestDto): Response<ThemeResponseDto>
    @GET("api/mypage") suspend fun getMyPage(): Response<UserResponseDto>
    @PATCH("api/mypage") suspend fun updateMyPage(@Body body: MyPageUpdateRequestDto): Response<UserResponseDto>
    @GET("api/setting/main") suspend fun getSettings(): Response<SettingMainResponseDto>
    @PATCH("api/setting") suspend fun updateSettings(@Body body: SettingUpdateRequestDto): Response<SettingMainResponseDto>
    @POST("api/setting/inquiry") suspend fun submitInquiry(@Body body: ContentRequestDto): Response<MessageEnvelopeDto>
    @POST("api/logout") suspend fun logout(): Response<MessageEnvelopeDto>
}
@Serializable data class TokenResponseDto(@SerialName("accessToken") val accessToken: String, @SerialName("tokenType") val tokenType: String)
@Serializable data class MessageEnvelopeDto(@SerialName("message") val message: String)
@Serializable data class EmailRequestDto(@SerialName("email") val email: String)
@Serializable data class EmailCodeRequestDto(@SerialName("email") val email: String, @SerialName("code") val code: String)
@Serializable data class LoginRequestDto(@SerialName("username") val username: String, @SerialName("password") val password: String)
@Serializable data class SignupRequestDto(@SerialName("email") val email: String, @SerialName("username") val username: String, @SerialName("password") val password: String, @SerialName("passwordConfirm") val passwordConfirm: String, @SerialName("verificationCode") val verificationCode: String, @SerialName("gender") val gender: String? = null, @SerialName("major") val major: String? = null, @SerialName("cohort") val cohort: Int? = null)
@Serializable data class PasswordResetRequestDto(@SerialName("email") val email: String, @SerialName("code") val code: String, @SerialName("password") val password: String, @SerialName("passwordConfirm") val passwordConfirm: String)
@Serializable data class ThemeRequestDto(@SerialName("theme") val theme: String)
@Serializable data class ThemeResponseDto(@SerialName("theme") val theme: String)
@Serializable data class ContentRequestDto(@SerialName("content") val content: String)
@Serializable data class CreateChatRequestDto(@SerialName("title") val title: String? = null)
@Serializable data class SendMessageRequestDto(@SerialName("content") val content: String)
@Serializable data class SettingUpdateRequestDto(@SerialName("customPrompt") val customPrompt: String? = null, @SerialName("deleteAllChats") val deleteAllChats: Boolean = false)
@Serializable data class MyPageUpdateRequestDto(@SerialName("username") val username: String? = null, @SerialName("nickname") val nickname: String? = null, @SerialName("profileInfo") val profileInfo: String? = null, @SerialName("profileImage") val profileImage: String? = null)
@Serializable data class MainResponseDto(@SerialName("chatList") val chatList: List<ChatSummaryDto>, @SerialName("newChat") val newChat: Boolean, @SerialName("searchKeyword") val searchKeyword: String? = null, @SerialName("page") val page: Int, @SerialName("size") val size: Int, @SerialName("hasNext") val hasNext: Boolean)
@Serializable data class ChatSummaryDto(@SerialName("id") val id: Long, @SerialName("title") val title: String, @SerialName("updatedAt") val updatedAt: String)
@Serializable data class ChatResponseDto(@SerialName("id") val id: Long, @SerialName("title") val title: String, @SerialName("messages") val messages: List<MessageDto>, @SerialName("messagePage") val messagePage: Int, @SerialName("messageSize") val messageSize: Int, @SerialName("hasMoreMessages") val hasMoreMessages: Boolean)
@Serializable data class MessageDto(@SerialName("id") val id: Long? = null, @SerialName("role") val role: String, @SerialName("content") val content: String, @SerialName("createdAt") val createdAt: String? = null)
@Serializable data class UserResponseDto(@SerialName("username") val username: String, @SerialName("email") val email: String, @SerialName("nickname") val nickname: String, @SerialName("profileInfo") val profileInfo: String, @SerialName("profileImage") val profileImage: String? = null, @SerialName("gender") val gender: String? = null, @SerialName("major") val major: String? = null, @SerialName("cohort") val cohort: Int? = null)
@Serializable data class SettingMainResponseDto(@SerialName("accountSetting") val accountSetting: UserResponseDto, @SerialName("theme") val theme: String, @SerialName("customPrompt") val customPrompt: String, @SerialName("logout") val logout: Boolean, @SerialName("inquiry") val inquiry: Boolean)
