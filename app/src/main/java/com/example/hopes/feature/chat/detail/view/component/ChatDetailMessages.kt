package com.example.hopes.feature.chat.detail.view.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.chat.detail.view.answer.ChatAnswerContent
import com.example.hopes.feature.chat.detail.view.answer.parseChatAnswerBlocks
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 내가 보낸 질문이다. 오른쪽에 붙는 파란 말풍선으로 그린다. */
@Composable
fun ChatUserBubble(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier =
            modifier
                .widthIn(max = USER_BUBBLE_MAX_WIDTH)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(AppRadius.Card),
                ).padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Item),
        color = MaterialTheme.colorScheme.onPrimary,
        style = HopesTextStyles.Body,
    )
}

/**
 * 선배 답변 카드다. 답변을 문단으로 나눠 그리고, 아래에 복사·공유 버튼을 둔다(피그마 08, 08-6).
 *
 * @param sources 첨부 자료 기반 답변이면 "생활수칙.pdf · 4~5쪽"처럼 근거 자료를 넘긴다. 카드 맨 위에 칩으로 보인다(11-3).
 */
@Composable
fun ChatAnswerCard(
    text: String,
    sources: List<String>,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onCopyCode: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)
    val blocks = remember(text) { parseChatAnswerBlocks(text) }

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .figmaSubtleShadow(cardShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = cardShape,
                    ).padding(AppSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            sources.forEach { source -> ChatAnswerSourceChip(source = source) }

            ChatAnswerContent(
                blocks = blocks,
                onCopyCode = onCopyCode,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Row {
            ChatAnswerActionButton(
                icon = Icons.Outlined.ContentCopy,
                contentDescription = stringResource(R.string.chat_copy_answer),
                onClick = onCopyClick,
            )

            ChatAnswerActionButton(
                icon = Icons.AutoMirrored.Outlined.Send,
                contentDescription = stringResource(R.string.chat_share_answer),
                onClick = onShareClick,
            )
        }
    }
}

/**
 * 답변을 기다리는 동안 보여주는 점 세 개 말풍선이다.
 *
 * @param text 기본은 "AI가 답변을 만들고 있어요."다. 첨부 분석 중(피그마 11)에는 "자료를 분석하고 있어요…"를 넘긴다.
 * @param detail 문구 아래 작은 회색 줄이다(예: "업로드 완료 · 이미지 2장, PDF 1개").
 */
@Composable
fun ChatAnswerGenerating(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.chat_answer_generating),
    detail: String? = null,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)
    val transition = rememberInfiniteTransition(label = "chatGeneratingDots")
    val activeDot by transition.animateFloat(
        initialValue = 0f,
        targetValue = DOT_COUNT.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(DOT_CYCLE_MILLIS), repeatMode = RepeatMode.Restart),
        label = "chatGeneratingActiveDot",
    )

    Row(
        modifier =
            modifier
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Medium)
                .semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
            repeat(DOT_COUNT) { index ->
                Box(
                    modifier =
                        Modifier
                            .size(DOT_SIZE)
                            .alpha(if (activeDot.toInt() == index) 1f else INACTIVE_DOT_ALPHA)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                )
            }
        }

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Column {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.LabelStrong,
            )

            if (detail != null) {
                Text(
                    text = detail,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }
        }
    }
}

/** 답변 근거 자료 칩이다(피그마 11-3 "생활수칙.pdf · 4~5쪽 기반"). */
@Composable
private fun ChatAnswerSourceChip(source: String) {
    Row(
        modifier =
            Modifier
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .padding(horizontal = AppSpacing.Compact, vertical = AppSpacing.XSmall + AppSpacing.XSmall),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppIconSize.Inline),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = stringResource(R.string.chat_answer_source, source),
            color = MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.Tag,
        )
    }
}

@Composable
private fun ChatAnswerActionButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(AppSize.IconButton)
                .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppIconSize.Small),
        )
    }
}

private val USER_BUBBLE_MAX_WIDTH = 260.dp
private val DOT_SIZE = 6.dp
private const val DOT_COUNT = 3
private const val DOT_CYCLE_MILLIS = 900
private const val INACTIVE_DOT_ALPHA = 0.3f
