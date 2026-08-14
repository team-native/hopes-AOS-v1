package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.history.presentation.HistoryScreenEvent
import com.example.hopes.feature.history.presentation.HistoryUiState
import com.example.hopes.navigation.HopesDestination

/** 서버에서 받은 지난 대화 목록과 검색 입력을 표시한다. */
@Composable
fun HistoryScreenContent(
    uiState: HistoryUiState,
    onEvent: (HistoryScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.History,
        onNavigate = onNavigate,
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 76.dp)) {
            Text(text = stringResource(R.string.history_title), style = MaterialTheme.typography.headlineMedium)
            Text(text = stringResource(R.string.history_description), color = MaterialTheme.colorScheme.onSurfaceVariant)
            BasicTextField(
                value = uiState.searchQuery,
                onValueChange = { onEvent(HistoryScreenEvent.SearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                singleLine = true,
            )
            when {
                uiState.isLoading -> HistoryStateText(R.string.history_loading)
                uiState.isError -> HistoryStateText(R.string.history_error) { onEvent(HistoryScreenEvent.RetryClicked) }
                uiState.chats.isEmpty() -> HistoryStateText(R.string.history_empty)
                else -> uiState.chats.forEach { chat ->
                    Text(
                        text = chat.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .clickable { onEvent(HistoryScreenEvent.ChatClicked(chat.id)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryStateText(
    textResId: Int,
    onClick: (() -> Unit)? = null,
) {
    Text(
        text = stringResource(textResId),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick)),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
