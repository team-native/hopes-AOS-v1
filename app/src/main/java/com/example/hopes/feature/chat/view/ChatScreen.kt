package com.example.hopes.feature.chat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesInfoBanner
import com.example.hopes.feature.chat.view.component.ChatComposer
import com.example.hopes.feature.chat.view.component.ChatContextChipRow
import com.example.hopes.feature.chat.view.component.ChatRecommendationHeader
import com.example.hopes.feature.chat.view.component.ChatRecommendationSkeleton
import com.example.hopes.feature.chat.view.component.ChatRecommendedQuestionCard
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.example.hopes.feature.chat.viewmodel.ChatRecommendationState
import com.example.hopes.feature.chat.viewmodel.ChatScreenEvent
import com.example.hopes.feature.chat.viewmodel.ChatUiState
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 05 채팅 홈이다. 학과·전공 칩, "무엇이 궁금한가요?" 제목, 추천 질문, 하단 입력창으로 된다.
 * 키보드가 올라오면 추천 질문을 숨기고 안내 한 줄만 남긴다(05-5).
 * 질문을 보내면 대화 생성을 기다리지 않고 곧바로 채팅 상세 화면으로 이동한다.
 */
@Composable
fun ChatScreen(
    uiState: ChatUiState,
    onEvent: (ChatScreenEvent) -> Unit,
    onDepartmentClick: () -> Unit,
    onMajorClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    val departmentLabel = uiState.department?.label()
    val majorLabel = uiState.major?.label()

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat(),
        onNavigate = onNavigate,
        fixedBottomContent = {
            ChatComposer(
                value = uiState.questionText,
                onValueChange = { onEvent(ChatScreenEvent.QuestionChanged(it)) },
                onSubmitClick = { onEvent(ChatScreenEvent.QuestionSubmitted) },
                placeholder = stringResource(R.string.chat_new_message),
                modifier =
                    Modifier
                        .imePadding()
                        .padding(
                            start = AppSpacing.Medium,
                            end = AppSpacing.Medium,
                            bottom = AppSpacing.Compact,
                        ),
            )
        },
        isBottomNavigationVisible = !isImeVisible,
    ) {
        // 키보드 높이만큼 줄어든 영역은 bottomBar(composer의 imePadding 포함)가 innerPadding으로 전달하므로
        // 여기서 imePadding을 다시 적용하지 않는다.
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppSpacing.Medium,
                        top = AppSpacing.SystemBarToContent,
                        end = AppSpacing.Medium,
                        bottom = AppSpacing.Section,
                    ),
        ) {
            ChatContextChipRow(
                departmentLabel = departmentLabel,
                majorLabel = majorLabel,
                onDepartmentClick = onDepartmentClick,
                onMajorClick = onMajorClick,
                onNewChatClick = { onEvent(ChatScreenEvent.NewChatClicked) },
            )

            if (isImeVisible) {
                Spacer(modifier = Modifier.height(AppSpacing.Item))

                Text(
                    text = stringResource(R.string.chat_recommendation_fill_hint),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            } else {
                Spacer(modifier = Modifier.height(AppSpacing.Section))

                ChatWelcomeTitle()

                Spacer(modifier = Modifier.height(AppSpacing.Section))

                ChatRecommendations(
                    uiState = uiState,
                    departmentLabel = departmentLabel,
                    majorLabel = majorLabel,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun ChatWelcomeTitle() {
    val welcome = stringResource(R.string.chat_welcome)
    val highlight = stringResource(R.string.chat_welcome_highlight)
    val highlightStart = welcome.indexOf(highlight)
    val title =
        buildAnnotatedString {
            if (highlightStart < 0) {
                append(welcome)
            } else {
                append(welcome.substring(0, highlightStart))
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(highlight)
                }
                append(welcome.substring(highlightStart + highlight.length))
            }
        }

    Text(
        text = title,
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics { heading() },
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        style = HopesTextStyles.Headline,
    )
}

@Composable
private fun ChatRecommendations(
    uiState: ChatUiState,
    departmentLabel: String?,
    majorLabel: String?,
    onEvent: (ChatScreenEvent) -> Unit,
) {
    val isPersonalizedLoaded =
        uiState.isPersonalized && uiState.recommendationState == ChatRecommendationState.Loaded
    val isPersonalizedLoading =
        uiState.recommendationState == ChatRecommendationState.Loading &&
            departmentLabel != null &&
            majorLabel != null

    when {
        isPersonalizedLoading && departmentLabel != null && majorLabel != null -> {
            ChatRecommendationHeader(
                label = stringResource(R.string.chat_recommendation_loading, departmentLabel, majorLabel),
                onRefreshClick = null,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            ChatRecommendationSkeleton()
            return
        }

        !uiState.isPersonalized -> {
            HopesInfoBanner(message = stringResource(R.string.chat_recommendation_select_hint))

            Spacer(modifier = Modifier.height(AppSpacing.Medium))
        }
    }

    ChatRecommendationHeader(
        label =
            if (isPersonalizedLoaded && departmentLabel != null && majorLabel != null) {
                stringResource(R.string.chat_recommendation_label, departmentLabel, majorLabel)
            } else {
                stringResource(R.string.chat_recommendation_default_label)
            },
        onRefreshClick =
            if (uiState.isPersonalized) {
                { onEvent(ChatScreenEvent.RecommendationsRefreshClicked) }
            } else {
                null
            },
    )

    if (uiState.isPersonalized && uiState.recommendationState == ChatRecommendationState.LoadError) {
        HopesInfoBanner(
            message = stringResource(R.string.chat_recommendation_error),
            actionText = stringResource(R.string.chat_recommendation_retry),
            onActionClick = { onEvent(ChatScreenEvent.RecommendationsRefreshClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))
    } else {
        Spacer(modifier = Modifier.height(AppSpacing.Small))
    }

    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
        uiState.recommendedTopics.forEach { topic ->
            val question = topic.question(uiState.major)

            ChatRecommendedQuestionCard(
                icon = topic.icon(),
                question = question,
                onClick = { onEvent(ChatScreenEvent.SuggestionClicked(question)) },
            )
        }
    }
}

@Preview(showBackground = true, name = "05 채팅 홈")
@Composable
private fun ChatScreenPreview() {
    HopesTheme {
        ChatScreen(
            uiState = ChatUiState(department = ChatDepartment.Software, major = ChatMajor.Frontend),
            onEvent = {},
            onDepartmentClick = {},
            onMajorClick = {},
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, name = "05-2 추천 불러오기 실패")
@Composable
private fun ChatScreenRecommendationErrorPreview() {
    HopesTheme {
        ChatScreen(
            uiState =
                ChatUiState(
                    department = ChatDepartment.Software,
                    major = ChatMajor.Frontend,
                    recommendationState = ChatRecommendationState.LoadError,
                ),
            onEvent = {},
            onDepartmentClick = {},
            onMajorClick = {},
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, name = "05-3 학과·전공 미선택")
@Composable
private fun ChatScreenUnselectedPreview() {
    HopesTheme {
        ChatScreen(
            uiState = ChatUiState(),
            onEvent = {},
            onDepartmentClick = {},
            onMajorClick = {},
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, name = "05-4 추천 질문 로딩")
@Composable
private fun ChatScreenLoadingPreview() {
    HopesTheme {
        ChatScreen(
            uiState =
                ChatUiState(
                    department = ChatDepartment.Software,
                    major = ChatMajor.Frontend,
                    recommendationState = ChatRecommendationState.Loading,
                ),
            onEvent = {},
            onDepartmentClick = {},
            onMajorClick = {},
            onNavigate = {},
        )
    }
}
