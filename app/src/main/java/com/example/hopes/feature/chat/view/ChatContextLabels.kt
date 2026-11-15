package com.example.hopes.feature.chat.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bed
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.example.hopes.feature.chat.viewmodel.ChatQuestionTopic
import com.teamnative.hopes.R

@Composable
fun ChatDepartment.label(): String =
    stringResource(
        when (this) {
            ChatDepartment.Software -> R.string.chat_department_software
            ChatDepartment.SmartIot -> R.string.chat_department_smart_iot
            ChatDepartment.ArtificialIntelligence -> R.string.chat_department_ai
        },
    )

@Composable
fun ChatMajor.label(): String = displayName ?: stringResource(R.string.chat_major_other)

/** 추천 질문 주제를 화면 문구로 바꾼다. 전공 공부 질문은 고른 전공 이름을 넣는다. */
@Composable
fun ChatQuestionTopic.question(major: ChatMajor?): String =
    when (this) {
        ChatQuestionTopic.Dormitory -> stringResource(R.string.chat_suggestion_dormitory)
        ChatQuestionTopic.Admission -> stringResource(R.string.chat_suggestion_admission)
        ChatQuestionTopic.MajorStudy -> stringResource(R.string.chat_suggestion_major_study, major?.label().orEmpty())
        ChatQuestionTopic.Advice -> stringResource(R.string.chat_suggestion_advice)
    }

fun ChatQuestionTopic.icon(): ImageVector =
    when (this) {
        ChatQuestionTopic.Dormitory -> Icons.Outlined.Bed
        ChatQuestionTopic.Admission -> Icons.Outlined.Description
        ChatQuestionTopic.MajorStudy -> Icons.Outlined.Code
        ChatQuestionTopic.Advice -> Icons.Outlined.FavoriteBorder
    }
