package com.example.hopes.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ChatSummary
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    private var loadChatsJob: Job? = null

    init {
        loadChats()
    }

    /** 검색어 변경 또는 재시도 시 첫 채팅 목록 페이지를 서버에서 갱신한다. */
    fun onEvent(event: HistoryScreenEvent) {
        when (event) {
            is HistoryScreenEvent.SearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
                loadChats()
            }

            is HistoryScreenEvent.ChatClicked -> Unit
            HistoryScreenEvent.RetryClicked -> loadChats()
        }
    }

    private fun loadChats() {
        loadChatsJob?.cancel()
        loadChatsJob = viewModelScope.launch {
            updateState { copy(isLoading = true, isError = false) }
            when (val result = getChatsUseCase(_uiState.value.searchQuery.ifBlank { null }, FIRST_PAGE, PAGE_SIZE)) {
                is AppResult.Success -> updateState {
                    copy(
                        chats = result.value.chats.map { chat ->
                            chat.toChatSummaryUiModel()
                        },
                        isLoading = false,
                    )
                }

                else -> updateState { copy(isLoading = false, isError = true) }
            }
        }
    }

    private fun ChatSummary.toChatSummaryUiModel(): ChatSummaryUiModel {
        return ChatSummaryUiModel(id = id, title = title)
    }

    private fun updateState(transform: HistoryUiState.() -> HistoryUiState) {
        _uiState.value = _uiState.value.transform()
    }

    private companion object {
        const val FIRST_PAGE = 0
        const val PAGE_SIZE = 20
    }
}
