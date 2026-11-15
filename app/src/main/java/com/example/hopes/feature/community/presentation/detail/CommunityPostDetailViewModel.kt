package com.example.hopes.feature.community.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.community.data.CommunityReportTarget
import com.example.hopes.feature.community.data.CommunityStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/** 게시글 하나와 댓글을 보여주고 좋아요·댓글·신고·삭제를 처리한다. 글은 임시 저장소 [CommunityStore]에서 읽는다. */
@HiltViewModel
class CommunityPostDetailViewModel
    @Inject
    constructor(
        private val store: CommunityStore,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CommunityPostDetailUiState())
        val uiState: StateFlow<CommunityPostDetailUiState> = _uiState.asStateFlow()

        val myCohort: Int = store.myCohort

        private var postId: Long? = null
        private var observeJob: Job? = null
        private var lastFailedLocalId = 0L

        /** NavKey로 받은 글 id를 한 번만 반영해 글과 댓글을 지켜보기 시작한다. */
        fun initialize(postId: Long) {
            if (this.postId == postId) return
            this.postId = postId

            observeJob?.cancel()
            observeJob =
                viewModelScope.launch {
                    combine(store.post(postId), store.comments(postId)) { post, comments -> post to comments }
                        .collect { (post, comments) ->
                            _uiState.update { it.copy(isLoading = false, post = post, comments = comments) }
                        }
                }
        }

        fun onEvent(event: CommunityPostDetailEvent) {
            val currentPostId = postId ?: return

            when (event) {
                CommunityPostDetailEvent.LikeClicked -> {
                    store.toggleLike(currentPostId)
                }

                CommunityPostDetailEvent.PostMoreClicked -> {
                    _uiState.update { it.copy(sheet = CommunityDetailSheet.PostMore) }
                }

                is CommunityPostDetailEvent.CommentMoreClicked -> {
                    _uiState.update { it.copy(sheet = CommunityDetailSheet.CommentMore(event.comment)) }
                }

                CommunityPostDetailEvent.SheetDismissed,
                CommunityPostDetailEvent.EditPostClicked,
                -> {
                    _uiState.update { it.copy(sheet = null) }
                }

                CommunityPostDetailEvent.DeletePostClicked -> {
                    _uiState.update { it.copy(sheet = null, dialog = CommunityDetailDialog.DeletePost) }
                }

                CommunityPostDetailEvent.ReportPostClicked -> {
                    openReport(CommunityReportTarget.Post(currentPostId), CommunityDetailToast.AlreadyReportedPost)
                }

                is CommunityPostDetailEvent.EditCommentClicked -> {
                    _uiState.update {
                        it.copy(
                            sheet = null,
                            editingCommentId = event.comment.id,
                            commentInput = event.comment.content,
                        )
                    }
                }

                is CommunityPostDetailEvent.DeleteCommentClicked -> {
                    _uiState.update {
                        it.copy(sheet = null, dialog = CommunityDetailDialog.DeleteComment(event.commentId))
                    }
                }

                is CommunityPostDetailEvent.ReportCommentClicked -> {
                    openReport(
                        CommunityReportTarget.Comment(event.commentId),
                        CommunityDetailToast.AlreadyReportedComment,
                    )
                }

                is CommunityPostDetailEvent.ReportSubmitted -> {
                    val target = (_uiState.value.sheet as? CommunityDetailSheet.Report)?.target ?: return
                    store.report(target, event.reason)
                    _uiState.update { it.copy(sheet = CommunityDetailSheet.ReportDone) }
                }

                CommunityPostDetailEvent.DialogDismissed -> {
                    _uiState.update { it.copy(dialog = null) }
                }

                CommunityPostDetailEvent.DeletePostConfirmed -> {
                    store.deletePost(currentPostId)
                    _uiState.update { it.copy(dialog = null, isPostDeleted = true) }
                }

                CommunityPostDetailEvent.DeleteCommentConfirmed -> {
                    val dialog = _uiState.value.dialog as? CommunityDetailDialog.DeleteComment ?: return
                    store.deleteComment(dialog.commentId)
                    _uiState.update {
                        it.copy(
                            dialog = null,
                            // 수정 중이던 댓글을 지웠으면 수정도 끝낸다.
                            editingCommentId = it.editingCommentId.takeUnless { id -> id == dialog.commentId },
                            commentInput = if (it.editingCommentId == dialog.commentId) "" else it.commentInput,
                        )
                    }
                }

                is CommunityPostDetailEvent.CommentInputChanged -> {
                    _uiState.update { it.copy(commentInput = event.text) }
                }

                CommunityPostDetailEvent.CommentSendClicked -> {
                    sendComment(currentPostId)
                }

                CommunityPostDetailEvent.CommentEditCancelled -> {
                    _uiState.update { it.copy(editingCommentId = null, commentInput = "") }
                }

                is CommunityPostDetailEvent.FailedCommentRetryClicked -> {
                    retryComment(currentPostId, event.localId)
                }

                CommunityPostDetailEvent.ToastDismissed -> {
                    _uiState.update { it.copy(toast = null) }
                }

                CommunityPostDetailEvent.BackClicked,
                CommunityPostDetailEvent.BackToListClicked,
                -> {
                    Unit
                }
            }
        }

        private fun openReport(
            target: CommunityReportTarget,
            alreadyReportedToast: CommunityDetailToast,
        ) {
            _uiState.update {
                if (store.isReported(target)) {
                    it.copy(sheet = null, toast = alreadyReportedToast)
                } else {
                    it.copy(sheet = CommunityDetailSheet.Report(target))
                }
            }
        }

        private fun sendComment(postId: Long) {
            val state = _uiState.value
            val content = state.commentInput.trim()
            if (content.isEmpty() || state.isSendingComment) return

            val editingCommentId = state.editingCommentId
            _uiState.update { it.copy(isSendingComment = true) }

            viewModelScope.launch {
                if (editingCommentId != null) {
                    val isUpdated = store.updateComment(editingCommentId, content)
                    _uiState.update {
                        if (isUpdated) {
                            it.copy(isSendingComment = false, editingCommentId = null, commentInput = "")
                        } else {
                            it.copy(isSendingComment = false, toast = CommunityDetailToast.CommentEditFailed)
                        }
                    }
                    return@launch
                }

                val isAdded = store.addComment(postId, content)
                _uiState.update {
                    it.copy(
                        isSendingComment = false,
                        commentInput = "",
                        failedComments =
                            if (isAdded) {
                                it.failedComments
                            } else {
                                it.failedComments + newFailedComment(content)
                            },
                    )
                }
            }
        }

        private fun retryComment(
            postId: Long,
            localId: Long,
        ) {
            val failed = _uiState.value.failedComments.firstOrNull { it.localId == localId } ?: return

            viewModelScope.launch {
                if (store.addComment(postId, failed.content)) {
                    _uiState.update {
                        it.copy(
                            failedComments =
                                it.failedComments.filterNot { c ->
                                    c.localId == localId
                                },
                        )
                    }
                }
            }
        }

        private fun newFailedComment(content: String): CommunityFailedComment {
            lastFailedLocalId += 1
            return CommunityFailedComment(
                localId = lastFailedLocalId,
                content = content,
                createdAt = LocalDateTime.now(),
            )
        }
    }
