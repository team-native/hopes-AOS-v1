package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/** 문의하기 서버 상태를 화면에 연결한다. */
@Composable
fun ContactRoute(
    onNavigate: (HopesDestination) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ContactViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ContactScreen(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        onNavigate = onNavigate,
    )
}
