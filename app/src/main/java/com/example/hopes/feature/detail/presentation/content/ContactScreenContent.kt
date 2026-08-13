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
fun ContactScreenContent(uiState: DetailUiState, onEvent: (DetailScreenEvent) -> Unit, onNavigate: (HopesDestination) -> Unit) {
    HopesScaffold(HopesDestination.Settings, onNavigate) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = AppSpacing.ScreenHorizontal)) {
            Text(stringResource(R.string.contact))
            OutlinedTextField(uiState.contactEmail, { onEvent(DetailScreenEvent.ContactEmailChanged(it)) }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) })
            OutlinedTextField(uiState.contactMessage, { onEvent(DetailScreenEvent.ContactMessageChanged(it)) }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.contact_content)) })
            Button(onClick = { onEvent(DetailScreenEvent.ContactSendClicked) }) { Text(stringResource(if (uiState.isContactSent) R.string.sent else R.string.send_contact)) }
        }
    }
}
