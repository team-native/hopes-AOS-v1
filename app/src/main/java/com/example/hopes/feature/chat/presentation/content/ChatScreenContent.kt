package com.example.hopes.feature.chat.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.feature.chat.presentation.component.ChatComposer
import com.example.hopes.navigation.HopesDestination

/** 피그마 05 채팅 홈 프레임을 서버 대화 생성 입력 상태와 함께 표시한다. */
@Composable
fun ChatScreenContent(
    questionText: String,
    isCreateChatError: Boolean,
    onQuestionChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNewChatClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
        fixedBottomContent = {
            ChatComposer(
                value = questionText,
                onValueChange = onQuestionChange,
                onSubmitClick = onSubmitClick,
                modifier = Modifier
                    .imePadding()
                    .padding(bottom = 10.dp),
            )
        },
        isBottomNavigationVisible = !isImeVisible,
    ) {
        ChatHomeHeader(onNewChatClick = onNewChatClick)
        ChatWelcome(isCreateChatError = isCreateChatError)
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
        Text(text = stringResource(R.string.new_chat), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun ChatWelcome(isCreateChatError: Boolean) {
    Box(
        modifier = Modifier
            .padding(start = 164.dp, top = 184.dp)
            .size(74.dp)
            .figmaRaisedShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(R.string.logo_mark), color = MaterialTheme.colorScheme.primary, style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold))
    }
    Text(text = stringResource(R.string.chat_welcome), modifier = Modifier.padding(start = 42.dp, top = 288.dp).width(318.dp), textAlign = TextAlign.Center, style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold))
    Text(text = stringResource(R.string.chat_welcome_description), modifier = Modifier.padding(start = 42.dp, top = 332.dp).width(318.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, style = TextStyle(fontSize = 14.sp))
    if (isCreateChatError) {
        Text(text = stringResource(R.string.chat_create_error), modifier = Modifier.padding(start = 42.dp, top = 356.dp).width(318.dp), color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium))
    }
}
