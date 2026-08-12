package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.navigation.HopesDestination

/** 피그마 08 지난 대화 프레임의 검색·기간별 목록을 표시한다. */
@Composable
fun HistoryScreenContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onQuestionClick: () -> Unit,
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
            onQuestionClick = onQuestionClick,
        )
    }
}

@Composable
private fun HistoryTitle() {
    Text(
        text = stringResource(R.string.history_title),
        modifier = Modifier.offset(x = 24.dp, y = 76.dp),
        style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp),
    )
    Text(
        text = stringResource(R.string.history_description),
        modifier = Modifier.offset(x = 24.dp, y = 111.dp).width(276.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
    )
}

@Composable
private fun HistoryNewChatButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .offset(x = 24.dp, y = 144.dp)
            .width(354.dp)
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "+  ${stringResource(R.string.new_chat)} 시작",
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
            .offset(x = 24.dp, y = 208.dp)
            .width(354.dp)
            .height(41.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.history_search),
                modifier = Modifier.offset(x = 20.dp, y = 13.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 14.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .offset(x = 20.dp, y = 8.dp)
                .width(288.dp)
                .height(28.dp),
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            ),
        )
        Text(
            text = "⌕",
            modifier = Modifier.offset(x = 326.dp, y = 10.dp),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun HistoryQuestionGroups(
    searchQuery: String,
    onQuestionClick: () -> Unit,
) {
    val recentQuestions = listOf(
        "기숙사 하루 일과가 어떻게 돼?",
        "전공 선택은 어떻게 하는 게 좋...",
        "여기랑 대덕중에 누가 더 좋음",
    )
    val previousQuestions = listOf(
        "입학하려면 뭘 준비해야 해?",
        "과랑 전공이랑 뭐가 다름",
        "후배한테 해주고 싶은 조언 있어?",
        "가장 예쁜 사람 누구?",
    )
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
        titleTop = 482,
        questions = previousQuestions,
        itemStartTop = 524,
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
    onQuestionClick: () -> Unit,
) {
    Text(
        text = title,
        modifier = Modifier.offset(x = 24.dp, y = titleTop.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
    )
    questions
        .filter { it.contains(searchQuery, ignoreCase = true) }
        .forEachIndexed { index, question ->
            Text(
                text = question,
                modifier = Modifier
                    .offset(x = 24.dp, y = (itemStartTop + (index * 46)).dp)
                    .width(280.dp)
                    .clickable(onClick = onQuestionClick),
                color = if (titleTop == 282 && index == 0) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 18.sp),
            )
        }
}
