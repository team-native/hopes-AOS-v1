package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.hopes.navigation.DemoConversation
import com.example.hopes.navigation.HopesDestination

/** 검색 결과를 스크롤 가능한 목록으로 배치하는 지난 대화 화면이다. */
@Composable
fun HistoryScreenContent(
    searchQuery: String,
    conversations: List<DemoConversation>,
    onSearchQueryChange: (String) -> Unit,
    onQuestionClick: (String) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val defaultQuestions = stringArrayResource(R.array.history_recent_questions).toList()
    val questions = (conversations.map(DemoConversation::question) + defaultQuestions)
        .distinct()
        .filter { it.contains(searchQuery, ignoreCase = true) }

    HopesScaffold(
        selectedDestination = HopesDestination.History,
        onNavigate = onNavigate,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = AppSpacing.ScreenHorizontal,
                vertical = AppSpacing.Section,
            ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            item { Text(text = stringResource(R.string.history_title)) }
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillParentMaxWidth(),
                    label = { Text(text = stringResource(R.string.history_search)) },
                    singleLine = true,
                )
            }
            items(questions.size) { index ->
                HistoryItem(
                    question = questions[index],
                    onClick = { onQuestionClick(questions[index]) },
                )
            }
        }
    }
}
