package com.example.hopes.feature.community.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * 게시글 상세 아래에 붙는 댓글 입력줄이다(피그마 14-6). 댓글을 수정 중이면 위에 "댓글 수정 중 · 취소"를 띄운다(피그마 14-8).
 */
@Composable
fun CommunityCommentInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isEditing: Boolean,
    onEditCancel: () -> Unit,
    isSending: Boolean,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }
    val placeholder = stringResource(R.string.community_comment_placeholder)
    val isSendEnabled = value.isNotBlank() && !isSending
    val sendDescription =
        if (isEditing) {
            stringResource(
                R.string.community_edit_submit,
            )
        } else {
            stringResource(R.string.community_comment_send)
        }

    // 댓글 수정을 고르면 입력창에 바로 커서를 둔다.
    LaunchedEffect(isEditing) {
        if (isEditing) focusRequester.requestFocus()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (isEditing) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppSpacing.Compact),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppIconSize.Inline),
                )

                Spacer(modifier = Modifier.width(AppSpacing.Small))

                Text(
                    text = stringResource(R.string.community_comment_editing),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    style = HopesTextStyles.CaptionStrong,
                )

                Text(
                    text = stringResource(R.string.cancel),
                    modifier = Modifier.clickable(role = Role.Button, onClick = onEditCancel),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .semantics { contentDescription = placeholder },
                textStyle = HopesTextStyles.Field.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                interactionSource = interactionSource,
                maxLines = MAX_INPUT_LINES,
                decorationBox = { innerTextField ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .heightIn(min = AppSize.IconButton + AppSpacing.Small)
                                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(INPUT_RADIUS))
                                .border(
                                    width = if (isFocused) AppBorderWidth.Emphasized else AppBorderWidth.Default,
                                    color =
                                        if (isFocused) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.outline
                                        },
                                    shape = RoundedCornerShape(INPUT_RADIUS),
                                ).padding(
                                    horizontal = AppSpacing.Medium,
                                    vertical =
                                        AppSpacing.Compact + AppSpacing.XSmall,
                                ),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = HopesTextStyles.Field,
                            )
                        }

                        innerTextField()
                    }
                },
            )

            Spacer(modifier = Modifier.width(AppSpacing.Compact))

            Box(
                modifier =
                    Modifier
                        .size(AppSize.IconButton + AppSpacing.Small)
                        .background(
                            color =
                                if (isSendEnabled) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_SEND_ALPHA)
                                },
                            shape = CircleShape,
                        ).shapeClickable(
                            shape = CircleShape,
                            enabled = isSendEnabled,
                            role = Role.Button,
                            onClick = onSendClick,
                        ).semantics { contentDescription = sendDescription },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(AppIconSize.Medium),
                )
            }
        }
    }
}

private val INPUT_RADIUS = AppSize.IconButton
private const val MAX_INPUT_LINES = 4
private const val DISABLED_SEND_ALPHA = 0.4f
