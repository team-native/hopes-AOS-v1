package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SIGNUP_VERIFICATION_CODE_LENGTH
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 6칸으로 나뉜 인증번호 입력란이다. 실제 입력은 보이지 않는 하나의 BasicTextField가 받고,
 * 칸 모양만 decorationBox에서 그려 붙여넣기·삭제가 일반 입력란처럼 동작한다.
 *
 * @param errorMessage null이 아니면 모든 칸을 빨간 테두리로 그리고 접근성 오류로 알린다.
 * @param isVerified 인증이 끝난 뒤의 회색 고정 상태로 그린다.
 */
@Composable
fun SignupCodeInput(
    code: String,
    errorMessage: String?,
    isEnabled: Boolean,
    isVerified: Boolean,
    onCodeChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val fieldDescription = stringResource(R.string.signup_code_label)
    val boxShape = RoundedCornerShape(AppRadius.Field)

    BasicTextField(
        value = code,
        onValueChange = onCodeChange,
        modifier =
            modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = fieldDescription
                    if (errorMessage != null) {
                        error(errorMessage)
                    }
                },
        enabled = isEnabled,
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done,
            ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        decorationBox = { _ ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
            ) {
                repeat(SIGNUP_VERIFICATION_CODE_LENGTH) { index ->
                    val digit = code.getOrNull(index)?.toString().orEmpty()
                    val isCurrentBox = isFocused && isEnabled && index == code.length
                    val borderColor =
                        when {
                            isVerified -> Color.Transparent
                            errorMessage != null -> MaterialTheme.colorScheme.error
                            isCurrentBox -> MaterialTheme.colorScheme.primary
                            else -> extendedColors.authFieldBorder
                        }

                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(CODE_BOX_HEIGHT)
                                .background(
                                    color =
                                        if (isVerified) {
                                            extendedColors.replyFieldBackground
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        },
                                    shape = boxShape,
                                ).border(
                                    width =
                                        if (isCurrentBox || errorMessage != null) {
                                            AppBorderWidth.Emphasized
                                        } else {
                                            AppBorderWidth.Default
                                        },
                                    color = borderColor,
                                    shape = boxShape,
                                ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = digit,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = HopesTextStyles.CodeDigit,
                        )
                    }
                }
            }
        },
    )
}

private val CODE_BOX_HEIGHT = 52.dp
