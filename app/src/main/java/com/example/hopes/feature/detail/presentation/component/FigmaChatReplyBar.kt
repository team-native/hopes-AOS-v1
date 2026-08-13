package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaViewportMetrics
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 키보드가 닫힌 채팅 상세의 하단 답글 입력 행이다. */
@Composable
fun FigmaChatReplyBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
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

/** 키보드가 열린 뒤에도 원본 비율을 유지하며 답글 입력 행만 키보드 위로 고정한다. */
@Composable
fun BoxScope.FigmaImeChatReplyBar(
    viewportMetrics: FigmaViewportMetrics,
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .imePadding()
            .width((402f * viewportMetrics.scale).dp)
            .height((74f * viewportMetrics.scale).dp),
    ) {
        Box(
            modifier = Modifier
                .width(402.dp)
                .height(74.dp)
                .graphicsLayer(
                    scaleX = viewportMetrics.scale,
                    scaleY = viewportMetrics.scale,
                    transformOrigin = TransformOrigin(0f, 1f),
                ),
        ) {
            FigmaChatReplyBar(
                value = value,
                onValueChange = onValueChange,
                onSubmitClick = onSubmitClick,
                modifier = Modifier,
            )
        }
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
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.chat_additional_question),
                modifier = Modifier.padding(start = 20.dp, top = 14.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 14.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp)
                .width(244.dp)
                .height(24.dp),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
    }
}
