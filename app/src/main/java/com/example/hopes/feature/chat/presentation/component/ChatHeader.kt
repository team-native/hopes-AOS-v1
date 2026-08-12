package com.example.hopes.feature.chat.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R

/** 채팅 화면의 제목과 이용 안내를 표시한다. */
@Composable
fun ChatHeader() {
    Text(
        text = stringResource(R.string.chat_title),
        style = MaterialTheme.typography.headlineMedium,
    )
    Text(
        text = stringResource(R.string.chat_description),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyLarge,
    )
}
