package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 로그인·회원가입 화면이 함께 쓰는 48dp 입력 필드다. 포커스 시 primary, 오류 시 error 색 테두리를 표시한다.
 * 비밀번호 재설정·설정 화면이 쓰는 FigmaAuthTextField와 분리해 두 화면에 영향을 주지 않는다.
 *
 * @param errorMessage null이 아니면 오류 상태로 그리고, 접근성 서비스에 이 문구를 오류로 알린다.
 */
@Composable
fun AuthTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderRes: Int,
    errorMessage: String?,
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
) {
    val extendedColors = LocalHopesExtendedColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    // 비밀번호 원문은 저장하지 않고, 화면 회전 후에도 표시 여부만 유지한다.
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val isError = errorMessage != null
    val fieldShape = RoundedCornerShape(AppRadius.Field)
    val borderColor =
        when {
            isError -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> extendedColors.authFieldBorder
        }
    val fieldTextStyle =
        TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
        )
    val fieldDescription = stringResource(placeholderRes)
    val passwordVisibilityDescription =
        stringResource(
            if (isPasswordVisible) {
                R.string.hide_password
            } else {
                R.string.show_password
            },
        )

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(FIELD_HEIGHT)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = fieldShape,
                ).border(
                    width =
                        if (isFocused || isError) {
                            FOCUSED_BORDER_WIDTH
                        } else {
                            DEFAULT_BORDER_WIDTH
                        },
                    color = borderColor,
                    shape = fieldShape,
                ).padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(placeholderRes),
                color = extendedColors.authFieldHint,
                style = fieldTextStyle,
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        end =
                            if (isPassword) {
                                PASSWORD_TOGGLE_SIZE
                            } else {
                                0.dp
                            },
                    ).semantics {
                        contentDescription = fieldDescription
                        if (errorMessage != null) {
                            error(errorMessage)
                        }
                    },
            enabled = isEnabled,
            singleLine = true,
            textStyle = fieldTextStyle,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            interactionSource = interactionSource,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        if (isPassword) {
                            KeyboardType.Password
                        } else {
                            keyboardType
                        },
                    imeAction = imeAction,
                ),
            keyboardActions =
                KeyboardActions(
                    onNext = { onImeAction() },
                    onDone = { onImeAction() },
                ),
            visualTransformation =
                if (isPassword && !isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
        )

        if (isPassword) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.CenterEnd)
                        .size(PASSWORD_TOGGLE_SIZE)
                        .shapeClickable(
                            shape = CircleShape,
                            onClick = { isPasswordVisible = !isPasswordVisible },
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector =
                        if (isPasswordVisible) {
                            Icons.Filled.VisibilityOff
                        } else {
                            Icons.Filled.Visibility
                        },
                    contentDescription = passwordVisibilityDescription,
                    tint = extendedColors.authFieldHint,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

private val FIELD_HEIGHT = 48.dp
private val PASSWORD_TOGGLE_SIZE = 40.dp
private val DEFAULT_BORDER_WIDTH = 1.dp
private val FOCUSED_BORDER_WIDTH = 1.5.dp
