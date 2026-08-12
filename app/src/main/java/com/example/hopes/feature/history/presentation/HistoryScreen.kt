package com.example.hopes.feature.history.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.history.presentation.content.HistoryScreenContent
import com.example.hopes.navigation.HopesDestination

/** 기록 목록 콘텐츠를 화면 단위로 제공한다. */
@Composable
fun HistoryScreen(onNavigate: (HopesDestination) -> Unit) {
    HistoryScreenContent(onNavigate = onNavigate)
}
