package com.example.hopes.feature.community.data

import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityMyComment
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.model.CommunityReportReason
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/** 화면에서 사용하는 커뮤니티 저장소 계약이다. */
interface CommunityRepository {
    val myCohort: Int
    val availableCohorts: StateFlow<List<Int>>
    val posts: Flow<List<CommunityPost>>
    val recentSearches: StateFlow<List<String>>
    val myPosts: Flow<List<CommunityPost>>
    val myComments: Flow<List<CommunityMyComment>>
    suspend fun loadBoards(): Boolean
    suspend fun refreshPosts(cohort: Int? = null, keyword: String? = null, page: Int = 0): Boolean
    suspend fun loadNextPostsPage(cohort: Int?): Boolean
    suspend fun refreshMyPosts(): Boolean
    suspend fun refreshMyComments(): Boolean
    fun post(postId: Long): Flow<CommunityPost?>
    fun comments(postId: Long): Flow<List<CommunityComment>>
    suspend fun refreshPost(postId: Long): Boolean
    suspend fun refreshComments(postId: Long): Boolean
    suspend fun createPost(cohort: Int?, title: String, content: String): Long?
    suspend fun updatePost(postId: Long, cohort: Int?, title: String, content: String): Boolean
    suspend fun deletePost(postId: Long): Boolean
    suspend fun toggleLike(postId: Long): Boolean
    suspend fun addComment(postId: Long, content: String): Boolean
    suspend fun updateComment(commentId: Long, content: String): Boolean
    suspend fun deleteComment(commentId: Long): Boolean
    fun isReported(target: CommunityReportTarget): Boolean
    suspend fun report(target: CommunityReportTarget, reason: CommunityReportReason): Boolean
    fun addRecentSearch(query: String)
    fun clearRecentSearches()
}
