package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 03 회원가입 카드의 가용 폭을 모두 사용하는 43dp 입력 행이다. */
@Composable
fun FigmaSignupField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false,
    onImeAction: (() -> Unit)? = null,
) {
    val extendedColors = LocalHopesExtendedColors.current

    // 카드 좌우 여백을 제외한 나머지 폭을 1f로 배분해 기기 폭 변화에도 필드 비율을 유지한다.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 17.dp, end = 18.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(43.dp)
                .border(
                    width = 1.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else extendedColors.authFieldBorder,
                    shape = RoundedCornerShape(14.dp),
                ),
        ) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    modifier = Modifier.padding(start = 12.dp, top = 11.dp),
                    color = extendedColors.authFieldHint,
                    style = TextStyle(fontSize = 15.sp),
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .padding(top = 11.dp)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
                    .height(24.dp),
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (isPassword) ImeAction.Done else ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onImeAction?.invoke() },
                ),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            )
        }
    }
}
