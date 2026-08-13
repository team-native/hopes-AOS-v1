package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import androidx.compose.ui.unit.dp

/** 질문을 입력하고 로컬 답변을 요청하는 채팅 입력 영역이다. */
@Composable
fun ChatComposer(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        OutlinedTextField(
            value = questionText,
            onValueChange = onQuestionChange,
            modifier = Modifier.width(270.dp),
            placeholder = { Text(text = stringResource(R.string.chat_new_message)) },
            singleLine = true,
        )
        Button(onClick = onSubmitClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.chat_send),
            )
        }
    }
}
