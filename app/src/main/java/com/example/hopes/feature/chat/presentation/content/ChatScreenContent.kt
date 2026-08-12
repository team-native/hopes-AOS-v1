package com.example.hopes.feature.chat.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.chat.presentation.component.ChatAnswerCard
import com.example.hopes.feature.chat.presentation.component.ChatComposer
import com.example.hopes.feature.chat.presentation.component.ChatHeader
import com.example.hopes.feature.chat.presentation.component.ChatSuggestions
import com.example.hopes.navigation.HopesDestination

/** 채팅 헤더, 추천 질문, 답변, 입력창을 하나의 화면으로 조합한다. */
@Composable
fun ChatScreenContent(
    questionText: String,
    submittedQuestion: String?,
    onQuestionChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HopesScaffold(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = AppSpacing.ScreenHorizontal,
                    vertical = AppSpacing.ScreenVertical,
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            ChatHeader()
            ChatSuggestions(onSuggestionClick = onSuggestionClick)
            submittedQuestion?.let { question ->
                ChatAnswerCard(question = question)
            }
            ChatComposer(
                questionText = questionText,
                onQuestionChange = onQuestionChange,
                onSubmitClick = onSubmitClick,
            )
        }
    }
}
