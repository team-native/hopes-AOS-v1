package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesPrimaryButton

/** 질문을 입력하고 로컬 답변을 요청하는 채팅 입력 영역이다. */
@Composable
fun ChatComposer(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
        OutlinedTextField(
            value = questionText,
            onValueChange = onQuestionChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.chat_input_label)) },
            minLines = 3,
        )
        HopesPrimaryButton(
            text = stringResource(R.string.chat_send),
            onClick = onSubmitClick,
        )
    }
}
