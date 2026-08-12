package com.example.hopes.feature.history.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.navigation.HopesDestination

/** 기록 화면에 앱 탐색 콜백을 제공한다. */
@Composable
fun HistoryRoute(onNavigate: (HopesDestination) -> Unit) {
    HistoryScreen(onNavigate = onNavigate)
}
