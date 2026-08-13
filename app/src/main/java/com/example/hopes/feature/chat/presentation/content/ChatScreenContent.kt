package com.example.hopes.feature.chat.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.core.designsystem.component.FigmaViewportMetrics
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.navigation.HopesDestination

/** 피그마 05 채팅 홈 프레임을 로컬 질문 상태와 함께 표시한다. */
@Composable
fun ChatScreenContent(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNewChatClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
        imeOverlay = { viewportMetrics ->
            if (isImeVisible) {
                FigmaImeChatComposer(
                    viewportMetrics = viewportMetrics,
                    value = questionText,
                    onValueChange = onQuestionChange,
                    onSubmitClick = onSubmitClick,
                )
            }
        },
    ) {
        ChatHomeHeader(onNewChatClick = onNewChatClick)
        ChatWelcome()
        ChatSuggestionList(onSuggestionClick = onSuggestionClick)
        if (!isImeVisible) {
            FigmaChatComposer(
                value = questionText,
                onValueChange = onQuestionChange,
                onSubmitClick = onSubmitClick,
            )
        }
    }
}

@Composable
private fun ChatHomeHeader(onNewChatClick: () -> Unit) {
    FigmaBrandHeader(modifier = Modifier.padding(start = 24.dp, top = 72.dp))
    Box(
        modifier = Modifier
            .padding(start = 308.dp, top = 66.dp)
            .width(70.dp)
            .height(39.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .clickable(onClick = onNewChatClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.new_chat),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun ChatWelcome() {
    Box(
        modifier = Modifier
            .padding(start = 164.dp, top = 184.dp)
            .size(74.dp)
            .figmaRaisedShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.logo_mark),
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
        )
    }
    Text(
        text = stringResource(R.string.chat_welcome),
        modifier = Modifier.padding(start = 42.dp, top = 288.dp).width(318.dp),
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
    )
    Text(
        text = stringResource(R.string.chat_welcome_description),
        modifier = Modifier.padding(start = 42.dp, top = 332.dp).width(318.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 14.sp),
    )
}

@Composable
private fun ChatSuggestionList(onSuggestionClick: (String) -> Unit) {
    val suggestionTexts = stringArrayResource(R.array.chat_suggestions)
    val symbols = stringArrayResource(R.array.chat_suggestion_symbols)
    suggestionTexts.forEachIndexed { index, question ->
        val topOffset = 396 + (index * 76)
        FigmaSuggestionCard(
            question = question,
            symbol = symbols.getOrElse(index) {
                stringResource(R.string.chat_suggestion_marker)
            },
            modifier = Modifier.padding(start = 24.dp, top = topOffset.dp),
            onClick = { onSuggestionClick(question) },
        )
    }
}

@Composable
private fun FigmaSuggestionCard(
    question: String,
    symbol: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .width(354.dp)
            .height(70.dp)
            .figmaRaisedShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 15.dp)
                .size(38.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = symbol,
                color = MaterialTheme.colorScheme.primary,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
            )
        }
        Text(
            text = question,
            modifier = Modifier.padding(start = 68.dp, top = 22.dp).width(266.dp),
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp),
        )
    }
}

@Composable
private fun FigmaChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    FigmaChatComposerSurface(
        value = value,
        onValueChange = onValueChange,
        onSubmitClick = onSubmitClick,
        modifier = Modifier.padding(start = 24.dp, top = 728.dp),
    )
}

/** 키보드가 열린 동안 전체 화면을 축소하지 않고 입력창만 키보드 위에 고정한다. */
@Composable
private fun androidx.compose.foundation.layout.BoxScope.FigmaImeChatComposer(
    viewportMetrics: FigmaViewportMetrics,
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .imePadding()
            .width((354f * viewportMetrics.scale).dp)
            .height((52f * viewportMetrics.scale).dp),
    ) {
        Box(
            modifier = Modifier
                .width(354.dp)
                .height(52.dp)
                .graphicsLayer(
                    scaleX = viewportMetrics.scale,
                    scaleY = viewportMetrics.scale,
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f),
                ),
        ) {
            FigmaChatComposerSurface(
                value = value,
                onValueChange = onValueChange,
                onSubmitClick = onSubmitClick,
            )
        }
    }
}

@Composable
private fun FigmaChatComposerSurface(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sendDescription = stringResource(R.string.chat_send)

    Box(
        modifier = modifier
            .width(354.dp)
            .height(52.dp)
            .figmaRaisedShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.chat_new_message),
                modifier = Modifier.padding(start = 14.dp, top = 17.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 14.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 14.dp, top = 10.dp)
                .width(276.dp)
                .height(32.dp),
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
            ),
        )
        Box(
            modifier = Modifier
                .padding(start = 306.dp, top = 11.dp)
                .size(30.dp)
                .figmaRaisedShadow(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp))
                .semantics {
                    role = Role.Button
                    contentDescription = sendDescription
                }
                .clickable(onClick = onSubmitClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.chat_send_symbol),
                color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
            )
        }
    }
}
