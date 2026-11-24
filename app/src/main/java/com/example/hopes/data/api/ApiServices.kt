package com.example.hopes.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Multipart
import retrofit2.http.Part
import okhttp3.MultipartBody
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {
    @POST("api/signup")
    suspend fun signUp(
        @Body body: SignupRequestDto,
    ): Response<TokenResponseDto>

    @POST("api/login")
    suspend fun login(
        @Body body: LoginRequestDto,
    ): Response<TokenResponseDto>

    @POST("api/signup/email-verifications")
    suspend fun sendSignupVerification(
        @Body body: EmailRequestDto,
    ): Response<MessageEnvelopeDto>

    @POST("api/signup/email-verifications/confirm")
    suspend fun confirmSignupVerification(
        @Body body: EmailCodeRequestDto,
    ): Response<MessageEnvelopeDto>

    @POST("api/password/request")
    suspend fun requestPasswordReset(
        @Body body: EmailRequestDto,
    ): Response<MessageEnvelopeDto>

    @POST("api/password/reset")
    suspend fun resetPassword(
        @Body body: PasswordResetRequestDto,
    ): Response<MessageEnvelopeDto>
}

interface ChatApiService {
    @GET("api/main")
    suspend fun getMain(
        @Query("searchKeyword") keyword: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<MainResponseDto>

    @GET("api/chats/{id}")
    suspend fun getChat(
        @Path("id") id: Long,
        @Query("messagePage") page: Int,
        @Query("messageSize") size: Int,
    ): Response<ChatResponseDto>

    @POST("api/chats")
    suspend fun createChat(
        @Body body: CreateChatRequestDto,
    ): Response<ChatResponseDto>

    @POST("api/chats/{id}/messages")
    suspend fun sendMessage(
        @Path("id") id: Long,
        @Header("X-Hopes-Client") client: String,
        @Body body: SendMessageRequestDto,
    ): Response<ChatResponseDto>

    @DELETE("api/chats/{id}")
    suspend fun deleteChat(
        @Path("id") id: Long,
    ): Response<Unit>
}

interface AttachmentApiService {
    @Multipart
    @POST("api/attachments")
    suspend fun uploadAttachment(
        @Query("clientRequestId") clientRequestId: String?,
        @Part file: MultipartBody.Part,
    ): Response<AttachmentResponseDto>

    @DELETE("api/attachments/{attachmentId}")
    suspend fun deleteAttachment(@Path("attachmentId") attachmentId: Long): Response<Unit>
}

interface CampusApiService {
    @POST("api/campus/location-check")
    suspend fun checkLocation(@Body body: LocationCheckRequestDto): Response<LocationCheckResponseDto>
}

interface CommunityApiService {
    @GET("api/community/posts")
    suspend fun getPosts(@Query("boardId") boardId: Long?, @Query("keyword") keyword: String?, @Query("page") page: Int, @Query("size") size: Int, @Query("sort") sort: String): Response<CommunityPostPageDto>

    @POST("api/community/posts")
    suspend fun createPost(@Body body: CommunityPostRequestDto): Response<CommunityPostDto>

