package com.example.hopes.feature.community.presentation.myactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityStore
import com.example.hopes.feature.community.model.CommunityMyComment
import com.example.hopes.feature.community.model.CommunityPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

enum class CommunityMyActivityTab {
    Posts,
    Comments,
}

/**
 * 내 활동 화면 상태다(피그마 18, 18-2, 18-3).
 *
 * @param morePostId "…"를 누른 내 글이다. 수정·삭제 시트를 띄운다.
 * @param pendingDeletePostId 삭제 확인을 묻는 글이다.
 */
data class CommunityMyActivityUiState(
    val selectedTab: CommunityMyActivityTab = CommunityMyActivityTab.Posts,
    val posts: List<CommunityPost> = emptyList(),
    val comments: List<CommunityMyComment> = emptyList(),
    val morePostId: Long? = null,
    val pendingDeletePostId: Long? = null,
)

/** 내가 쓴 글과 댓글을 보여준다. 글은 임시 저장소 [CommunityStore]에서 읽는다. */
@HiltViewModel
class CommunityMyActivityViewModel
    @Inject
    constructor(
        private val store: CommunityStore,
    ) : ViewModel() {
        private val screenState = MutableStateFlow(CommunityMyActivityUiState())

        val uiState: StateFlow<CommunityMyActivityUiState> =
            combine(screenState, store.myPosts, store.myComments) { state, posts, comments ->
                state.copy(posts = posts, comments = comments)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = CommunityMyActivityUiState(),
            )

        fun selectTab(tab: CommunityMyActivityTab) {
            screenState.value = screenState.value.copy(selectedTab = tab)
        }

        fun openPostMore(postId: Long) {
            screenState.value = screenState.value.copy(morePostId = postId)
        }

        fun closePostMore() {
            screenState.value = screenState.value.copy(morePostId = null)
        }

        /** 목록에서 하트를 누르면 좋아요를 켜고, 다시 누르면 끈다. */
        fun toggleLike(postId: Long) {
            store.toggleLike(postId)
        }

        fun requestDeletePost(postId: Long) {
            screenState.value = screenState.value.copy(morePostId = null, pendingDeletePostId = postId)
        }

        fun dismissDeletePost() {
            screenState.value = screenState.value.copy(pendingDeletePostId = null)
        }

        fun confirmDeletePost() {
            val postId = screenState.value.pendingDeletePostId ?: return
            store.deletePost(postId)
            screenState.value = screenState.value.copy(pendingDeletePostId = null)
        }

        private companion object {
            const val STOP_TIMEOUT_MILLIS = 5_000L
        }
    }
