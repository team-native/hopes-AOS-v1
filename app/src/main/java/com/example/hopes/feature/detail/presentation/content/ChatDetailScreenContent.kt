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
fun ChatDetailScreenContent(uiState: DetailUiState, onEvent: (DetailScreenEvent) -> Unit, onNavigate: (HopesDestination) -> Unit) {
    val conversation = uiState.conversation
    HopesScaffold(HopesDestination.Chat, onNavigate) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = AppSpacing.ScreenHorizontal)) {
            Text(conversation?.question ?: stringResource(R.string.chat_detail_title))
            Text(stringResource(R.string.chat_detail_answer))
            conversation?.replies?.forEach { Text(it) }
            OutlinedTextField(conversation?.replyDraft.orEmpty(), { onEvent(DetailScreenEvent.ReplyChanged(it)) }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.chat_additional_question)) })
            Button(onClick = { onEvent(DetailScreenEvent.ReplySubmitted) }) { Text(stringResource(R.string.chat_send)) }
        }
    }
}
