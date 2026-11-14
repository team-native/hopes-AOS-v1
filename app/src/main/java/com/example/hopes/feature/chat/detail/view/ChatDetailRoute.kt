package com.example.hopes.feature.chat.detail.view

import android.content.ClipData
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailViewModel
import com.example.hopes.feature.chat.view.label
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.teamnative.hopes.R
import kotlinx.coroutines.launch

/**
 * 서버 대화 상세 상태를 수집하고 뒤로가기·복사·공유를 연결한다.
 *
 * @param department 채팅 홈에서 고른 학과다. 학과·전공을 모두 알면 부제목에 쓰고, 아니면 기본 부제목을 쓴다.
 */
@Composable
fun ChatDetailRoute(
    chatId: Long,
    question: String,
    department: ChatDepartment?,
    major: ChatMajor?,
    onBackClick: () -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(chatId, question) { viewModel.initialize(chatId, question) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var toastMessageResId by rememberSaveable { mutableStateOf<Int?>(null) }
    val subtitle =
        if (department != null && major != null) {
            stringResource(R.string.chat_detail_subtitle, department.label(), major.label())
        } else {
            stringResource(R.string.chat_answer_label)
        }
    val shareTitle = stringResource(R.string.chat_share_answer)

    fun copyToClipboard(
        text: String,
        toastResId: Int,
    ) {
        coroutineScope.launch {
            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, text)))
            toastMessageResId = toastResId
        }
    }

    ChatDetailScreen(
        uiState = uiState,
        subtitle = subtitle,
        onEvent = { event ->
            if (event == ChatDetailScreenEvent.BackClicked) {
                onBackClick()
            } else {
                viewModel.onEvent(event)
            }
        },
        onCopyAnswer = { answer -> copyToClipboard(answer, R.string.chat_answer_copied_toast) },
        onShareAnswer = { answer ->
            val sendIntent =
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, answer)
                }
            context.startActivity(Intent.createChooser(sendIntent, shareTitle))
        },
        onCopyCode = { code -> copyToClipboard(code, R.string.chat_code_copied_toast) },
        toastMessage = toastMessageResId?.let { stringResource(it) },
        onToastDismiss = { toastMessageResId = null },
    )
}
