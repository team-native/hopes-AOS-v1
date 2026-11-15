package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.CountChatsUseCase
import com.example.hopes.domain.usecase.GetProfileUseCase
import com.example.hopes.feature.community.data.CommunityStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 마이페이지 프로필 요약과 활동 통계를 불러온다. 프로필 수정은 ProfileEditViewModel이 맡는다.
 * 작성글·댓글 수는 커뮤니티 저장소의 내 글·내 댓글 수를 따라가고, 대화 수는 서버 대화 목록을 세어 채운다.
 */
@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val getProfileUseCase: GetProfileUseCase,
        private val countChatsUseCase: CountChatsUseCase,
        communityStore: CommunityStore,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MyPageUiState())
        val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

        init {
            refreshProfile()
            viewModelScope.launch {
                combine(communityStore.myPosts, communityStore.myComments) { posts, comments ->
                    posts.size to comments.size
                }.collect { (postCount, commentCount) ->
                    updateStats { copy(postCount = postCount, commentCount = commentCount) }
                }
            }
        }

        /**
         * 내 대화 수를 다시 센다. 마이페이지에 들어올 때마다 불러 새 대화·삭제가 반영되게 한다.
         * 실패하면 이전 값을 그대로 둔다.
         */
        fun refreshChatCount() {
            viewModelScope.launch {
                val result = countChatsUseCase()
                if (result is AppResult.Success) {
                    updateStats { copy(chatCount = result.value) }
                }
            }
        }

        private fun updateStats(transform: MyPageActivityStats.() -> MyPageActivityStats) {
            _uiState.update { it.copy(activityStats = (it.activityStats ?: MyPageActivityStats()).transform()) }
        }

        /** 프로필 수정 화면에서 돌아왔을 때처럼 서버 프로필을 다시 읽어 요약을 갱신한다. */
        fun refreshProfile() {
            viewModelScope.launch {
                _uiState.update { it.copy(isProfileLoading = true, isProfileLoadFailed = false) }
                when (val result = getProfileUseCase()) {
                    is AppResult.Success -> {
                        val profile = result.value
                        _uiState.update {
                            it.copy(
                                profileName = profile.nickname,
                                email = profile.email,
                                major = profile.major,
                                isProfileLoading = false,
                            )
                        }
                    }

                    else -> {
                        _uiState.update { it.copy(isProfileLoading = false, isProfileLoadFailed = true) }
                    }
                }
            }
        }
    }
