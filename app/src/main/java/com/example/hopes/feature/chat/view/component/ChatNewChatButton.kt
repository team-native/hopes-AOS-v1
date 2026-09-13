package com.example.hopes.feature.chat.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamnative.hopes.R
import com.example.hopes.core.designsystem.component.shapeClickable

/** 채팅 홈 헤더 우측의 새 채팅 시작 버튼이다. */
@Composable
fun ChatNewChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonShape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .width(70.dp)
            .height(38.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, buttonShape)
            .background(MaterialTheme.colorScheme.surface, buttonShape)
            .shapeClickable(shape = buttonShape, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.new_chat),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}
