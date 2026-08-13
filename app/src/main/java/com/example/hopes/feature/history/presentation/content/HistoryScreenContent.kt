package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.navigation.DemoConversation
import com.example.hopes.navigation.HopesDestination

@Composable
fun HistoryScreenContent(searchQuery: String, conversations: List<DemoConversation>, onSearchQueryChange: (String) -> Unit, onQuestionClick: (String) -> Unit, onNavigate: (HopesDestination) -> Unit) {
    val questions = (conversations.map(DemoConversation::question) + stringArrayResource(R.array.history_recent_questions)).distinct().filter { it.contains(searchQuery, true) }
    HopesScaffold(HopesDestination.History, onNavigate) { inner -> LazyColumn(Modifier.fillMaxSize().padding(inner), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = AppSpacing.ScreenHorizontal, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { item { Text(stringResource(R.string.history_title), style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold)); Text(stringResource(R.string.history_description), color = MaterialTheme.colorScheme.onSurfaceVariant) }; item { BasicTextField(searchQuery, onSearchQueryChange, Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)).padding(14.dp), textStyle = TextStyle(fontSize = 14.sp)) }; items(questions.size) { index -> Text(questions[index], Modifier.fillMaxWidth().clickable { onQuestionClick(questions[index]) }.padding(vertical = 12.dp), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)) } } }
}
