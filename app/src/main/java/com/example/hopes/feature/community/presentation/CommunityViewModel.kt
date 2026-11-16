package com.example.hopes.feature.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/** 기수 필터에 맞는 게시글 목록을 만든다. 글은 임시 저장소 [CommunityStore]에서 읽는다. */
@HiltViewModel
class CommunityViewModel
    @Inject
    constructor(
        private val store: CommunityStore,
    ) : ViewModel() {
        private val selectedCohort = MutableStateFlow<Int?>(null)

        val uiState: StateFlow<CommunityUiState> =
            combine(store.posts, selectedCohort) { posts, cohort ->
                val boardPosts = if (cohort == null) posts else posts.filter { it.cohort == cohort }
                CommunityUiState(
                    selectedCohort = cohort,
                    posts = boardPosts,
                    contentState =
                        if (boardPosts.isEmpty()) CommunityContentState.Empty else CommunityContentState.Content,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = CommunityUiState(),
            )

        fun selectCohort(cohort: Int?) {
            selectedCohort.value = cohort
        }

        /** 목록에서 하트를 누르면 좋아요를 켜고, 다시 누르면 끈다. */
        fun toggleLike(postId: Long) {
            store.toggleLike(postId)
        }

        private companion object {
            const val STOP_TIMEOUT_MILLIS = 5_000L
        }
    }
