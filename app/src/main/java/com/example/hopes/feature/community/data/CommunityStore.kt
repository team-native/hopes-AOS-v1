package com.example.hopes.feature.community.data

import com.example.hopes.core.connectivity.NetworkMonitor
import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.CommunityCommentRequestDto
import com.example.hopes.data.api.CommunityPostRequestDto
import com.example.hopes.data.api.CommunityPostUpdateDto
import com.example.hopes.data.api.CommunityReportRequestDto
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityMyComment
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.model.CommunityReportReason
import com.example.hopes.feature.community.model.COMMUNITY_COHORTS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
 * 커뮤니티 remote repository facade다. 최근 검색어와 화면 캐시는 앱 메모리에 유지한다.
 *
 * 글 등록·수정과 댓글 등록·수정은 인터넷이 끊겨 있으면 실패로 돌려준다.
 * 실패 화면(피그마 14-9, 15-7)을 비행기 모드로 확인할 수 있게 하려는 것이다.
 */
@Singleton
class CommunityStore
    @Inject
    constructor(
        private val networkMonitor: NetworkMonitor,
        private val remoteDataSource: CommunityRemoteDataSource,
    ) : CommunityRepository {
        private val seed = CommunitySeed()
        private val postsState = MutableStateFlow(seed.posts)
        private val myPostsState = MutableStateFlow<List<CommunityPost>>(emptyList())
        private val commentsState = MutableStateFlow(seed.comments)
        private val myCommentsState = MutableStateFlow<List<CommunityMyComment>>(emptyList())
        private val reportedTargets = MutableStateFlow<Set<CommunityReportTarget>>(emptySet())
        private val recentSearchesState = MutableStateFlow(seed.recentSearches)
        private val availableCohortsState = MutableStateFlow(COMMUNITY_COHORTS)
        private var boardIdsByCohort: Map<Int?, Long> = emptyMap()
        private var lastPostsPage = 0
        private var hasNextPostsPage = false
        private var lastPostsKeyword: String? = null
        private var isLoadingNextPostsPage = false

        override suspend fun loadBoards(): Boolean {
            val response = remoteDataSource.boards()
            if (response !is NetworkResult.Success) return false
            val boards = response.value.values.flatten().sortedBy { it.sortOrder }
            boardIdsByCohort = boards.associate { it.cohort to it.id }
            availableCohortsState.value = boards.mapNotNull { it.cohort }.distinct().sorted()
            return boards.isNotEmpty()
        }

        override suspend fun refreshPosts(cohort: Int?, keyword: String?, page: Int): Boolean {
            if (boardIdsByCohort.isEmpty()) loadBoards()
            val result = remoteDataSource.posts(boardIdsByCohort[cohort], keyword, page, PAGE_SIZE)
            if (result !is NetworkResult.Success) return false
            if (result.value.page != page) return false
            val posts = result.value.items.map { it.toCommunityPost(boardIdsByCohort.entries.firstOrNull { entry -> entry.value == it.boardId }?.key) }
            if (page == 0) {
                postsState.value = posts
                lastPostsKeyword = keyword
            } else {
                postsState.update { existing -> existing + posts.filterNot { post -> existing.any { it.id == post.id } } }
            }
            lastPostsPage = page
            hasNextPostsPage = result.value.hasNext
            return true
        }

        override suspend fun loadNextPostsPage(cohort: Int?): Boolean {
            if (!hasNextPostsPage || isLoadingNextPostsPage) return true
            isLoadingNextPostsPage = true
            return try {
                refreshPosts(cohort, lastPostsKeyword, lastPostsPage + 1)
            } finally {
                isLoadingNextPostsPage = false
            }
        }

        override suspend fun refreshPost(postId: Long): Boolean {
            val result = remoteDataSource.post(postId)
            if (result !is NetworkResult.Success) return false
            val item = result.value
            val post = item.toCommunityPost(boardIdsByCohort.entries.firstOrNull { it.value == item.boardId }?.key)
            postsState.update { current -> current.filterNot { it.id == post.id } + post }
            if (post.isMine) myPostsState.update { current -> current.filterNot { it.id == post.id } + post }
            return true
        }

        override suspend fun refreshComments(postId: Long): Boolean {
            val allComments = mutableListOf<com.example.hopes.data.api.CommunityCommentDto>()
            var page = 0
            var hasNext: Boolean
            do {
                if (page >= MAX_PAGES) return false
                val result = remoteDataSource.comments(postId, page, PAGE_SIZE)
                if (result !is NetworkResult.Success) return false
                if (result.value.page != page) return false
                allComments += result.value.items
                hasNext = result.value.hasNext
                page++
            } while (hasNext)
            commentsState.update { existing ->
                existing.filterNot { it.postId == postId } + allComments.map { item ->
                    item.toCommunityComment(postId)
                }
            }
            return true
        }

        /** 서버 프로필에 기수 필드가 없어 쓰는 임시 내 기수다. 새 글·댓글의 작성자 기수가 된다. */
        override val myCohort: Int = MY_COHORT
        override val availableCohorts: StateFlow<List<Int>> = availableCohortsState.asStateFlow()

        /** 최신 글이 위로 오는 전체 게시글이다. 댓글 수는 실제 댓글 개수로 채운다. */
        override val posts: Flow<List<CommunityPost>> =
            combine(postsState, commentsState) { posts, comments ->
                posts
                    .map { post -> if (comments.any { it.postId == post.id }) post.copy(commentCount = comments.count { it.postId == post.id }.toLong()) else post }
                    .sortedByDescending { it.createdAt }
            }

        override val recentSearches: StateFlow<List<String>> = recentSearchesState.asStateFlow()

        override val myPosts: Flow<List<CommunityPost>> = myPostsState.asStateFlow()

        override val myComments: Flow<List<CommunityMyComment>> = myCommentsState.asStateFlow()

        override suspend fun refreshMyPosts(): Boolean {
            if (boardIdsByCohort.isEmpty()) loadBoards()
            val allItems = mutableListOf<com.example.hopes.data.api.CommunityPostPreviewDto>()
            var page = 0
            var hasNext: Boolean
            do {
                if (page >= MAX_PAGES) return false
                val result = remoteDataSource.myPosts(page, PAGE_SIZE)
                if (result !is NetworkResult.Success) return false
                if (result.value.page != page) return false
                allItems += result.value.items
                hasNext = result.value.hasNext
                page++
            } while (hasNext)
            myPostsState.value = allItems.map { it.toCommunityPost(boardIdsByCohort.entries.firstOrNull { entry -> entry.value == it.boardId }?.key) }
            return true
        }

        override suspend fun refreshMyComments(): Boolean {
            val allItems = mutableListOf<com.example.hopes.data.api.CommunityMyCommentDto>()
            var page = 0
            var hasNext: Boolean
            do {
                if (page >= MAX_PAGES) return false
                val result = remoteDataSource.myComments(page, PAGE_SIZE)
                if (result !is NetworkResult.Success) return false
                if (result.value.page != page) return false
                allItems += result.value.items
                hasNext = result.value.hasNext
                page++
            } while (hasNext)
            myCommentsState.value = allItems.map { it.toCommunityMyComment(myCohort) }
            return true
        }

        /** 글이 지워졌으면 null을 내보낸다. */
        override fun post(postId: Long): Flow<CommunityPost?> = posts.map { posts -> posts.firstOrNull { it.id == postId } }

        /** 오래된 댓글이 위로 오는 댓글 목록이다. */
        override fun comments(postId: Long): Flow<List<CommunityComment>> =
            commentsState.map { comments ->
                comments.filter { it.postId == postId }.sortedBy { it.createdAt }
            }

        /** 새 글을 등록하고 id를 돌려준다. 실패하면 null이다. */
        override suspend fun createPost(
            cohort: Int?,
            title: String,
            content: String,
        ): Long? {
            if (!isOnline()) return null

            val boardId = boardIdsByCohort[cohort] ?: return null
            val result = remoteDataSource.createPost(CommunityPostRequestDto(boardId, title, content))
            if (result !is NetworkResult.Success) return null
            val createdPost = result.value.toCommunityPost(cohort)
            postsState.update { current -> listOf(createdPost) + current.filterNot { it.id == createdPost.id } }
            if (createdPost.isMine) {
                myPostsState.update { current -> listOf(createdPost) + current.filterNot { it.id == createdPost.id } }
            }
            // POST 응답이 생성 성공의 기준이다. 목록 GET 실패가 등록 실패로 보이지 않도록
            // 먼저 두 캐시에 반영하고, 목록 동기화는 best-effort로 수행한다.
            refreshPosts(cohort)
            return createdPost.id
        }

        override suspend fun updatePost(
            postId: Long,
            cohort: Int?,
            title: String,
            content: String,
        ): Boolean {
            if (!isOnline()) return false

            val result = remoteDataSource.updatePost(postId, CommunityPostUpdateDto(title, content))
            if (result !is NetworkResult.Success) return false
            return refreshPost(postId)
        }

        /** 글과 그 글의 댓글을 함께 지운다. */
        override suspend fun deletePost(postId: Long): Boolean {
            val result = remoteDataSource.deletePost(postId)
            if (result !is NetworkResult.Success) return false
            postsState.update { posts -> posts.filterNot { it.id == postId } }
            myPostsState.update { posts -> posts.filterNot { it.id == postId } }
            commentsState.update { comments -> comments.filterNot { it.postId == postId } }
            myCommentsState.update { comments -> comments.filterNot { it.comment.postId == postId } }
            return true
        }

        override suspend fun toggleLike(postId: Long): Boolean {
            val originalFeed = postsState.value.firstOrNull { it.id == postId }
            val originalMyPost = myPostsState.value.firstOrNull { it.id == postId }
            val original = originalFeed ?: originalMyPost ?: return false
            postsState.update { posts -> posts.map { if (it.id == postId) it.copy(isLiked = !it.isLiked, likeCount = (it.likeCount + if (it.isLiked) -1L else 1L).coerceAtLeast(0L)) else it } }
            myPostsState.update { posts -> posts.map { if (it.id == postId) it.copy(isLiked = !it.isLiked, likeCount = (it.likeCount + if (it.isLiked) -1L else 1L).coerceAtLeast(0L)) else it } }
            val result = remoteDataSource.setLike(postId, !original.isLiked)
            if (result !is NetworkResult.Success) {
                originalFeed?.let { feedPost -> postsState.update { posts -> posts.map { if (it.id == postId) feedPost else it } } }
                originalMyPost?.let { myPost -> myPostsState.update { posts -> posts.map { if (it.id == postId) myPost else it } } }
                return false
            }
            return true
        }

        override suspend fun addComment(
            postId: Long,
            content: String,
        ): Boolean {
            if (!isOnline()) return false

            val result = remoteDataSource.createComment(postId, CommunityCommentRequestDto(content))
            if (result !is NetworkResult.Success) return false
            val createdComment = result.value.toCommunityComment(postId)
            commentsState.update { current -> listOf(createdComment) + current.filterNot { it.id == createdComment.id } }
            if (createdComment.isMine) {
                val boardCohort = postsState.value.firstOrNull { it.id == postId }?.cohort
                val postTitle = postsState.value.firstOrNull { it.id == postId }?.title.orEmpty()
                val myComment = CommunityMyComment(createdComment, boardCohort, postTitle)
                myCommentsState.update { current -> listOf(myComment) + current.filterNot { it.comment.id == createdComment.id } }
            }
            // 서버가 돌려준 댓글을 즉시 캐시에 반영한다. 이후 상세 화면의 다음 refresh에서
            // 목록을 재검증하며, 이 POST 자체는 GET 실패 때문에 실패로 바뀌지 않는다.
            return true
        }

        override suspend fun updateComment(
            commentId: Long,
            content: String,
        ): Boolean {
            if (!isOnline()) return false

            val commentPostId = commentsState.value.firstOrNull { it.id == commentId }?.postId ?: return false
            val result = remoteDataSource.updateComment(commentId, CommunityCommentRequestDto(content))
            if (result !is NetworkResult.Success) return false
            val updatedComment = result.value.toCommunityComment(commentPostId)
            commentsState.update { current -> current.map { if (it.id == commentId) updatedComment else it } }
            myCommentsState.update { current ->
                current.map { myComment ->
                    if (myComment.comment.id == commentId) myComment.copy(comment = updatedComment) else myComment
                }
            }
            return refreshComments(commentPostId)
        }

        override suspend fun deleteComment(commentId: Long): Boolean {
            val result = remoteDataSource.deleteComment(commentId)
            if (result !is NetworkResult.Success) return false
            commentsState.update { comments -> comments.filterNot { it.id == commentId } }
            myCommentsState.update { comments -> comments.filterNot { it.comment.id == commentId } }
            return true
        }

        override fun isReported(target: CommunityReportTarget): Boolean = target in reportedTargets.value

        /** 신고를 접수하고 성공한 대상을 로컬에서 중복 신고하지 않게 표시한다. */
        override suspend fun report(
            target: CommunityReportTarget,
            reason: CommunityReportReason,
        ): Boolean {
            val targetType = if (target is CommunityReportTarget.Post) "POST" else "COMMENT"
            val targetId = when (target) { is CommunityReportTarget.Post -> target.postId; is CommunityReportTarget.Comment -> target.commentId }
            val reasonCode = when (reason) {
                CommunityReportReason.Abuse -> "ABUSE"
                CommunityReportReason.Spam -> "SPAM"
                CommunityReportReason.PersonalInfo -> "PERSONAL_INFO"
                CommunityReportReason.Inappropriate -> "INAPPROPRIATE"
                CommunityReportReason.Other -> "OTHER"
            }
            val result = remoteDataSource.report(CommunityReportRequestDto(targetType, targetId, reasonCode))
            if (result !is NetworkResult.Success) return false
            reportedTargets.update { it + target }
            return true
        }

        /** 검색어를 최근 검색어 맨 앞에 넣는다. 같은 검색어는 한 번만 남기고 최대 [MAX_RECENT_SEARCHES]개까지 둔다. */
        override fun addRecentSearch(query: String) {
            val keyword = query.trim()
            if (keyword.isEmpty()) return

            recentSearchesState.update { searches ->
                (listOf(keyword) + searches.filterNot { it == keyword }).take(MAX_RECENT_SEARCHES)
            }
        }

        override fun clearRecentSearches() {
            recentSearchesState.value = emptyList()
        }

        private suspend fun isOnline(): Boolean = networkMonitor.isOnline.first()

        private companion object {
            const val PAGE_SIZE = 20
            const val MY_COHORT = 10
            const val MAX_RECENT_SEARCHES = 10
            const val MAX_PAGES = 100
        }
    }

/** 저장소를 처음 채울 값이다. */
data class CommunitySeed(
    val posts: List<CommunityPost> = emptyList(),
    val comments: List<CommunityComment> = emptyList(),
    val recentSearches: List<String> = emptyList(),
)
