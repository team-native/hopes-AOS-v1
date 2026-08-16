package com.example.hopes.data.mapper

import com.example.hopes.data.api.ChatResponseDto
import com.example.hopes.data.api.MainResponseDto
import com.example.hopes.data.api.MessageDto
import com.example.hopes.data.api.MyPageUpdateRequestDto
import com.example.hopes.data.api.PasswordResetRequestDto
import com.example.hopes.data.api.SettingMainResponseDto
import com.example.hopes.data.api.SignupRequestDto
import com.example.hopes.data.api.TokenResponseDto
import com.example.hopes.data.api.UserResponseDto
import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatMessage
import com.example.hopes.domain.model.ChatMessageRole
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.model.PasswordResetRequest
import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.model.UserSettings

fun TokenResponseDto.toDomain(): AuthToken = AuthToken(accessToken, tokenType)

fun MainResponseDto.toDomain(): ChatPage = ChatPage(
    chats = chatList.map { summary ->
        com.example.hopes.domain.model.ChatSummary(summary.id, summary.title, summary.updatedAt)
    },
    hasNextPage = hasNext,
)

fun ChatResponseDto.toDomain(): Chat = Chat(
    id = id,
    title = title,
    messages = messages.map(MessageDto::toDomain),
    hasMoreMessages = hasMoreMessages,
)

fun MessageDto.toDomain(): ChatMessage = ChatMessage(
    id = id,
    role = role.toChatMessageRole(),
    content = content,
    createdAt = createdAt,
)

private fun String.toChatMessageRole(): ChatMessageRole {
    return when (this) {
        "USER" -> ChatMessageRole.User
        "ASSISTANT" -> ChatMessageRole.Assistant
        else -> ChatMessageRole.Unknown
    }
}

fun UserResponseDto.toDomain(): UserProfile = UserProfile(
    username = username,
    email = email,
    nickname = nickname,
    profileInfo = profileInfo,
    profileImage = profileImage,
    major = major,
)

fun SettingMainResponseDto.toDomain(): UserSettings = UserSettings(
    profile = accountSetting.toDomain(),
    theme = theme,
    customPrompt = customPrompt,
)

fun SignUpRequest.toDto(): SignupRequestDto = SignupRequestDto(
    email = email,
    username = username,
    password = password,
    passwordConfirm = passwordConfirm,
    verificationCode = verificationCode,
    gender = gender,
    major = major,
    cohort = cohort,
)

fun PasswordResetRequest.toDto(): PasswordResetRequestDto = PasswordResetRequestDto(
    email = email,
    code = code,
    password = password,
    passwordConfirm = passwordConfirm,
)

fun ProfileUpdateRequest.toDto(): MyPageUpdateRequestDto = MyPageUpdateRequestDto(
    username = username,
    nickname = nickname,
    profileInfo = profileInfo,
    profileImage = profileImage,
)