    @GET("api/community/posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Long): Response<CommunityPostDto>

    @PATCH("api/community/posts/{postId}")
    suspend fun updatePost(@Path("postId") postId: Long, @Body body: CommunityPostUpdateDto): Response<CommunityPostDto>

    @DELETE("api/community/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): Response<Unit>

    @GET("api/community/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Long, @Query("page") page: Int, @Query("size") size: Int): Response<CommunityCommentPageDto>

    @POST("api/community/posts/{postId}/comments")
    suspend fun createComment(@Path("postId") postId: Long, @Body body: CommunityCommentRequestDto): Response<CommunityCommentDto>

    @PATCH("api/community/comments/{commentId}")
    suspend fun updateComment(@Path("commentId") commentId: Long, @Body body: CommunityCommentRequestDto): Response<CommunityCommentDto>

    @DELETE("api/community/comments/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Long): Response<Unit>

    @PUT("api/community/posts/{postId}/like")
    suspend fun like(@Path("postId") postId: Long): Response<Unit>

    @DELETE("api/community/posts/{postId}/like")
    suspend fun unlike(@Path("postId") postId: Long): Response<Unit>

    @POST("api/community/reports")
    suspend fun report(@Body body: CommunityReportRequestDto): Response<Unit>

    @GET("api/community/boards")
    suspend fun getBoards(): Response<Map<String, List<CommunityBoardDto>>>

    @GET("api/community/me/posts")
    suspend fun getMyPosts(@Query("page") page: Int, @Query("size") size: Int): Response<CommunityPostPageDto>

    @GET("api/community/me/comments")
    suspend fun getMyComments(@Query("page") page: Int, @Query("size") size: Int): Response<CommunityMyCommentPageDto>
}

interface ChatAssistApiService {
    @GET("api/departments") suspend fun getDepartments(): Response<Map<String, List<ChatCategoryDto>>>
    @GET("api/specialties") suspend fun getSpecialties(): Response<Map<String, List<ChatCategoryDto>>>
    @GET("api/chat-preferences") suspend fun getChatPreferences(): Response<ChatPreferenceDto>
    @PATCH("api/chat-preferences") suspend fun updateChatPreferences(@Body body: ChatPreferenceUpdateDto): Response<ChatPreferenceDto>
    @GET("api/privacy/recommendation-consent") suspend fun getRecommendationConsent(): Response<RecommendationConsentDto>
    @PUT("api/privacy/recommendation-consent") suspend fun updateRecommendationConsent(@Body body: RecommendationConsentRequestDto): Response<RecommendationConsentDto>
    @GET("api/question-recommendations")
    suspend fun getRecommendations(
        @Query("departmentId") departmentId: Long?,
        @Query("specialtyId") specialtyId: Long?,
        @Query("limit") limit: Int,
        @Query("refreshToken") refreshToken: String?,
        @Query("excludeIds") excludeIds: List<Long>,
        @Query("personalized") personalized: Boolean,
        @Query("specialtyIds") specialtyIds: List<Long>?,
    ): Response<QuestionRecommendationResultDto>
}

@Serializable data class CommunityPostPageDto(@SerialName("items") val items: List<CommunityPostPreviewDto>, @SerialName("page") val page: Int, @SerialName("size") val size: Int, @SerialName("hasNext") val hasNext: Boolean)
@Serializable data class LocationCheckRequestDto(@SerialName("latitude") val latitude: Double, @SerialName("longitude") val longitude: Double, @SerialName("horizontalAccuracyMeters") val horizontalAccuracyMeters: Double, @SerialName("measuredAt") val measuredAt: String, @SerialName("clientRequestId") val clientRequestId: String? = null)
@Serializable data class LocationCheckResponseDto(@SerialName("status") val status: String, @SerialName("accuracyMeters") val accuracyMeters: Double, @SerialName("message") val message: String, @SerialName("floorPlansAvailable") val floorPlansAvailable: Boolean, @SerialName("matchedBuilding") val matchedBuilding: Long? = null)
@Serializable data class CommunityCommentPageDto(@SerialName("items") val items: List<CommunityCommentDto>, @SerialName("page") val page: Int, @SerialName("size") val size: Int, @SerialName("hasNext") val hasNext: Boolean)
@Serializable data class CommunityMyCommentPageDto(@SerialName("items") val items: List<CommunityMyCommentDto>, @SerialName("page") val page: Int, @SerialName("size") val size: Int, @SerialName("hasNext") val hasNext: Boolean)
@Serializable data class CommunityPostPreviewDto(@SerialName("id") val id: Long, @SerialName("boardId") val boardId: Long, @SerialName("title") val title: String, @SerialName("preview") val preview: String, @SerialName("createdAt") val createdAt: String, @SerialName("updatedAt") val updatedAt: String, @SerialName("likeCount") val likeCount: Long, @SerialName("commentCount") val commentCount: Long, @SerialName("likedByMe") val likedByMe: Boolean, @SerialName("isMine") val isMine: Boolean, @SerialName("authorLabel") val authorLabel: String, @SerialName("viewCount") val viewCount: Long, @SerialName("popularityScore") val popularityScore: Long)
@Serializable data class CommunityPostDto(@SerialName("id") val id: Long, @SerialName("boardId") val boardId: Long, @SerialName("title") val title: String, @SerialName("content") val content: String, @SerialName("createdAt") val createdAt: String, @SerialName("updatedAt") val updatedAt: String, @SerialName("likeCount") val likeCount: Long, @SerialName("commentCount") val commentCount: Long, @SerialName("likedByMe") val likedByMe: Boolean, @SerialName("isMine") val isMine: Boolean, @SerialName("authorLabel") val authorLabel: String, @SerialName("viewCount") val viewCount: Long, @SerialName("popularityScore") val popularityScore: Long)
@Serializable data class CommunityCommentDto(@SerialName("id") val id: Long, @SerialName("content") val content: String, @SerialName("createdAt") val createdAt: String, @SerialName("updatedAt") val updatedAt: String, @SerialName("isMine") val isMine: Boolean, @SerialName("authorLabel") val authorLabel: String)
@Serializable data class CommunityMyCommentDto(@SerialName("id") val id: Long, @SerialName("postId") val postId: Long, @SerialName("postTitle") val postTitle: String? = null, @SerialName("content") val content: String, @SerialName("createdAt") val createdAt: String, @SerialName("isMine") val isMine: Boolean)
@Serializable data class CommunityBoardDto(@SerialName("id") val id: Long, @SerialName("name") val name: String, @SerialName("cohort") val cohort: Int? = null, @SerialName("sortOrder") val sortOrder: Int)
@Serializable data class CommunityPostRequestDto(@SerialName("boardId") val boardId: Long, @SerialName("title") val title: String, @SerialName("content") val content: String, @SerialName("clientRequestId") val clientRequestId: String? = null)
@Serializable data class CommunityPostUpdateDto(@SerialName("title") val title: String? = null, @SerialName("content") val content: String? = null)
@Serializable data class CommunityCommentRequestDto(@SerialName("content") val content: String, @SerialName("clientRequestId") val clientRequestId: String? = null)
@Serializable data class CommunityReportRequestDto(@SerialName("targetType") val targetType: String, @SerialName("targetId") val targetId: Long, @SerialName("reasonCode") val reasonCode: String, @SerialName("detail") val detail: String? = null)

@Serializable data class ChatCategoryDto(@SerialName("id") val id: Long, @SerialName("name") val name: String, @SerialName("description") val description: String, @SerialName("sortOrder") val sortOrder: Int, @SerialName("isActive") val isActive: Boolean)
@Serializable data class ChatPreferenceDto(@SerialName("departmentId") val departmentId: Long? = null, @SerialName("specialtyId") val specialtyId: Long? = null, @SerialName("invalidSelection") val invalidSelection: Boolean, @SerialName("specialtyIds") val specialtyIds: List<Long> = emptyList())
@Serializable data class ChatPreferenceUpdateDto(@SerialName("departmentId") val departmentId: Long? = null, @SerialName("specialtyId") val specialtyId: Long? = null, @SerialName("specialtyIds") val specialtyIds: List<Long> = emptyList())
@Serializable data class RecommendationConsentDto(@SerialName("enabled") val enabled: Boolean, @SerialName("policyVersion") val policyVersion: String, @SerialName("updatedAt") val updatedAt: String? = null)
@Serializable data class RecommendationConsentRequestDto(@SerialName("enabled") val enabled: Boolean, @SerialName("policyVersion") val policyVersion: String)
@Serializable data class QuestionRecommendationDto(@SerialName("id") val id: Long, @SerialName("content") val content: String, @SerialName("departmentId") val departmentId: Long? = null, @SerialName("specialtyId") val specialtyId: Long? = null)
@Serializable data class QuestionRecommendationResultDto(@SerialName("items") val items: List<QuestionRecommendationDto>, @SerialName("source") val source: String, @SerialName("fallbackUsed") val fallbackUsed: Boolean, @SerialName("nextRefreshToken") val nextRefreshToken: String)

interface SettingsApiService {
    @PATCH("api/general")
    suspend fun updateTheme(
        @Body body: ThemeRequestDto,
    ): Response<ThemeResponseDto>

    @GET("api/mypage")
    suspend fun getMyPage(): Response<UserResponseDto>

    @PATCH("api/mypage")
    suspend fun updateMyPage(
        @Body body: MyPageUpdateRequestDto,
    ): Response<UserResponseDto>

    @GET("api/setting/main")
    suspend fun getSettings(): Response<SettingMainResponseDto>

    @PATCH("api/setting")
    suspend fun updateSettings(
        @Body body: SettingUpdateRequestDto,
    ): Response<SettingMainResponseDto>

    @POST("api/setting/inquiry")
    suspend fun submitInquiry(
        @Body body: InquiryRequestDto,
    ): Response<MessageEnvelopeDto>

    @POST("api/logout")
    suspend fun logout(): Response<MessageEnvelopeDto>

    @HTTP(method = "DELETE", path = "api/account", hasBody = true)
    suspend fun deleteAccount(
        @Body body: DeleteAccountRequestDto,
    ): Response<Unit>
}

@Serializable data class TokenResponseDto(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("tokenType") val tokenType: String,
    @SerialName("message") val message: String? = null,
)

@Serializable data class MessageEnvelopeDto(
    @SerialName("message") val message: String,
)

@Serializable data class DeleteAccountRequestDto(
    @SerialName("password") val password: String,
)

@Serializable data class EmailRequestDto(
    @SerialName("email") val email: String,
)

@Serializable data class EmailCodeRequestDto(
    @SerialName("email") val email: String,
    @SerialName("code") val code: String,
)

@Serializable data class LoginRequestDto(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)

@Serializable data class SignupRequestDto(
    @SerialName("email") val email: String,
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("passwordConfirm") val passwordConfirm: String,
    @SerialName("verificationCode") val verificationCode: String,
    @SerialName("gender") val gender: String? = null,
    @SerialName("major") val major: String? = null,
    @SerialName("cohort") val cohort: Int? = null,
)

@Serializable data class PasswordResetRequestDto(
    @SerialName("email") val email: String,
    @SerialName("code") val code: String,
    @SerialName("newPassword") val newPassword: String,
    @SerialName("passwordConfirm") val passwordConfirm: String? = null,
)

@Serializable data class ThemeRequestDto(
    @SerialName("theme") val theme: String,
)

@Serializable data class ThemeResponseDto(
    @SerialName("theme") val theme: String,
)

@Serializable data class InquiryRequestDto(
    @SerialName("content") val content: String,
    @SerialName("replyEmail") val replyEmail: String? = null,
)

@Serializable data class CreateChatRequestDto(
    @SerialName("title") val title: String? = null,
)

@Serializable data class SendMessageRequestDto(
    @SerialName("content") val content: String,
    @SerialName("attachmentIds") val attachmentIds: List<Long> = emptyList(),
    @SerialName("departmentId") val departmentId: Long? = null,
    @SerialName("specialtyId") val specialtyId: Long? = null,
    @SerialName("clientRequestId") val clientRequestId: String? = null,
    @SerialName("specialtyIds") val specialtyIds: List<Long>? = null,
    @SerialName("destinationPlaceId") val destinationPlaceId: String? = null,
    @SerialName("navigationOrigin") val navigationOrigin: String? = null,
    @SerialName("elevatorOnly") val elevatorOnly: Boolean = false,
)

@Serializable data class SettingUpdateRequestDto(
    @SerialName("customPrompt") val customPrompt: String? = null,
    @SerialName("deleteAllChats") val deleteAllChats: Boolean = false,
)

@Serializable data class MyPageUpdateRequestDto(
    @SerialName("username") val username: String? = null,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("profileInfo") val profileInfo: String? = null,
    @SerialName("profileImage") val profileImage: String? = null,
)

@Serializable data class MainResponseDto(
    @SerialName("chatList") val chatList: List<ChatSummaryDto>,
    @SerialName("newChat") val newChat: Boolean,
    @SerialName("searchKeyword") val searchKeyword: String? = null,
    @SerialName("page") val page: Int,
    @SerialName("size") val size: Int,
    @SerialName("hasNext") val hasNext: Boolean,
)

@Serializable data class ChatSummaryDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("updatedAt") val updatedAt: String,
)

@Serializable data class ChatResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("messages") val messages: List<MessageDto>,
    @SerialName("messagePage") val messagePage: Int,
    @SerialName("messageSize") val messageSize: Int,
    @SerialName("hasMoreMessages") val hasMoreMessages: Boolean,
)

@Serializable data class MessageDto(
    @SerialName("id") val id: Long? = null,
    @SerialName("role") val role: String,
    @SerialName("content") val content: String,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("attachments") val attachments: List<AttachmentResponseDto> = emptyList(),
    @SerialName("analysisStatus") val analysisStatus: String? = null,
    @SerialName("contentFormat") val contentFormat: String = "TEXT",
    @SerialName("location") val location: kotlinx.serialization.json.JsonElement? = null,
)

@Serializable data class AttachmentResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("fileName") val fileName: String,
    @SerialName("mimeType") val mimeType: String,
    @SerialName("sizeBytes") val sizeBytes: Long,
    @SerialName("status") val status: String,
    @SerialName("previewUrl") val previewUrl: String? = null,
    @SerialName("expiresAt") val expiresAt: String,
)

@Serializable data class UserResponseDto(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("profileInfo") val profileInfo: String,
    @SerialName("profileImage") val profileImage: String? = null,
    @SerialName("gender") val gender: String? = null,
    @SerialName("major") val major: String? = null,
    @SerialName("cohort") val cohort: Int? = null,
)

@Serializable data class SettingMainResponseDto(
    @SerialName("accountSetting") val accountSetting: UserResponseDto,
    @SerialName("theme") val theme: String,
    @SerialName("customPrompt") val customPrompt: String,
    @SerialName("logout") val logout: Boolean,
    @SerialName("inquiry") val inquiry: Boolean,
)
