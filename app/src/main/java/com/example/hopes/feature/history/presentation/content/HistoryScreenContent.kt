package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.history.presentation.component.HistoryItem
import com.example.hopes.navigation.HopesDestination

/** 지난 질문을 카드 목록으로 조합한다. */
@Composable
fun HistoryScreenContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onQuestionClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HopesScaffold(
        selectedDestination = HopesDestination.History,
        onNavigate = onNavigate,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = AppSpacing.ScreenHorizontal,
                    vertical = AppSpacing.ScreenVertical,
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            Text(
                text = stringResource(R.string.history_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.history_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.history_search)) },
            )
            stringArrayResource(R.array.history_questions)
                .filter { question -> question.contains(searchQuery, ignoreCase = true) }
                .forEach { question ->
                    HistoryItem(question = question, onClick = onQuestionClick)
                }
        }
    }
}
