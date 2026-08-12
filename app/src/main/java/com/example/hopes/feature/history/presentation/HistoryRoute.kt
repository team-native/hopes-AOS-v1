package com.example.hopes.feature.history.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.hopes.navigation.HopesDestination

/** 기록 화면에 앱 탐색 콜백을 제공한다. */
@Composable
fun HistoryRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToChatDetail: () -> Unit,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    HistoryScreen(
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onQuestionClick = onNavigateToChatDetail,
        onNavigate = onNavigate,
    )
}
