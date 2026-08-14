package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/** 마이페이지 서버 상태를 수집하고 설정 화면 이동을 연결한다. */
@Composable
fun MyPageRoute(
    onNavigate: (HopesDestination) -> Unit,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    MyPageScreen(
        uiState = uiState.value,
        onEvent = { event ->
            when (event) {
                MyPageScreenEvent.AppSettingsClicked -> {
                    onNavigate(HopesDestination.AppSettings)
                }

                else -> viewModel.onEvent(event)
            }
        },
        onNavigate = onNavigate,
    )
}
