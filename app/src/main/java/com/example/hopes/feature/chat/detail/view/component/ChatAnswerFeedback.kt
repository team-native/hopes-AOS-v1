package com.example.hopes.feature.chat.detail.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesCardHeading
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 마지막 답변 아래의 "좋아요"(켜고 끄기)와 "더 물어보기" 칩이다(피그마 08). */
@Composable
fun ChatAnswerFeedbackRow(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onAskMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        Box(
            modifier =
                Modifier
                    .height(AppSize.SmallButton)
                    .clip(CircleShape)
                    .background(
                        if (isLiked) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    ).border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, CircleShape)
                    .toggleable(value = isLiked, role = Role.Checkbox, onValueChange = { onLikeClick() })
                    .padding(horizontal = AppSpacing.Medium),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.chat_like),
                color =
                    if (isLiked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                style = HopesTextStyles.LabelStrong,
            )
        }

        Box(
            modifier =
                Modifier
                    .height(AppSize.SmallButton)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onAskMoreClick)
                    .padding(horizontal = AppSpacing.Medium),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.chat_ask_more),
                color = MaterialTheme.colorScheme.onPrimary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

/** "추가로 많이 묻는 질문" 카드다. 질문을 누르면 입력창에 채운다(피그마 08). */
@Composable
fun ChatFollowUpQuestionsCard(
    questions: List<String>,
    onQuestionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = AppSpacing.Item),
    ) {
        HopesCardHeading(
            title = stringResource(R.string.chat_follow_up_title),
            modifier = Modifier.padding(horizontal = AppSpacing.Medium),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        questions.forEachIndexed { index, question ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = AppSpacing.Medium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                )
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button, onClick = { onQuestionClick(question) })
                        .padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Item),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppIconSize.Small),
                )

                Spacer(modifier = Modifier.width(AppSpacing.Compact))

                Text(
                    text = question,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = HopesTextStyles.Body,
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AppIconSize.Medium),
                )
            }
        }
    }
}

/** 화면 아래, 입력창 바로 위에 뜨는 흰색 "답변 중지" 버튼이다(피그마 08-2). */
@Composable
fun ChatStopAnswerButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonShape = RoundedCornerShape(AppRadius.Field)

    Box(
        modifier =
            modifier
                .height(AppSize.SmallButton + AppSpacing.Small)
                .figmaSubtleShadow(buttonShape)
                .background(MaterialTheme.colorScheme.surface, buttonShape)
                .shapeClickable(shape = buttonShape, role = Role.Button, onClick = onClick)
                .padding(horizontal = AppSpacing.Medium),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.chat_stop),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.LabelStrong,
        )
    }
}

/** 오프라인이라 아직 못 보낸 질문 아래에 붙는 "연결되면 전송돼요" 표시다(피그마 08-5). */
@Composable
fun ChatPendingStatus(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Inline),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = stringResource(R.string.chat_pending),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Caption,
        )
    }
}

private const val DIVIDER_ALPHA = 0.6f
