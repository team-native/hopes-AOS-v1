package com.example.hopes.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ChatEffect {
    /** 대화 생성은 상세 화면 자체에서 진행하므로, 질문만 들고 곧바로 상세 화면으로 이동한다. */
    data class NavigateToNewChat(val question: String) : ChatEffect
}

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    /** 질문 입력·제출 이벤트를 처리한다. */
    fun onEvent(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.QuestionChanged -> updateState {
                copy(questionText = event.question)
            }

            ChatScreenEvent.QuestionSubmitted -> navigateToNewChat(_uiState.value.questionText)
            is ChatScreenEvent.SuggestionClicked -> navigateToNewChat(event.question)
            ChatScreenEvent.NewChatClicked -> updateState { copy(questionText = "") }
        }
    }

    /** 질문을 곧바로 상세 화면으로 넘겨, 대화 생성·첫 메시지 전송을 상세 화면에서 진행하게 한다. */
    private fun navigateToNewChat(question: String) {
        val trimmedQuestion = question.trim()
        if (trimmedQuestion.isEmpty()) {
            return
        }

        viewModelScope.launch {
            updateState { copy(questionText = "") }
            _effect.emit(ChatEffect.NavigateToNewChat(trimmedQuestion))
        }
    }

    private fun updateState(transform: ChatUiState.() -> ChatUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
