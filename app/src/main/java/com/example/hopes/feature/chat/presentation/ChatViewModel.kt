package com.example.hopes.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.CreateChatUseCase
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
    data class ChatCreated(val chatId: Long) : ChatEffect
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val createChatUseCase: CreateChatUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    /** 질문 입력·제출 이벤트를 처리한다. */
    fun onEvent(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.QuestionChanged -> updateState {
                copy(questionText = event.question, isCreateChatError = false)
            }

            ChatScreenEvent.QuestionSubmitted -> createChat(_uiState.value.questionText)
            ChatScreenEvent.NewChatClicked -> updateState {
                copy(questionText = "", isCreateChatError = false)
            }
        }
    }

    /** 질문을 제목으로 서버 대화를 생성하고 성공 시 서버 ID로 상세 이동 효과를 발행한다. */
    private fun createChat(question: String) {
        val trimmedQuestion = question.trim()
        if (trimmedQuestion.isEmpty()) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, isCreateChatError = false) }
            when (val result = createChatUseCase(trimmedQuestion)) {
                is AppResult.Success -> {
                    updateState { copy(questionText = "", isLoading = false) }
                    _effect.emit(ChatEffect.ChatCreated(result.value.id))
                }

                else -> updateState { copy(isLoading = false, isCreateChatError = true) }
            }
        }
    }

    private fun updateState(transform: ChatUiState.() -> ChatUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
