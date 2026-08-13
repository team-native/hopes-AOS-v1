package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Arrangement
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
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.navigation.HopesDestination

@Composable
fun MyPageScreenContent(uiState: DetailUiState, onEvent: (DetailScreenEvent) -> Unit, onNavigate: (HopesDestination) -> Unit) {
    HopesScaffold(HopesDestination.Settings, onNavigate) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = AppSpacing.ScreenHorizontal), verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
            Text(stringResource(R.string.my_page))
            HopesSurfaceCard { Text(stringResource(R.string.my_page_account)); Text(stringResource(R.string.my_page_email)); Text(stringResource(R.string.my_page_major)) }
            HopesSurfaceCard { Text(stringResource(R.string.profile)); OutlinedTextField(uiState.profileName, { onEvent(DetailScreenEvent.ProfileNameChanged(it)) }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.name)) }); OutlinedTextField(uiState.profileIntroduction, { onEvent(DetailScreenEvent.ProfileIntroductionChanged(it)) }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.profile_personalization_label)) }); Button(onClick = { onEvent(DetailScreenEvent.ProfileSaveClicked) }) { Text(stringResource(if (uiState.isProfileSaved) R.string.saved else R.string.save)) } }
        }
    }
}
