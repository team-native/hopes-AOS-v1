package com.example.hopes.feature.chat.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.feature.chat.view.component.ChatDepartmentSheet
import com.example.hopes.feature.chat.view.component.ChatMajorSheet
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatEffect
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.example.hopes.feature.chat.viewmodel.ChatScreenEvent
import com.example.hopes.feature.chat.viewmodel.ChatViewModel
import com.example.hopes.navigation.HopesDestination

/** 채팅 홈에서 열 수 있는 선택 시트다. */
private enum class ChatSelectionSheetType {
    Department,
    Major,
}

/**
 * 채팅 입력 상태를 소유하고 화면 이벤트를 처리한다.
 *
 * @param onNavigateToNewChatDetail 질문과 함께 상세 화면으로 이동한다. 학과·전공은 상세 화면 부제목에 쓴다.
 */
@Composable
fun ChatRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToNewChatDetail: (question: String, department: ChatDepartment?, major: ChatMajor?) -> Unit,
    isNewChatRequested: Boolean = false,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var visibleSheet by rememberSaveable { mutableStateOf<ChatSelectionSheetType?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            if (effect is ChatEffect.NavigateToNewChat) {
                onNavigateToNewChatDetail(effect.question, effect.department, effect.major)
            }
        }
    }

    LaunchedEffect(isNewChatRequested) {
        if (isNewChatRequested) {
            viewModel.onEvent(ChatScreenEvent.NewChatClicked)
        }
    }

    ChatScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onDepartmentClick = { visibleSheet = ChatSelectionSheetType.Department },
        onMajorClick = { visibleSheet = ChatSelectionSheetType.Major },
        onNavigate = onNavigate,
    )

    when (visibleSheet) {
        ChatSelectionSheetType.Department -> {
            ChatDepartmentSheet(
                selectedDepartment = uiState.department,
                onConfirm = { department ->
                    viewModel.onEvent(ChatScreenEvent.DepartmentSelected(department))
                    visibleSheet = null
                },
                onDismiss = { visibleSheet = null },
            )
        }

        ChatSelectionSheetType.Major -> {
            ChatMajorSheet(
                selectedMajor = uiState.major,
                onConfirm = { major ->
                    viewModel.onEvent(ChatScreenEvent.MajorSelected(major))
                    visibleSheet = null
                },
                onDismiss = { visibleSheet = null },
            )
        }

        null -> {
            Unit
        }
    }
}
