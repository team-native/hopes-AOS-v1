package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaSubtleShadow

/** 추가 질문 전송 후 AI 답변을 기다리는 동안 표시하는 로딩 말풍선이다. 왼쪽 스피너는 계속 회전한다. */
@Composable
fun FigmaChatAnswerGeneratingBubble(modifier: Modifier = Modifier) {
    val bubbleShape = RoundedCornerShape(18.dp)

    Row(
        modifier = modifier
            .figmaSubtleShadow(bubbleShape)
            .background(color = MaterialTheme.colorScheme.surface, shape = bubbleShape)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = bubbleShape)
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.chat_answer_generating),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 15.sp),
        )
    }
}
