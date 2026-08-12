package com.example.hopes.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 설정 하위 화면과 채팅 상세의 로컬 입력·저장 상태를 표시한다. */
@Composable
fun DemoDetailScreen(
    title: String,
    onBackClick: () -> Unit,
) {
    var inputText by rememberSaveable { mutableStateOf("") }
    var isSaved by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.ScreenHorizontal),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        TextButton(onClick = onBackClick) {
            Text(text = stringResource(R.string.back))
        }
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        HopesSurfaceCard {
            Text(
                text = stringResource(R.string.detail_notice),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = if (title == "문의하기") {
                            stringResource(R.string.contact_content)
                        } else {
                            stringResource(R.string.system_prompt)
                        },
                    )
                },
            )
            HopesPrimaryButton(
                text = if (title == "문의하기") {
                    stringResource(R.string.send_contact)
                } else {
                    stringResource(R.string.save)
                },
                onClick = { isSaved = inputText.isNotBlank() },
            )
            if (isSaved) {
                Text(
                    text = stringResource(R.string.save),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
