package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.history.presentation.ChatSummaryUiModel
import com.example.hopes.feature.history.presentation.HistoryDateLabel
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * 지난 대화 묶음 카드다. 행마다 제목·날짜·오른쪽 화살표를 보여주고, 왼쪽으로 밀면 빨간 "삭제"가 나온다(피그마 24, 24-5).
 *
 * @param highlight 검색어다. 제목에서 일치하는 부분을 파란 글자로 강조한다(24-2).
 * @param dateLabelOf 대화의 날짜 문구 종류를 돌려준다. null이면 날짜 줄을 숨긴다.
 */
@Composable
fun HistoryChatGroup(
    chats: List<ChatSummaryUiModel>,
    highlight: String,
    dateLabelOf: (ChatSummaryUiModel) -> HistoryDateLabel?,
    onChatClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface),
    ) {
        chats.forEachIndexed { index, chat ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = AppSpacing.Medium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                )
            }

            HistoryChatRow(
                chat = chat,
                highlight = highlight,
                dateLabel = dateLabelOf(chat),
                onClick = { onChatClick(chat.id) },
                onDeleteClick = { onDeleteClick(chat.id) },
            )
        }
    }
}

@Composable
private fun HistoryChatRow(
    chat: ChatSummaryUiModel,
    highlight: String,
    dateLabel: HistoryDateLabel?,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()
    val deleteLabel = stringResource(R.string.history_delete)

    // 끝까지 밀면 확인 다이얼로그를 띄우고 행은 제자리로 돌려 둔다. 실제 삭제는 확인 후에만 한다.
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDeleteClick()
            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Column(
                    modifier =
                        Modifier
                            .width(DELETE_ACTION_WIDTH)
                            .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(AppIconSize.Small),
                    )

                    Text(
                        text = deleteLabel,
                        color = MaterialTheme.colorScheme.onError,
                        style = HopesTextStyles.Tag,
                    )
                }
            }
        },
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(role = Role.Button, onClick = onClick)
                    // 밀기 동작을 쓰기 어려운 사용자도 접근성 메뉴에서 삭제할 수 있게 한다.
                    .semantics {
                        customActions =
                            listOf(
                                CustomAccessibilityAction(deleteLabel) {
                                    onDeleteClick()
                                    true
                                },
                            )
                    }.padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Item + AppSpacing.XSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = highlightedTitle(chat.title, highlight),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = HopesTextStyles.Body,
                )

                if (dateLabel != null) {
                    Text(
                        text = dateLabel.text(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = HopesTextStyles.Caption,
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }
    }
}

@Composable
private fun highlightedTitle(
    title: String,
    highlight: String,
): AnnotatedString {
    val keyword = highlight.trim()
    val start = if (keyword.isEmpty()) -1 else title.indexOf(keyword, ignoreCase = true)
    if (start < 0) return AnnotatedString(title)

    val highlightColor = MaterialTheme.colorScheme.primary
    return buildAnnotatedString {
        append(title.substring(0, start))
        withStyle(SpanStyle(color = highlightColor)) {
            append(title.substring(start, start + keyword.length))
        }
        append(title.substring(start + keyword.length))
    }
}

@Composable
private fun HistoryDateLabel.text(): String =
    when (this) {
        HistoryDateLabel.Today -> stringResource(R.string.history_date_today)
        HistoryDateLabel.Yesterday -> stringResource(R.string.history_date_yesterday)
        is HistoryDateLabel.DaysAgo -> stringResource(R.string.history_date_days_ago, days.toInt())
        is HistoryDateLabel.Date -> stringResource(R.string.history_date_month_day, date.monthValue, date.dayOfMonth)
    }

private val DELETE_ACTION_WIDTH = 72.dp
private const val DIVIDER_ALPHA = 0.6f
