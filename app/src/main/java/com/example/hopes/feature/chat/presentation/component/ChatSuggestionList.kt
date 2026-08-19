package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.CropSquare
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing

private data class ChatSuggestion(
    val icon: ImageVector,
    val textRes: Int,
    val iconRotationDegrees: Float = 0f,
)

private val chatSuggestions = listOf(
    ChatSuggestion(Icons.Outlined.Home, R.string.chat_suggestion_dormitory),
    // 입학 항목은 다이아몬드 모양이 아니라 45도 회전한 정사각형이라, CropSquare를 회전해 재사용한다.
    ChatSuggestion(Icons.Outlined.CropSquare, R.string.chat_suggestion_admission, iconRotationDegrees = 45f),
    ChatSuggestion(Icons.Outlined.Code, R.string.chat_suggestion_major),
    ChatSuggestion(Icons.Outlined.CropSquare, R.string.chat_suggestion_advice),
)

/** 채팅 홈에서 자주 묻는 질문 4가지를 세로로 배치하고, 클릭 시 해당 질문 텍스트를 전달한다. */
@Composable
fun ChatSuggestionList(
    onSuggestionClick: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        chatSuggestions.forEach { suggestion ->
            val text = stringResource(suggestion.textRes)
            ChatSuggestionChip(
                icon = suggestion.icon,
                text = text,
                onClick = { onSuggestionClick(text) },
                isLoading = isLoading,
                iconRotationDegrees = suggestion.iconRotationDegrees,
            )
        }
    }
}
