package com.example.hopes.feature.community.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityRepository
import com.example.hopes.feature.community.model.CommunityPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * @param results 검색어가 있을 때만 의미가 있다. 검색어가 비면 최근 검색어를 보여준다(피그마 16-2).
 */
data class CommunitySearchUiState(
    val query: String = "",
    val results: List<CommunityPost> = emptyList(),
    val recentSearches: List<String> = emptyList(),
    val status: CommunitySearchStatus = CommunitySearchStatus.RecentSearches,
)

enum class CommunitySearchStatus { RecentSearches, Editing, Loading, Results, Empty, LoadError }

/** 게시글 검색 API 결과를 [CommunityRepository] 화면 캐시에서 표시한다. */
@HiltViewModel
class CommunitySearchViewModel
    @Inject
    constructor(
        private val store: CommunityRepository,
    ) : ViewModel() {
        private val query = MutableStateFlow("")
        private val boardCohort = MutableStateFlow<Int?>(null)
        private val searchStatus = MutableStateFlow(CommunitySearchStatus.RecentSearches)

        val uiState: StateFlow<CommunitySearchUiState> =
            combine(store.posts, store.recentSearches, query, boardCohort, searchStatus) { posts, recentSearches, query, cohort, status ->
                CommunitySearchUiState(
                    query = query,
                    results = if (status == CommunitySearchStatus.Results) searchPosts(posts, query, cohort) else emptyList(),
                    recentSearches = recentSearches,
                    status = status,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = CommunitySearchUiState(),
            )

        /** @param cohort 검색할 게시판이다. null이면 모든 게시판에서 찾는다. */
        fun initialize(cohort: Int?) {
            boardCohort.value = cohort
        }

        fun changeQuery(text: String) {
            query.value = text
            searchStatus.value = if (text.isBlank()) CommunitySearchStatus.RecentSearches else CommunitySearchStatus.Editing
        }

        /** 키보드의 검색 키를 누르면 검색어를 최근 검색어에 남긴다. */
        fun submitQuery() {
            executeSearch(query.value)
        }

        fun selectRecentSearch(keyword: String) {
            query.value = keyword
            executeSearch(keyword)
        }

        fun retrySearch() {
            if (searchStatus.value == CommunitySearchStatus.LoadError) executeSearch(query.value)
        }

        private fun executeSearch(keyword: String) {
            val normalized = keyword.trim()
            if (normalized.isEmpty()) {
                searchStatus.value = CommunitySearchStatus.RecentSearches
                return
            }
            query.value = normalized
            store.addRecentSearch(normalized)
            searchStatus.value = CommunitySearchStatus.Loading
            viewModelScope.launch {
                val boardsLoaded = store.loadBoards()
                val postsLoaded = boardsLoaded && store.refreshPosts(boardCohort.value, normalized)
                if (query.value == normalized) {
                    searchStatus.value = searchStatusFor(postsLoaded, searchPosts(store.posts.first(), normalized, boardCohort.value))
                }
            }
        }

        /** 목록에서 하트를 누르면 좋아요를 켜고, 다시 누르면 끈다. */
        fun toggleLike(postId: Long) {
            viewModelScope.launch { store.toggleLike(postId) }
        }

        fun clearRecentSearches() {
            store.clearRecentSearches()
        }

        private companion object {
            const val STOP_TIMEOUT_MILLIS = 5_000L
        }
    }

/** [cohort] 게시판 글 중 제목이나 내용에 [query]가 들어간 글을 찾는다. 검색어가 비면 빈 목록이다. */
fun searchPosts(
    posts: List<CommunityPost>,
    query: String,
    cohort: Int?,
): List<CommunityPost> {
    val keyword = query.trim()
    if (keyword.isEmpty()) return emptyList()

    return posts.filter { post ->
        (cohort == null || post.cohort == cohort) &&
            (post.title.contains(keyword, ignoreCase = true) || post.content.contains(keyword, ignoreCase = true))
    }
}

internal fun searchStatusFor(
    succeeded: Boolean,
    results: List<CommunityPost>,
): CommunitySearchStatus =
    when {
        !succeeded -> CommunitySearchStatus.LoadError
        results.isEmpty() -> CommunitySearchStatus.Empty
        else -> CommunitySearchStatus.Results
    }
