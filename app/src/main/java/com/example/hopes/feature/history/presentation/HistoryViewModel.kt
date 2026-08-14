package com.example.hopes.feature.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.model.ChatSummary
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        loadFirstPage()
    }

    /** 검색어 변경 또는 재시도 시 첫 채팅 목록 페이지를 서버에서 갱신한다. */
    fun onEvent(event: HistoryScreenEvent) {
        when (event) {
            HistoryScreenEvent.NewChatClicked -> Unit

            is HistoryScreenEvent.SearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
                loadFirstPage(shouldDebounce = true)
            }

            is HistoryScreenEvent.ChatClicked -> Unit
            HistoryScreenEvent.RetryClicked -> loadFirstPage()
            HistoryScreenEvent.LoadNextPageClicked -> loadNextPage()
            HistoryScreenEvent.LoadNextPageRetryClicked -> loadNextPage()
        }
    }

    /** 화면 진입·검색어 변경·재시도 시 첫 페이지를 불러온다. */
    private fun loadFirstPage(shouldDebounce: Boolean = false) {
        loadChatsJob?.cancel()
        loadChatsJob = viewModelScope.launch {
            updateState {
                copy(
                    chats = emptyList(),
                    contentState = HistoryContentState.Loading,
                    nextPage = FIRST_PAGE,
                    hasNextPage = false,
                    isLoadingNextPage = false,
                    isNextPageError = false,
                )
            }

            if (shouldDebounce) {
                delay(SEARCH_DEBOUNCE_MILLIS)
            }

            val keyword = _uiState.value.searchQuery.ifBlank { null }
            when (val result = getChatsUseCase(keyword, FIRST_PAGE, PAGE_SIZE)) {
                is AppResult.Success -> updateState {
                    val chatUiModels = result.value.chats.map { chat ->
                        chat.toChatSummaryUiModel()
                    }
                    copy(
                        chats = chatUiModels,
                        contentState = if (chatUiModels.isEmpty()) {
                            HistoryContentState.Empty
                        } else {
                            HistoryContentState.Content
                        },
                        nextPage = FIRST_PAGE + 1,
                        hasNextPage = result.value.hasNextPage,
                    )
                }

                else -> updateState { copy(contentState = HistoryContentState.Error) }
            }
        }
    }

    /** 목록 하단 도달 시 다음 페이지를 기존 결과 뒤에 추가한다. */
    private fun loadNextPage() {
        val currentState = _uiState.value
        if (
            currentState.contentState != HistoryContentState.Content ||
            !currentState.hasNextPage ||
            currentState.isLoadingNextPage
        ) {
            return
        }

        loadChatsJob?.cancel()
        loadChatsJob = viewModelScope.launch {
            val requestedPage = _uiState.value.nextPage
            val keyword = _uiState.value.searchQuery.ifBlank { null }
            updateState {
                copy(
                    isLoadingNextPage = true,
                    isNextPageError = false,
                )
            }

            when (val result = getChatsUseCase(keyword, requestedPage, PAGE_SIZE)) {
                is AppResult.Success -> updateState {
                    copy(
                        chats = chats + result.value.chats.map { chat ->
                            chat.toChatSummaryUiModel()
                        },
                        nextPage = requestedPage + 1,
                        hasNextPage = result.value.hasNextPage,
                        isLoadingNextPage = false,
                    )
                }

                else -> updateState {
                    copy(
                        isLoadingNextPage = false,
                        isNextPageError = true,
                    )
                }
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
        const val PAGE_SIZE = 20
        const val SEARCH_DEBOUNCE_MILLIS = 300L
    }
}
