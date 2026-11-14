package com.example.hopes.feature.chat.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * 채팅 홈·상세 하단의 알약 모양 질문 입력창이다. 왼쪽 자료 첨부(+), 가운데 입력, 오른쪽 음성 입력·전송 버튼으로 된다.
 *
 * @param isEnabled false면 입력과 버튼을 모두 막고 [placeholder]를 회색으로 보여준다(답변 생성 중, 오프라인).
 * @param isSubmitAllowed false면 입력은 그대로 두고 전송 버튼만 막는다(보낼 수 없는 첨부가 있을 때).
 */
@Composable
fun ChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isSubmitAllowed: Boolean = true,
    onAttachClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
) {
    val isSubmitEnabled = isEnabled && isSubmitAllowed && value.isNotBlank()
    val sendDescription = stringResource(R.string.chat_send)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = AppSize.Field + AppSpacing.Small)
                .testTag("chat_composer_surface")
                .figmaRaisedShadow(CircleShape)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, CircleShape)
                .padding(horizontal = AppSpacing.Small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ChatComposerIconButton(
            onClick = onAttachClick,
            isEnabled = isEnabled,
            contentDescription = stringResource(R.string.chat_attach),
        ) { tint ->
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    style = HopesTextStyles.Body,
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = placeholder },
                enabled = isEnabled,
                singleLine = true,
                textStyle = HopesTextStyles.Body.copy(color = MaterialTheme.colorScheme.onSurface),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (isSubmitEnabled) onSubmitClick() }),
            )
        }

        ChatComposerIconButton(
            onClick = onVoiceClick,
            isEnabled = isEnabled,
            contentDescription = stringResource(R.string.chat_voice_input),
        ) { tint ->
            Icon(
                imageVector = Icons.Outlined.Mic,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Box(
            modifier =
                Modifier
                    .size(AppSize.IconButton)
                    .shapeClickable(
                        shape = CircleShape,
                        enabled = isSubmitEnabled,
                        role = Role.Button,
                        onClick = onSubmitClick,
                    ).semantics { contentDescription = sendDescription },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(SEND_BUTTON_SIZE)
                        .background(
                            color =
                                if (isSubmitEnabled) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_SEND_ALPHA)
                                },
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(AppIconSize.Small),
                )
            }
        }
    }
}

@Composable
private fun ChatComposerIconButton(
    onClick: () -> Unit,
    isEnabled: Boolean,
    contentDescription: String,
    icon: @Composable (tint: Color) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(AppSize.IconButton)
                .shapeClickable(shape = CircleShape, enabled = isEnabled, role = Role.Button, onClick = onClick)
                .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        icon(
            if (isEnabled) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = DISABLED_ICON_ALPHA)
            },
        )
    }
}

private val SEND_BUTTON_SIZE = 32.dp
private const val DISABLED_SEND_ALPHA = 0.38f
private const val DISABLED_ICON_ALPHA = 0.5f
