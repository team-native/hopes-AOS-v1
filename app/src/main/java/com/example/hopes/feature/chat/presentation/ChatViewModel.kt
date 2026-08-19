package com.example.hopes.feature.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.CreateChatUseCase
import com.example.hopes.domain.usecase.SendChatMessageUseCase
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
    private val sendChatMessageUseCase: SendChatMessageUseCase,
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
            is ChatScreenEvent.SuggestionClicked -> createChat(event.question)
            ChatScreenEvent.NewChatClicked -> updateState {
                copy(questionText = "", isCreateChatError = false)
            }
        }
    }

    /** 질문 제출 시 서버 대화를 생성한 뒤 첫 메시지를 전송하고 성공 시 상세로 이동한다. */
    private fun createChat(question: String) {
        val trimmedQuestion = question.trim()
        if (trimmedQuestion.isEmpty() || _uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, isCreateChatError = false) }
            when (val result = createChatUseCase(trimmedQuestion)) {
                is AppResult.Success -> {
                    sendInitialMessage(
                        chatId = result.value.id,
                        question = trimmedQuestion,
                    )
                }

                else -> updateState { copy(isLoading = false, isCreateChatError = true) }
            }
        }
    }

    /** 새 대화에 첫 질문을 서버로 전송해 서버 메시지가 생성된 뒤 상세 화면으로 이동한다. */
    private suspend fun sendInitialMessage(
        chatId: Long,
        question: String,
    ) {
        when (sendChatMessageUseCase(chatId, question)) {
            is AppResult.Success -> {
                updateState { copy(questionText = "", isLoading = false) }
                _effect.emit(ChatEffect.ChatCreated(chatId))
            }

            else -> updateState { copy(isLoading = false, isCreateChatError = true) }
        }
    }

    private fun updateState(transform: ChatUiState.() -> ChatUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
