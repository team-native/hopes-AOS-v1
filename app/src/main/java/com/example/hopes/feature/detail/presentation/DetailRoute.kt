package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/** 서버 상태를 수집하고 상세 화면의 사용자 이벤트를 ViewModel·탐색에 연결한다. */
@Composable
fun DetailRoute(
    screenType: DetailScreenType,
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(screenType) {
        if (screenType == DetailScreenType.PersonalSettings) {
            viewModel.loadPersonalPrompt()
        }
    }

    val onEvent: (DetailScreenEvent) -> Unit = { event ->
        when (event) {
            DetailScreenEvent.BackClicked -> onBackClick()
            DetailScreenEvent.AppSettingsClicked -> Unit
            is DetailScreenEvent.PersonalPromptChanged -> viewModel.updatePersonalPrompt(event.value)
            DetailScreenEvent.PersonalPromptSaveClicked -> viewModel.savePersonalPrompt()
            is DetailScreenEvent.ContactEmailChanged -> viewModel.updateContactEmail(event.value)
            is DetailScreenEvent.ContactMessageChanged -> viewModel.updateContactMessage(event.value)
            DetailScreenEvent.ContactSendClicked -> viewModel.submitContact()
        }
    }

    when (screenType) {
        DetailScreenType.PersonalSettings -> {
            PersonalSettingsScreen(
                uiState = uiState.value,
                onEvent = onEvent,
                onNavigate = onNavigate,
            )
        }

        DetailScreenType.Contact -> {
            ContactScreen(
                uiState = uiState.value,
                onEvent = onEvent,
                onNavigate = onNavigate,
            )
        }
    }
}
