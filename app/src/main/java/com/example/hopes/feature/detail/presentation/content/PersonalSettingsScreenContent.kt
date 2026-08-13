package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.navigation.HopesDestination

@Composable
fun PersonalSettingsScreenContent(uiState: DetailUiState, onEvent: (DetailScreenEvent) -> Unit, onNavigate: (HopesDestination) -> Unit) {
    HopesScaffold(HopesDestination.Settings, onNavigate) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = AppSpacing.ScreenHorizontal)) {
            Text(stringResource(R.string.personal_settings))
            OutlinedTextField(uiState.personalPrompt, { onEvent(DetailScreenEvent.PersonalPromptChanged(it)) }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.system_prompt_description)) })
            Button(onClick = { onEvent(DetailScreenEvent.PersonalPromptSaveClicked) }) { Text(stringResource(if (uiState.isPromptSaved) R.string.saved else R.string.prompt_save)) }
        }
    }
}
