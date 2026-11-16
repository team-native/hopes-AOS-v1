package com.example.hopes.feature.chat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesInfoBanner
import com.example.hopes.core.designsystem.component.HopesToast
import com.example.hopes.feature.chat.detail.view.component.ChatDraftAttachments
import com.example.hopes.feature.chat.view.component.ChatComposer
import com.example.hopes.feature.chat.view.component.ChatContextChipRow
import com.example.hopes.feature.chat.view.component.ChatDefaultQuestionCard
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
    onAttachClick: () -> Unit = {},
    toastMessage: String? = null,
    onToastDismiss: () -> Unit = {},
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    val departmentLabel = uiState.department?.label()
    val majorLabel = uiState.major?.label()

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat(),
        onNavigate = onNavigate,
        fixedBottomContent = {
            Column(
                modifier =
                    Modifier
                        .imePadding()
                        // 피그마 Composer: 좌우 16, 위아래 10.
                        .padding(
                            start = AppSpacing.Medium,
                            top = COMPOSER_PADDING_VERTICAL,
                            end = AppSpacing.Medium,
                            bottom = COMPOSER_PADDING_VERTICAL,
                        ),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
            ) {
                if (uiState.draftAttachments.isNotEmpty()) {
                    ChatDraftAttachments(
                        attachments = uiState.draftAttachments,
                        onRemove = { onEvent(ChatScreenEvent.AttachmentRemoved(it.uri)) },
                        // 업로드 API 연동 전이라 업로드 실패 상태가 생기지 않는다.
                        onRetry = {},
                    )
                }

                ChatComposer(
                    value = uiState.questionText,
                    onValueChange = { onEvent(ChatScreenEvent.QuestionChanged(it)) },
                    onSubmitClick = { onEvent(ChatScreenEvent.QuestionSubmitted) },
                    placeholder =
                        if (uiState.draftAttachments.isNotEmpty()) {
                            stringResource(R.string.chat_attach_question_placeholder)
                        } else {
                            stringResource(R.string.chat_new_message)
                        },
                    isSubmitAllowed = uiState.canSendDraftAttachments,
                    onAttachClick = onAttachClick,
                )
            }
        },
        isBottomNavigationVisible = !isImeVisible,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 키보드 높이만큼 줄어든 영역은 bottomBar(composer의 imePadding 포함)가 innerPadding으로 전달하므로
            // 여기서 imePadding을 다시 적용하지 않는다.
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = AppSpacing.ScreenHorizontal,
                            top = AppSpacing.SystemBarToContent,
                            end = AppSpacing.ScreenHorizontal,
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
                    // 피그마 Content 요소 간격은 16이다. 학과·전공을 고르기 전(05-3)에는 제목이 없다.
                    Spacer(modifier = Modifier.height(AppSpacing.Medium))

                    if (uiState.isPersonalized) {
                        ChatWelcomeTitle()

                        Spacer(modifier = Modifier.height(AppSpacing.Medium))
                    }

                    ChatRecommendations(
                        uiState = uiState,
                        departmentLabel = departmentLabel,
                        majorLabel = majorLabel,
                        onEvent = onEvent,
                    )
                }
            }

            if (toastMessage != null) {
                HopesToast(
                    message = toastMessage,
                    onDismiss = onToastDismiss,
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = AppSpacing.Section),
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
        // 피그마 Cloud Card: 위 8, 아래 6.
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = AppSpacing.Compact, bottom = AppSpacing.Small + AppSpacing.XSmall)
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

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

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
        // 피그마 05의 "추천 질문" 줄과 첫 카드 사이 간격이다.
        Spacer(modifier = Modifier.height(AppSpacing.Medium))
    }

    if (!uiState.isPersonalized) {
        // 피그마 05-3: 학과·전공을 고르기 전에는 기본 추천 질문을 카드 한 장에 구분선으로 보여준다.
        ChatDefaultQuestionCard(
            questions = uiState.recommendedTopics.map { topic -> topic.icon() to topic.question(uiState.major) },
            onQuestionClick = { question -> onEvent(ChatScreenEvent.SuggestionClicked(question)) },
        )
    } else {
        // 피그마 05: 질문마다 별도 카드이고 카드 사이 간격은 8이다.
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

private val COMPOSER_PADDING_VERTICAL = 10.dp
