package com.example.hopes.feature.community.data

import com.example.hopes.core.connectivity.NetworkMonitor
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityMyComment
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.model.CommunityReportReason
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/** 신고 대상이다. 같은 글·댓글을 두 번 신고하지 않도록 구분해 기억한다. */
sealed interface CommunityReportTarget {
    data class Post(
        val postId: Long,
    ) : CommunityReportTarget

    data class Comment(
        val commentId: Long,
    ) : CommunityReportTarget
}

/**
 * 커뮤니티 API가 생기기 전까지 게시글·댓글·신고·최근 검색어를 앱 메모리에만 들고 있는 임시 저장소다.
 * 앱을 다시 켜면 처음 상태로 돌아간다. API를 붙일 때 이 클래스를 repository로 바꾸면 된다.
 *
 * 글 등록·수정과 댓글 등록·수정은 인터넷이 끊겨 있으면 실패로 돌려준다.
 * 실패 화면(피그마 14-9, 15-7)을 비행기 모드로 확인할 수 있게 하려는 것이다.
 */
@Singleton
class CommunityStore
    @Inject
    constructor(
        private val networkMonitor: NetworkMonitor,
    ) {
        // 임시 글을 넣는다. 확인이 끝나면 인자를 지운다(CommunityTempData.kt 참고).
        private val seed = CommunitySeed(posts = CommunityTempData.posts, comments = CommunityTempData.comments)
        private val postsState = MutableStateFlow(seed.posts)
        private val commentsState = MutableStateFlow(seed.comments)
        private val reportedTargets = MutableStateFlow<Set<CommunityReportTarget>>(emptySet())
        private val recentSearchesState = MutableStateFlow(seed.recentSearches)
        private var lastId = (seed.posts.map { it.id } + seed.comments.map { it.id }).maxOrNull() ?: 0L

        /** 서버 프로필에 기수 필드가 없어 쓰는 임시 내 기수다. 새 글·댓글의 작성자 기수가 된다. */
        val myCohort: Int = MY_COHORT

        /** 최신 글이 위로 오는 전체 게시글이다. 댓글 수는 실제 댓글 개수로 채운다. */
        val posts: Flow<List<CommunityPost>> =
            combine(postsState, commentsState) { posts, comments ->
                posts
                    .map { post -> post.copy(commentCount = comments.count { it.postId == post.id }) }
                    .sortedByDescending { it.createdAt }
            }

        val recentSearches: StateFlow<List<String>> = recentSearchesState.asStateFlow()

        val myPosts: Flow<List<CommunityPost>> = posts.map { posts -> posts.filter { it.isMine } }

        val myComments: Flow<List<CommunityMyComment>> =
            combine(postsState, commentsState) { posts, comments ->
                comments
                    .filter { it.isMine }
                    .sortedByDescending { it.createdAt }
                    .mapNotNull { comment ->
                        val post = posts.firstOrNull { it.id == comment.postId } ?: return@mapNotNull null
                        CommunityMyComment(comment = comment, postCohort = post.cohort, postTitle = post.title)
                    }
            }

        /** 글이 지워졌으면 null을 내보낸다. */
        fun post(postId: Long): Flow<CommunityPost?> = posts.map { posts -> posts.firstOrNull { it.id == postId } }

        /** 오래된 댓글이 위로 오는 댓글 목록이다. */
        fun comments(postId: Long): Flow<List<CommunityComment>> =
            commentsState.map { comments ->
                comments.filter { it.postId == postId }.sortedBy { it.createdAt }
            }

        /** 새 글을 등록하고 id를 돌려준다. 실패하면 null이다. */
        suspend fun createPost(
            cohort: Int?,
            title: String,
            content: String,
        ): Long? {
            if (!isOnline()) return null

            val id = nextId()
            postsState.update { posts ->
                posts +
                    CommunityPost(
                        id = id,
                        cohort = cohort,
                        authorCohort = myCohort,
                        title = title,
                        content = content,
                        createdAt = LocalDateTime.now(),
                        isMine = true,
                    )
            }
            return id
        }

        suspend fun updatePost(
            postId: Long,
            cohort: Int?,
            title: String,
            content: String,
        ): Boolean {
            if (!isOnline()) return false

            postsState.update { posts ->
                posts.map { post ->
                    if (post.id == postId) {
                        post.copy(cohort = cohort, title = title, content = content, isEdited = true)
                    } else {
                        post
                    }
                }
            }
            return true
        }

        /** 글과 그 글의 댓글을 함께 지운다. */
        fun deletePost(postId: Long) {
            postsState.update { posts -> posts.filterNot { it.id == postId } }
            commentsState.update { comments -> comments.filterNot { it.postId == postId } }
        }

        fun toggleLike(postId: Long) {
            postsState.update { posts ->
                posts.map { post ->
                    if (post.id != postId) return@map post
                    post.copy(
                        isLiked = !post.isLiked,
                        likeCount = post.likeCount + if (post.isLiked) -1 else 1,
                    )
                }
            }
        }

        suspend fun addComment(
            postId: Long,
            content: String,
        ): Boolean {
            if (!isOnline()) return false

            commentsState.update { comments ->
                comments +
                    CommunityComment(
                        id = nextId(),
                        postId = postId,
                        authorCohort = myCohort,
                        content = content,
                        createdAt = LocalDateTime.now(),
                        isMine = true,
                    )
            }
            return true
        }

        suspend fun updateComment(
            commentId: Long,
            content: String,
        ): Boolean {
            if (!isOnline()) return false

            commentsState.update { comments ->
                comments.map { comment ->
                    if (comment.id == commentId) comment.copy(content = content, isEdited = true) else comment
                }
            }
            return true
        }

        fun deleteComment(commentId: Long) {
            commentsState.update { comments -> comments.filterNot { it.id == commentId } }
        }

        fun isReported(target: CommunityReportTarget): Boolean = target in reportedTargets.value

        /** 신고를 접수한다. 사유는 API가 생기면 함께 보낸다. */
        @Suppress("UNUSED_PARAMETER")
        fun report(
            target: CommunityReportTarget,
            reason: CommunityReportReason,
        ) {
            reportedTargets.update { it + target }
        }

        /** 검색어를 최근 검색어 맨 앞에 넣는다. 같은 검색어는 한 번만 남기고 최대 [MAX_RECENT_SEARCHES]개까지 둔다. */
        fun addRecentSearch(query: String) {
            val keyword = query.trim()
            if (keyword.isEmpty()) return

            recentSearchesState.update { searches ->
                (listOf(keyword) + searches.filterNot { it == keyword }).take(MAX_RECENT_SEARCHES)
            }
        }

        fun clearRecentSearches() {
            recentSearchesState.value = emptyList()
        }

        private suspend fun isOnline(): Boolean = networkMonitor.isOnline.first()

        private fun nextId(): Long {
            lastId += 1
            return lastId
        }

        private companion object {
            const val MY_COHORT = 10
            const val MAX_RECENT_SEARCHES = 10
        }
    }

/** 저장소를 처음 채울 값이다. */
data class CommunitySeed(
    val posts: List<CommunityPost> = emptyList(),
    val comments: List<CommunityComment> = emptyList(),
    val recentSearches: List<String> = emptyList(),
)
