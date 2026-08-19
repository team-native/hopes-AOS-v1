package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 온보딩 화면 하단의 채팅 시작 CTA 버튼이다. */
@Composable
fun HomeStartChatButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(338.dp)
            .height(46.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .semantics { role = Role.Button }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.start_chat),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}
