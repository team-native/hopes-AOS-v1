package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/** 개인 설정 서버 상태를 화면에 연결한다. */
@Composable
fun PersonalSettingsRoute(
    onNavigate: (HopesDestination) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PersonalSettingsViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PersonalSettingsScreen(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onNavigate = onNavigate,
    )
}
