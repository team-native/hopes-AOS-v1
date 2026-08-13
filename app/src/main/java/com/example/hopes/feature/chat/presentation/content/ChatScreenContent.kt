package com.example.hopes.feature.chat.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.chat.presentation.component.ChatComposer
import com.example.hopes.feature.chat.presentation.component.ChatHeader
import com.example.hopes.feature.chat.presentation.component.ChatSuggestions
import com.example.hopes.navigation.HopesDestination

/** 추천 질문과 질문 입력 영역을 기기 크기에 맞춰 세로 흐름으로 배치한다. */
@Composable
fun ChatScreenContent(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNewChatClick: () -> Unit,
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
                .padding(horizontal = AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            ChatHeader(onNewChatClick = onNewChatClick)
            ChatSuggestions(onSuggestionClick = onSuggestionClick)
            ChatComposer(
                questionText = questionText,
                onQuestionChange = onQuestionChange,
                onSubmitClick = onSubmitClick,
            )
        }
    }
}
