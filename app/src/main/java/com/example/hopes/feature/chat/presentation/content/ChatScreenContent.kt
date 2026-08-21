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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.hopes.feature.chat.presentation.component.ChatSuggestionList
import com.example.hopes.feature.chat.presentation.component.ChatWelcomeHero
import com.example.hopes.navigation.HopesDestination

/**
 * 피그마 05 채팅 홈 프레임을 서버 대화 생성 입력 상태와 함께 표시한다.
 * 질문을 제출하면 대화 생성을 기다리지 않고 곧바로 채팅 상세 화면으로 이동하며,
 * 대화 생성·첫 답변 로딩 표시는 상세 화면(ChatDetailScreenContent)이 담당한다.
 */
@Composable
fun ChatScreenContent(
    questionText: String,
    onQuestionChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    onNewChatClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat(),
        onNavigate = onNavigate,
        fixedBottomContent = {
            ChatComposer(
                value = questionText,
                onValueChange = onQuestionChange,
                onSubmitClick = onSubmitClick,
                isLoading = false,
                modifier = Modifier
                    .imePadding()
                    .padding(bottom = 10.dp),
            )
        },
        isBottomNavigationVisible = !isImeVisible,
    ) {
        // 키보드가 올라와 가용 높이가 줄어들 때도 콘텐츠가 잘리지 않도록 스크롤 가능하게 하고,
        // imePadding으로 키보드 높이만큼 하단 여백을 확보해 마지막 카드까지 스크롤해 볼 수 있게 한다.
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .imePadding(),
        ) {
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

            Spacer(modifier = Modifier.height(30.dp))

            ChatWelcomeHero(isCreateChatError = false)

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            ChatSuggestionList(
                onSuggestionClick = onSuggestionClick,
                isLoading = false,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(27.dp))
        }
    }
}
