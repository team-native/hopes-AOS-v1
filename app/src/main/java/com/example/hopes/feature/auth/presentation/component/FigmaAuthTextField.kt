package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 로그인 시트의 40dp 입력 필드다. */
@Composable
fun FigmaAuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelRes: Int,
    isPassword: Boolean = false,
    onImeAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val fieldTextStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
    )
    val fieldDescription = stringResource(labelRes)

    Box(
        modifier = modifier
            // Figma 02 입력 필드의 실제 폭은 332dp이며 로그인 버튼(338dp)보다 6dp 좁다.
            .width(332.dp)
            .height(40.dp)
            .border(
                width = 1.dp,
                color = extendedColors.authFieldBorder,
                shape = RoundedCornerShape(14.dp),
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        if (value.isEmpty()) {
            Text(
                text = stringResource(labelRes),
                color = extendedColors.authFieldHint,
                style = fieldTextStyle,
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = if (isPassword) 24.dp else 0.dp)
                .semantics { contentDescription = fieldDescription },
            singleLine = true,
            textStyle = fieldTextStyle,
            keyboardOptions = KeyboardOptions(
                imeAction = if (isPassword) ImeAction.Done else ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction?.invoke() },
            ),
            visualTransformation = if (isPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
        )

        if (isPassword) {
            Image(
                painter = painterResource(R.drawable.figma_auth_password_eye),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(width = 16.dp, height = 11.dp),
            )
        }
    }
}
