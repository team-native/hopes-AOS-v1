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
 */
@Composable
fun ChatAnswerCard(
    text: String,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onCopyCode: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)
    val blocks = remember(text) { parseChatAnswerBlocks(text) }

    Column(modifier = modifier.fillMaxWidth()) {
        ChatAnswerContent(
            blocks = blocks,
            onCopyCode = onCopyCode,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .figmaSubtleShadow(cardShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = cardShape,
                    ).padding(AppSpacing.Medium),
        )

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

/** 답변을 기다리는 동안 보여주는 점 세 개 말풍선이다. */
@Composable
fun ChatAnswerGenerating(modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)
    val transition = rememberInfiniteTransition(label = "chatGeneratingDots")
    val activeDot by transition.animateFloat(
        initialValue = 0f,
        targetValue = DOT_COUNT.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(DOT_CYCLE_MILLIS), repeatMode = RepeatMode.Restart),
        label = "chatGeneratingActiveDot",
    )
    val generatingText = stringResource(R.string.chat_answer_generating)

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

        Text(
            text = generatingText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )
    }
}

/**
 * 빨간 아이콘 제목, 회색 설명, 밑줄 "다시 시도" 링크로 된 실패 안내다(피그마 08-3).
 */
@Composable
fun ChatErrorMessage(
    title: String,
    description: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(AppIconSize.Small),
            )

            Spacer(modifier = Modifier.width(AppSpacing.Small + AppSpacing.XSmall))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.error,
                style = HopesTextStyles.BodyStrong,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Subtitle,
        )

        Text(
            text = stringResource(R.string.chat_retry),
            modifier =
                Modifier
                    .clickable(role = Role.Button, onClick = onRetryClick)
                    .padding(vertical = AppSpacing.Compact),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label.copy(textDecoration = TextDecoration.Underline),
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
