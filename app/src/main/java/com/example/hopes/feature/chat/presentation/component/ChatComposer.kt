package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
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
    isLoading: Boolean,
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
                .figmaRaisedShadow(RoundedCornerShape(41.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(41.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(41.dp)),
        ) {
            if (value.isEmpty()) {
                Text(
                    text = stringResource(R.string.chat_new_message),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 20.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TextStyle(fontSize = 14.sp),
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .width(254.dp),
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
                    .align(Alignment.CenterEnd)
                    .padding(end = 18.dp)
                    .size(48.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = sendDescription
                    }
                    .clickable(enabled = !isLoading, onClick = onSubmitClick),
                contentAlignment = Alignment.Center,
            ) {
                // 시각적 버튼은 30.dp로 유지하고, 바깥 48.dp 영역으로 터치 접근성을 보장한다.
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .figmaRaisedShadow(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.img),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}
