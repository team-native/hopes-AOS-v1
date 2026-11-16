package com.example.hopes.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetProfileUseCase
import com.example.hopes.feature.community.data.CommunityStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 홈 인사말에 쓸 프로필 이름을 불러온다. 실패하면 이름 없는 인사말을 그대로 둔다. */
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getProfileUseCase: GetProfileUseCase,
        private val communityStore: CommunityStore,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
        val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

        init {
            loadUserName()
            observePopularPosts()
        }

        /** 하트를 누르면 좋아요를 켜고, 다시 누르면 끈다. 커뮤니티 목록과 같은 저장소를 쓰므로 두 화면의 숫자가 같다. */
        fun toggleLike(postId: Long) {
            communityStore.toggleLike(postId)
        }

        /** 좋아요가 많은 글 두 개를 인기글로 보여준다. */
        private fun observePopularPosts() {
            viewModelScope.launch {
                communityStore.posts.collect { posts ->
                    val popularPosts =
                        posts
                            .sortedByDescending { it.likeCount }
                            .take(POPULAR_POST_COUNT)
                            .map { HomePopularPost(it.id, it.cohort, it.title, it.likeCount, it.isLiked) }
                    _uiState.update { state -> state.copy(popularPosts = popularPosts) }
                }
            }
        }

        private fun loadUserName() {
            viewModelScope.launch {
                val result = getProfileUseCase()
                _uiState.update { state ->
                    state.copy(
                        userName = (result as? AppResult.Success)?.value?.nickname?.toHomeGreetingName(),
                        isLoading = false,
                    )
                }
            }
        }

        private companion object {
            const val POPULAR_POST_COUNT = 2
        }
    }
