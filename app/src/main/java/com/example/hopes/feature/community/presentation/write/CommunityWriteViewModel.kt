package com.example.hopes.feature.community.presentation.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 새 글 등록과 내 글 수정을 맡는다. 글은 임시 저장소 [CommunityStore]에 저장한다. */
@HiltViewModel
class CommunityWriteViewModel
    @Inject
    constructor(
        private val store: CommunityRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CommunityWriteUiState())
        val uiState: StateFlow<CommunityWriteUiState> = _uiState.asStateFlow()

        private var isInitialized = false

        // 처음 값과 같으면 나갈 때 확인을 묻지 않는다(피그마 15-4).
        private var initialState = CommunityWriteUiState()

        /**
         * @param postId 수정할 글이다. null이면 새 글을 쓴다.
         * @param defaultCohort 새 글의 기본 게시판이다. null이면 내 기수 게시판으로 둔다.
         */
        fun initialize(
            postId: Long?,
            defaultCohort: Int?,
        ) {
            if (isInitialized) return
            isInitialized = true

            viewModelScope.launch {
                store.loadBoards()
                val cohorts = store.availableCohorts.value
                if (postId == null) {
                    val selectedCohort = defaultCohort?.takeIf { it in cohorts } ?: store.myCohort.takeIf { it in cohorts } ?: cohorts.firstOrNull()
                    initialState = CommunityWriteUiState(cohorts = cohorts, cohort = selectedCohort)
                    _uiState.value = initialState
                    return@launch
                }
                store.refreshPost(postId)
                val post = store.post(postId).first() ?: return@launch
                initialState =
                    CommunityWriteUiState(
                        editingPostId = post.id,
                        cohorts = cohorts,
                        cohort = post.cohort,
                        title = post.title,
                        content = post.content,
                    )
                _uiState.value = initialState
            }
        }

        fun onEvent(event: CommunityWriteEvent) {
            when (event) {
                CommunityWriteEvent.CloseClicked -> {
                    _uiState.update {
                        if (it.hasChanges()) {
                            it.copy(
                                isExitDialogVisible = true,
                            )
                        } else {
                            it.copy(result = CommunityWriteResult.Closed)
                        }
                    }
                }

                CommunityWriteEvent.ExitConfirmed -> {
                    _uiState.update { it.copy(isExitDialogVisible = false, result = CommunityWriteResult.Closed) }
                }

                CommunityWriteEvent.ExitDismissed -> {
                    _uiState.update { it.copy(isExitDialogVisible = false) }
                }

                CommunityWriteEvent.BoardClicked -> {
                    _uiState.update { if (it.isEditMode) it else it.copy(isBoardSheetVisible = true) }
                }

                is CommunityWriteEvent.BoardSelected -> {
                    _uiState.update { it.copy(cohort = event.cohort, isBoardSheetVisible = false) }
                }

                CommunityWriteEvent.BoardSheetDismissed -> {
                    _uiState.update { it.copy(isBoardSheetVisible = false) }
                }

                is CommunityWriteEvent.TitleChanged -> {
                    _uiState.update { it.copy(title = event.title, isTitleError = false) }
                }

                is CommunityWriteEvent.ContentChanged -> {
                    _uiState.update { it.copy(content = event.content, isContentError = false) }
                }

                CommunityWriteEvent.SubmitClicked -> {
                    submit()
                }
            }
        }

        private fun submit() {
            val state = _uiState.value
            if (state.isSubmitting) return

            val title = state.title.trim()
            val content = state.content.trim()
            if (title.isEmpty() || content.isEmpty()) {
                _uiState.update { it.copy(isTitleError = title.isEmpty(), isContentError = content.isEmpty()) }
                return
            }

            _uiState.update { it.copy(isSubmitting = true, isSubmitFailed = false) }

            viewModelScope.launch {
                val editingPostId = state.editingPostId
                val isSaved =
                    if (editingPostId == null) {
                        store.createPost(state.cohort, title, content) != null
                    } else {
                        store.updatePost(editingPostId, state.cohort, title, content)
                    }

                _uiState.update {
                    when {
                        !isSaved -> it.copy(isSubmitting = false, isSubmitFailed = true)
                        editingPostId == null -> it.copy(isSubmitting = false, result = CommunityWriteResult.Created)
                        else -> it.copy(isSubmitting = false, result = CommunityWriteResult.Updated)
                    }
                }
            }
        }

        private fun CommunityWriteUiState.hasChanges(): Boolean =
            cohort != initialState.cohort || title != initialState.title || content != initialState.content
    }
