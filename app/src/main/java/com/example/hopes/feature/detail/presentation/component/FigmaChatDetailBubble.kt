package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.component.figmaSubtleShadow

/** 피그마 채팅 상세의 사용자·답변 말풍선 크기와 그림자를 표현한다. */
@Composable
fun FigmaChatDetailBubble(
    text: String,
    modifier: Modifier,
    isUser: Boolean,
) {
    val bubbleShape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .widthIn(max = if (isUser) 265.dp else 354.dp)
            .defaultMinSize(minHeight = 58.dp)
            .then(
                if (isUser) {
                    Modifier
                } else {
                    Modifier.figmaSubtleShadow(bubbleShape)
                },
            )
            .background(
                color = if (isUser) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = bubbleShape,
            )
            .border(
                width = if (isUser) 0.dp else 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = bubbleShape,
            ),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(
                    start = if (isUser) 16.dp else 20.dp,
                    end = if (isUser) 16.dp else 20.dp,
                    top = 18.dp,
                    bottom = 18.dp,
                ),
            color = if (isUser) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = if (isUser) FontWeight.SemiBold else FontWeight.Normal,
                lineHeight = 22.sp,
            ),
        )
    }
}
