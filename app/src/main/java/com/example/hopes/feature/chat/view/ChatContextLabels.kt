package com.example.hopes.feature.chat.view

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
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

/** 추천 질문 카드 왼쪽의 피그마 아이콘(res/drawable/ic_topic_*)이다. */
@DrawableRes
fun ChatQuestionTopic.icon(): Int =
    when (this) {
        ChatQuestionTopic.Dormitory -> R.drawable.ic_topic_dormitory
        ChatQuestionTopic.Admission -> R.drawable.ic_topic_admission
        ChatQuestionTopic.MajorStudy -> R.drawable.ic_topic_major_study
        ChatQuestionTopic.Advice -> R.drawable.ic_topic_advice
    }
