package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.navigation.HopesDestination
@Composable fun PersonalSettingsScreenContent(uiState: DetailUiState, onEvent: (DetailScreenEvent) -> Unit, onNavigate: (HopesDestination) -> Unit) { HopesScaffold(HopesDestination.Settings,onNavigate){ inner->Column(Modifier.fillMaxSize().padding(inner).padding(horizontal=AppSpacing.ScreenHorizontal,vertical=24.dp)){Text(stringResource(R.string.personal_settings));HopesSurfaceCard{Text(stringResource(R.string.personal_settings_subtitle));FigmaAuthTextField(uiState.personalPrompt,{onEvent(DetailScreenEvent.PersonalPromptChanged(it))},R.string.system_prompt_description,modifier=Modifier.fillMaxWidth());Text(stringResource(R.string.prompt_save),Modifier.fillMaxWidth())}}} }
