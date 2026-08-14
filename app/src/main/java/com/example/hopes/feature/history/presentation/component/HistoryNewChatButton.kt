package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing

/** 빈 채팅 화면으로 이동하는 기록 화면의 새 대화 버튼이다. */
@Composable
fun HistoryNewChatButton(onClick: () -> Unit) {
    val buttonShape = RoundedCornerShape(AppRadius.Button)

    Box(
        modifier = Modifier
            .padding(
                start = AppSpacing.ScreenHorizontal,
                top = HISTORY_NEW_CHAT_TOP_PADDING,
                end = AppSpacing.ScreenHorizontal,
            )
            .fillMaxWidth()
            .height(HISTORY_NEW_CHAT_HEIGHT)
            .border(1.dp, MaterialTheme.colorScheme.outline, buttonShape)
            .background(MaterialTheme.colorScheme.surface, buttonShape)
            .semantics { role = Role.Button }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                R.string.history_start_new_chat,
                stringResource(R.string.new_chat),
            ),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

private val HISTORY_NEW_CHAT_TOP_PADDING = 144.dp
private val HISTORY_NEW_CHAT_HEIGHT = 48.dp
