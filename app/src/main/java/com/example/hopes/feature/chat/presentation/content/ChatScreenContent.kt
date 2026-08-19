package com.example.hopes.feature.chat.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.feature.chat.presentation.component.ChatComposer
import com.example.hopes.feature.chat.presentation.component.ChatNewChatButton
import com.example.hopes.feature.chat.presentation.component.ChatWelcomeHero
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
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = AppSpacing.SystemBarToContent, end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FigmaBrandHeader()

                ChatNewChatButton(onClick = onNewChatClick)
            }

            Spacer(modifier = Modifier.height(126.dp))

            ChatWelcomeHero(isCreateChatError = isCreateChatError)
        }
    }
}
