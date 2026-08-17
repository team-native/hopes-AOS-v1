package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 키보드 상태와 관계없이 하나의 입력창을 하단 또는 키보드 위에 표시한다. */
@Composable
fun FigmaChatReplyBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .imePadding()
            .width(402.dp)
            .height(74.dp)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        FigmaChatReplyInput(
            value = value,
            onValueChange = onValueChange,
        )
        FigmaDetailPrimaryButton(
            text = stringResource(R.string.chat_send),
            modifier = Modifier
                .padding(start = 320.dp, top = 16.dp)
                .width(58.dp)
                .height(44.dp),
            onClick = onSubmitClick,
        )
    }
}

/** 피그마 282×44 추가 질문 입력 필드다. */
@Composable
private fun FigmaChatReplyInput(
    value: String,
    onValueChange: (String) -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = Modifier
            .padding(start = 24.dp, top = 16.dp)
            .width(282.dp)
            .height(44.dp)
            .background(
                color = extendedColors.replyFieldBackground,
                shape = RoundedCornerShape(16.dp),
            )
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.CenterStart,
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.chat_additional_question),
                modifier = Modifier.padding(start = 20.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 14.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
                .width(244.dp)
                .height(24.dp),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.width(244.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    innerTextField()
                }
            },
        )
    }
}
