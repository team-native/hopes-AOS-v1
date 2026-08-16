package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaRaisedShadow

/** 메인 채팅 화면에서 Scaffold 하단에 고정되는 질문 입력창이다. */
@Composable
fun ChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sendDescription = stringResource(R.string.chat_send)

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(354.dp)
                .height(52.dp)
                .figmaRaisedShadow(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
        ) {
            if (value.isEmpty()) {
                Text(
                    text = stringResource(R.string.chat_new_message),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 14.dp, end = 54.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TextStyle(fontSize = 14.sp),
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 14.dp)
                    .width(276.dp)
                    .height(32.dp),
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        innerTextField()
                    }
                },
            )

            Box(
                modifier = Modifier
                    .padding(start = 306.dp, top = 11.dp)
                    .size(30.dp)
                    .figmaRaisedShadow(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp))
                    .semantics {
                        role = Role.Button
                        contentDescription = sendDescription
                    }
                    .clickable(onClick = onSubmitClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.chat_send_symbol),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 30.sp,
                    ),
                )
            }
        }
    }
}
