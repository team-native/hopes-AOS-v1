package com.example.hopes.feature.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch

/** 기수 필터에 맞는 서버 게시글 목록을 만든다. */
@HiltViewModel
class CommunityViewModel
    @Inject
    constructor(
        private val store: CommunityRepository,
    ) : ViewModel() {
        private val selectedCohort = MutableStateFlow<Int?>(null)
        private val contentState = MutableStateFlow(CommunityContentState.Loading)
        private val nextPageState = MutableStateFlow(CommunityNextPageState.Idle)

        init {
            loadPosts()
        }

        val uiState: StateFlow<CommunityUiState> =
            combine(store.posts, selectedCohort, contentState, store.availableCohorts, nextPageState) { posts, cohort, loadState, cohorts, pageState ->
                val boardPosts = if (cohort == null) posts else posts.filter { it.cohort == cohort }
                CommunityUiState(
                    selectedCohort = cohort,
                    cohorts = cohorts,
                    posts = boardPosts,
                    contentState = when (loadState) {
                        CommunityContentState.Loading, CommunityContentState.LoadError -> loadState
                        else -> if (boardPosts.isEmpty()) CommunityContentState.Empty else CommunityContentState.Content
                    },
                    nextPageState = pageState,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = CommunityUiState(),
            )

        fun selectCohort(cohort: Int?) {
            selectedCohort.value = cohort
            loadPosts()
        }

        /** 목록에서 하트를 누르면 좋아요를 켜고, 다시 누르면 끈다. */
        fun toggleLike(postId: Long) {
            viewModelScope.launch { store.toggleLike(postId) }
        }

        fun retry() = loadPosts()

        fun loadNextPage() {
            if (nextPageState.value != CommunityNextPageState.Idle) return
            requestNextPage()
        }

        fun retryNextPage() {
            if (nextPageState.value != CommunityNextPageState.Error) return
            requestNextPage()
        }

        private fun requestNextPage() {
            nextPageState.value = CommunityNextPageState.Loading
            viewModelScope.launch {
                val success = store.loadNextPostsPage(selectedCohort.value)
                nextPageState.value = if (success) CommunityNextPageState.Idle else CommunityNextPageState.Error
            }
        }

        private fun loadPosts() {
            viewModelScope.launch {
                contentState.value = CommunityContentState.Loading
                nextPageState.value = CommunityNextPageState.Idle
                val success = store.loadBoards() && store.refreshPosts(selectedCohort.value)
                contentState.value = if (success) CommunityContentState.Content else CommunityContentState.LoadError
            }
        }

        private companion object {
            const val STOP_TIMEOUT_MILLIS = 5_000L
        }
    }
