package com.example.hopes.feature.chat.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.navigation.HopesDestination

@Composable
fun ChatScreenContent(questionText: String, onQuestionChange: (String) -> Unit, onSuggestionClick: (String) -> Unit, onSubmitClick: () -> Unit, onNewChatClick: () -> Unit, onNavigate: (HopesDestination) -> Unit) {
    HopesScaffold(HopesDestination.Chat, onNavigate) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = AppSpacing.ScreenHorizontal, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { FigmaBrandHeader(); Text(stringResource(R.string.new_chat), Modifier.clickable(onClick = onNewChatClick)) }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.size(74.dp).figmaRaisedShadow(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) { Text(stringResource(R.string.logo_mark), color = MaterialTheme.colorScheme.primary, style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold)) }
                Text(stringResource(R.string.chat_welcome), style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold))
                Text(stringResource(R.string.chat_welcome_description), color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            }
            stringArrayResource(R.array.chat_suggestions).forEach { suggestion -> Box(Modifier.fillMaxWidth().figmaRaisedShadow(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)).clickable { onSuggestionClick(suggestion) }.padding(18.dp)) { Text(suggestion, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold)) } }
            Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) { BasicTextField(questionText, onQuestionChange, Modifier.weight(1f).padding(vertical = 14.dp), textStyle = TextStyle(fontSize = 14.sp)); Text(stringResource(R.string.chat_send), Modifier.clickable(onClick = onSubmitClick).padding(8.dp), color = MaterialTheme.colorScheme.primary) }
        }
    }
}
