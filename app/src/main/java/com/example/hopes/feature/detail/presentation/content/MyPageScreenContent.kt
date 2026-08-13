package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Arrangement
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

@Composable fun MyPageScreenContent(uiState: DetailUiState, onEvent: (DetailScreenEvent) -> Unit, onNavigate: (HopesDestination) -> Unit) { HopesScaffold(HopesDestination.Settings, onNavigate) { inner -> Column(Modifier.fillMaxSize().padding(inner).padding(horizontal = AppSpacing.ScreenHorizontal, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { Text(stringResource(R.string.my_page)); HopesSurfaceCard { Text(stringResource(R.string.my_page_account)); Text(stringResource(R.string.my_page_email)); Text(stringResource(R.string.my_page_major)) }; HopesSurfaceCard { Text(stringResource(R.string.profile)); FigmaAuthTextField(uiState.profileName, { onEvent(DetailScreenEvent.ProfileNameChanged(it)) }, R.string.name, modifier = Modifier.fillMaxWidth()); FigmaAuthTextField(uiState.profileIntroduction, { onEvent(DetailScreenEvent.ProfileIntroductionChanged(it)) }, R.string.profile_personalization_label, modifier = Modifier.fillMaxWidth()); Text(stringResource(R.string.save), Modifier.fillMaxWidth()) } } } }
