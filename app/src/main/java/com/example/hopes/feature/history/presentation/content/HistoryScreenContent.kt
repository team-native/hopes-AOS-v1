package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.navigation.DemoConversation
import com.example.hopes.navigation.HopesDestination

/** 피그마 08 지난 대화 프레임의 검색·기간별 목록을 표시한다. */
@Composable
fun HistoryScreenContent(
    searchQuery: String,
    conversations: List<DemoConversation>,
    onSearchQueryChange: (String) -> Unit,
    onQuestionClick: (String) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.History,
        onNavigate = onNavigate,
    ) {
        HistoryTitle()
        HistoryNewChatButton(onClick = { onNavigate(HopesDestination.Chat) })
        HistorySearchField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
        )
        HistoryQuestionGroups(
            searchQuery = searchQuery,
            conversations = conversations,
            onQuestionClick = onQuestionClick,
        )
    }
}

@Composable
private fun HistoryTitle() {
    Text(
        text = stringResource(R.string.history_title),
        modifier = Modifier.padding(start = 24.dp, top = 76.dp),
        style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp),
    )
    Text(
        text = stringResource(R.string.history_description),
        modifier = Modifier.padding(start = 24.dp, top = 111.dp).width(276.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
    )
}

@Composable
private fun HistoryNewChatButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(start = 24.dp, top = 144.dp)
            .width(354.dp)
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                R.string.history_start_new_chat,
                stringResource(R.string.new_chat),
            ),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun HistorySearchField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(start = 24.dp, top = 208.dp)
            .width(354.dp)
            .height(41.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.history_search),
                modifier = Modifier.padding(start = 20.dp, top = 13.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 14.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 20.dp, top = 8.dp)
                .width(288.dp)
                .height(28.dp),
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            ),
        )
        Text(
            text = stringResource(R.string.history_search_symbol),
            modifier = Modifier.padding(start = 329.dp, top = 12.dp),
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun HistoryQuestionGroups(
    searchQuery: String,
    conversations: List<DemoConversation>,
    onQuestionClick: (String) -> Unit,
) {
    val figmaRecentQuestions = stringArrayResource(R.array.history_recent_questions).toList()
    // 채팅에서 새로 만든 로컬 대화를 피그마 기본 목록 위에 반영한다.
    val recentQuestions = (conversations.map(DemoConversation::question) + figmaRecentQuestions)
        .distinct()
    val previousQuestions = stringArrayResource(R.array.history_previous_questions).toList()
    val visibleRecentQuestionCount = recentQuestions.count { question ->
        question.contains(searchQuery, ignoreCase = true)
    }
    // 기본 Figma 3개 항목에서는 y=482를 그대로 유지하고, 새 대화가 늘어난 만큼만 다음 그룹을 민다.
    val previousGroupShift = (visibleRecentQuestionCount - figmaRecentQuestions.size)
        .coerceAtLeast(0) * 46
    HistoryGroup(
        title = stringResource(R.string.history_recent),
        titleTop = 282,
        questions = recentQuestions,
        itemStartTop = 324,
        searchQuery = searchQuery,
        onQuestionClick = onQuestionClick,
    )
    HistoryGroup(
        title = stringResource(R.string.history_previous),
        titleTop = 482 + previousGroupShift,
        questions = previousQuestions,
        itemStartTop = 524 + previousGroupShift,
        searchQuery = searchQuery,
        onQuestionClick = onQuestionClick,
    )
}

@Composable
private fun HistoryGroup(
    title: String,
    titleTop: Int,
    questions: List<String>,
    itemStartTop: Int,
    searchQuery: String,
    onQuestionClick: (String) -> Unit,
) {
    Text(
        text = title,
        modifier = Modifier.padding(start = 24.dp, top = titleTop.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
    )
    questions
        .filter { it.contains(searchQuery, ignoreCase = true) }
        .forEachIndexed { index, question ->
            Text(
                text = question,
                modifier = Modifier
                    .padding(start = 24.dp, top = (itemStartTop + (index * 46)).dp)
                    .width(280.dp)
                    .clickable { onQuestionClick(question) },
                color = if (titleTop == 282 && index == 0) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 18.sp),
            )
        }
}
