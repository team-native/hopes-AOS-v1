package com.example.hopes.feature.community.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityStore
import com.example.hopes.feature.community.model.CommunityPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * @param results 검색어가 있을 때만 의미가 있다. 검색어가 비면 최근 검색어를 보여준다(피그마 16-2).
 */
data class CommunitySearchUiState(
    val query: String = "",
    val results: List<CommunityPost> = emptyList(),
    val recentSearches: List<String> = emptyList(),
)

/** 게시글 제목·내용에서 검색어를 찾는다. 글은 임시 저장소 [CommunityStore]에서 읽는다. */
@HiltViewModel
class CommunitySearchViewModel
    @Inject
    constructor(
        private val store: CommunityStore,
    ) : ViewModel() {
        private val query = MutableStateFlow("")
        private val boardCohort = MutableStateFlow<Int?>(null)

        val uiState: StateFlow<CommunitySearchUiState> =
            combine(store.posts, store.recentSearches, query, boardCohort) { posts, recentSearches, query, cohort ->
                CommunitySearchUiState(
                    query = query,
                    results = searchPosts(posts, query, cohort),
                    recentSearches = recentSearches,
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
        }

        /** 키보드의 검색 키를 누르면 검색어를 최근 검색어에 남긴다. */
        fun submitQuery() {
            store.addRecentSearch(query.value)
        }

        fun selectRecentSearch(keyword: String) {
            query.value = keyword
            store.addRecentSearch(keyword)
        }

        /** 목록에서 하트를 누르면 좋아요를 켜고, 다시 누르면 끈다. */
        fun toggleLike(postId: Long) {
            store.toggleLike(postId)
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
