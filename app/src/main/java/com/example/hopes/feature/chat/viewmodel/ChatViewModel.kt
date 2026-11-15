package com.example.hopes.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ChatEffect {
    /** 대화 생성은 상세 화면 자체에서 진행하므로, 질문만 들고 곧바로 상세 화면으로 이동한다. */
    data class NavigateToNewChat(
        val question: String,
        val department: ChatDepartment?,
        val major: ChatMajor?,
    ) : ChatEffect
}

@HiltViewModel
class ChatViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(ChatUiState())
        val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<ChatEffect>()
        val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

        /** 질문 입력·제출 이벤트를 처리한다. */
        fun onEvent(event: ChatScreenEvent) {
            when (event) {
                is ChatScreenEvent.QuestionChanged -> {
                    updateState {
                        copy(questionText = event.question)
                    }
                }

                ChatScreenEvent.QuestionSubmitted -> {
                    navigateToNewChat(_uiState.value.questionText)
                }

                is ChatScreenEvent.SuggestionClicked -> {
                    updateState { copy(questionText = event.question) }
                }

                ChatScreenEvent.NewChatClicked -> {
                    updateState { copy(questionText = "") }
                }

                is ChatScreenEvent.DepartmentSelected -> {
                    updateState { copy(department = event.department) }
                }

                is ChatScreenEvent.MajorSelected -> {
                    updateState { copy(major = event.major) }
                }

                // 추천 질문 API 연동 전이라 학과·전공에 맞춘 고정 질문을 그대로 둔다. 연동 시 여기서 다시 불러온다.
                ChatScreenEvent.RecommendationsRefreshClicked -> {
                    Unit
                }
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
                val currentState = _uiState.value
                _effect.emit(
                    ChatEffect.NavigateToNewChat(
                        question = trimmedQuestion,
                        department = currentState.department,
                        major = currentState.major,
                    ),
                )
            }
        }

        private fun updateState(transform: ChatUiState.() -> ChatUiState) {
            _uiState.value = _uiState.value.transform()
        }
    }
