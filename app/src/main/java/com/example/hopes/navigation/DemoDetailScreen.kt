package com.example.hopes.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.stringArrayResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesLogo
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 피그마의 채팅 상세와 설정 하위 화면을 목적별 카드 구성으로 제공한다. */
@Composable
fun DemoDetailScreen(
    destination: HopesDestination,
    onBackClick: () -> Unit,
) {
    when (destination) {
        HopesDestination.ChatDetail -> ChatDetailContent(onBackClick = onBackClick)
        HopesDestination.MyPage -> MyPageContent(onBackClick = onBackClick)
        HopesDestination.PersonalSettings -> PersonalSettingsContent(onBackClick = onBackClick)
        HopesDestination.Contact -> ContactContent(onBackClick = onBackClick)
        else -> Unit
    }
}

@Composable
private fun DetailScaffold(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(AppSpacing.ScreenHorizontal),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
            TextButton(onClick = onBackClick) { Text(text = stringResource(R.string.back)) }
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
        }
        content()
    }
}

@Composable
private fun ChatDetailContent(onBackClick: () -> Unit) {
    var replyText by rememberSaveable { mutableStateOf("") }
    DetailScaffold(title = stringResource(R.string.chat_detail_title), onBackClick = onBackClick) {
        Text(text = stringResource(R.string.chat_answer_label), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            HopesSurfaceCard { Text(text = stringArrayResource(R.array.history_questions)[0], style = MaterialTheme.typography.bodyLarge) }
        }
        HopesSurfaceCard { Text(text = stringResource(R.string.chat_detail_answer), style = MaterialTheme.typography.bodyLarge) }
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
            OutlinedTextField(value = replyText, onValueChange = { replyText = it }, modifier = Modifier.weight(1f), label = { Text(stringResource(R.string.chat_additional_question)) })
            TextButton(onClick = { replyText = "" }) { Text(stringResource(R.string.chat_send)) }
        }
    }
}

@Composable
private fun MyPageContent(onBackClick: () -> Unit) {
    var introductionText by rememberSaveable { mutableStateOf("") }
    DetailScaffold(title = stringResource(R.string.my_page), onBackClick = onBackClick) {
        HopesLogo()
        HopesSurfaceCard {
            Text(text = stringResource(R.string.my_page_account), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.my_page_email), style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(R.string.my_page_major), style = MaterialTheme.typography.bodyMedium)
        }
        HopesSurfaceCard {
            Text(text = stringResource(R.string.profile), style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = introductionText, onValueChange = { introductionText = it }, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.profile_placeholder)) }, minLines = 3)
            HopesPrimaryButton(text = stringResource(R.string.save), onClick = {})
        }
    }
}

@Composable
private fun PersonalSettingsContent(onBackClick: () -> Unit) {
    var promptText by rememberSaveable { mutableStateOf("") }
    DetailScaffold(title = stringResource(R.string.personal_settings), onBackClick = onBackClick) {
        HopesSurfaceCard {
            Text(text = stringResource(R.string.personal_settings), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.system_prompt), style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(value = promptText, onValueChange = { promptText = it }, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.prompt_placeholder)) }, minLines = 6)
            HopesPrimaryButton(text = stringResource(R.string.save), onClick = {})
        }
    }
}

@Composable
private fun ContactContent(onBackClick: () -> Unit) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var contentText by rememberSaveable { mutableStateOf("") }
    DetailScaffold(title = stringResource(R.string.contact), onBackClick = onBackClick) {
        HopesSurfaceCard {
            OutlinedTextField(value = emailText, onValueChange = { emailText = it }, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) })
            OutlinedTextField(value = contentText, onValueChange = { contentText = it }, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.contact_placeholder)) }, minLines = 5)
            HopesPrimaryButton(text = stringResource(R.string.send_contact), onClick = {})
        }
        HopesSurfaceCard { Text(text = stringResource(R.string.contact_email_info), style = MaterialTheme.typography.bodyMedium) }
    }
}
