package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/**
 * 한 줄 입력란이다. 포커스 시 primary, 오류 시 error 색 테두리를 표시한다.
 *
 * @param accessibilityLabel 화면 낭독기가 읽을 입력란 이름이다. 보통 위 라벨 문구를 넘긴다.
 * @param errorMessage null이 아니면 오류 상태로 그리고 접근성 오류로 알린다. 문구 자체는 [HopesInlineErrorMessage]로 따로 그린다.
 */
@Composable
fun HopesTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    accessibilityLabel: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    isEnabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
) {
    HopesInputBox(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        accessibilityLabel = accessibilityLabel,
        errorMessage = errorMessage,
        isEnabled = isEnabled,
        isSingleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        modifier = modifier.height(AppSize.Field),
        contentAlignment = Alignment.CenterStart,
    )
}

/**
 * 여러 줄 입력란이다. 최소 높이 [minHeight]에서 시작해 내용이 늘면 함께 늘어난다.
 */
@Composable
fun HopesTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    accessibilityLabel: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    isEnabled: Boolean = true,
    minHeight: Dp = DEFAULT_TEXT_AREA_MIN_HEIGHT,
) {
    HopesInputBox(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        accessibilityLabel = accessibilityLabel,
        errorMessage = errorMessage,
        isEnabled = isEnabled,
        isSingleLine = false,
        keyboardOptions = KeyboardOptions.Default,
        modifier = modifier.heightIn(min = minHeight),
        contentAlignment = Alignment.TopStart,
        verticalPadding = AppSpacing.Item,
    )
}

@Composable
private fun HopesInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    accessibilityLabel: String,
    errorMessage: String?,
    isEnabled: Boolean,
    isSingleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
    contentAlignment: Alignment,
    verticalPadding: Dp = 0.dp,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError = errorMessage != null
    val fieldShape = RoundedCornerShape(AppRadius.Field)
    val textStyle = HopesTextStyles.Field.copy(color = MaterialTheme.colorScheme.onSurface)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = accessibilityLabel
                    if (errorMessage != null) {
                        error(errorMessage)
                    }
                },
        enabled = isEnabled,
        singleLine = isSingleLine,
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        decorationBox = { innerTextField ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .then(if (isSingleLine) Modifier.height(AppSize.Field) else Modifier)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = fieldShape,
                        ).border(
                            width =
                                if (isFocused || isError) {
                                    AppBorderWidth.Emphasized
                                } else {
                                    AppBorderWidth.Default
                                },
                            color =
                                when {
                                    isError -> MaterialTheme.colorScheme.error
                                    isFocused -> MaterialTheme.colorScheme.primary
                                    else -> extendedColors.authFieldBorder
                                },
                            shape = fieldShape,
                        ).padding(horizontal = AppSpacing.Medium, vertical = verticalPadding),
                contentAlignment = contentAlignment,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = extendedColors.authFieldHint,
                        style = HopesTextStyles.Field,
                    )
                }

                innerTextField()
            }
        },
    )
}

private val DEFAULT_TEXT_AREA_MIN_HEIGHT = 120.dp
