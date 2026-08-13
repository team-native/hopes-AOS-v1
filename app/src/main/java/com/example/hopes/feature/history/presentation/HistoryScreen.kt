package com.example.hopes.feature.history.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.history.presentation.content.HistoryScreenContent
import com.example.hopes.navigation.DemoConversation
import com.example.hopes.navigation.HopesDestination

/** 기록 목록 콘텐츠를 화면 단위로 제공한다. */
@Composable
fun HistoryScreen(
    searchQuery: String,
    conversations: List<DemoConversation>,
    onSearchQueryChange: (String) -> Unit,
    onQuestionClick: (String) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HistoryScreenContent(
        searchQuery = searchQuery,
        conversations = conversations,
        onSearchQueryChange = onSearchQueryChange,
        onQuestionClick = onQuestionClick,
        onNavigate = onNavigate,
    )
}
