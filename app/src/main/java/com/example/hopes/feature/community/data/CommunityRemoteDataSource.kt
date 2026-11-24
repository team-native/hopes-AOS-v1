package com.example.hopes.feature.community.data

import com.example.hopes.core.network.ApiExecutor
import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.CommunityApiService
import com.example.hopes.data.api.CommunityBoardDto
import com.example.hopes.data.api.CommunityCommentDto
import com.example.hopes.data.api.CommunityCommentPageDto
import com.example.hopes.data.api.CommunityCommentRequestDto
import com.example.hopes.data.api.CommunityMyCommentPageDto
import com.example.hopes.data.api.CommunityPostDto
import com.example.hopes.data.api.CommunityPostPageDto
import com.example.hopes.data.api.CommunityPostRequestDto
import com.example.hopes.data.api.CommunityPostUpdateDto
import com.example.hopes.data.api.CommunityReportRequestDto
import javax.inject.Inject

/** 커뮤니티 HTTP 호출을 ApiExecutor로 표준화한다. */
class CommunityRemoteDataSource
    @Inject
    constructor(
        private val apiExecutor: ApiExecutor,
        private val api: CommunityApiService,
    ) {
        suspend fun boards(): NetworkResult<Map<String, List<CommunityBoardDto>>> = apiExecutor.execute { api.getBoards() }

        suspend fun posts(boardId: Long?, keyword: String?, page: Int, size: Int): NetworkResult<CommunityPostPageDto> =
            apiExecutor.execute { api.getPosts(boardId, keyword, page, size, "LATEST") }

        suspend fun post(postId: Long): NetworkResult<CommunityPostDto> = apiExecutor.execute { api.getPost(postId) }

        suspend fun createPost(body: CommunityPostRequestDto): NetworkResult<CommunityPostDto> = apiExecutor.execute { api.createPost(body) }

        suspend fun updatePost(postId: Long, body: CommunityPostUpdateDto): NetworkResult<CommunityPostDto> = apiExecutor.execute { api.updatePost(postId, body) }

        suspend fun deletePost(postId: Long): NetworkResult<Unit> = apiExecutor.executeNoContent { api.deletePost(postId) }

        suspend fun comments(postId: Long, page: Int, size: Int): NetworkResult<CommunityCommentPageDto> = apiExecutor.execute { api.getComments(postId, page, size) }

        suspend fun createComment(postId: Long, body: CommunityCommentRequestDto): NetworkResult<CommunityCommentDto> = apiExecutor.execute { api.createComment(postId, body) }

        suspend fun updateComment(commentId: Long, body: CommunityCommentRequestDto): NetworkResult<CommunityCommentDto> = apiExecutor.execute { api.updateComment(commentId, body) }

        suspend fun deleteComment(commentId: Long): NetworkResult<Unit> = apiExecutor.executeNoContent { api.deleteComment(commentId) }

        suspend fun setLike(postId: Long, liked: Boolean): NetworkResult<Unit> =
            if (liked) apiExecutor.executeNoContent { api.like(postId) } else apiExecutor.executeNoContent { api.unlike(postId) }

        suspend fun report(body: CommunityReportRequestDto): NetworkResult<Unit> = apiExecutor.executeNoContent { api.report(body) }

        suspend fun myPosts(page: Int, size: Int): NetworkResult<CommunityPostPageDto> = apiExecutor.execute { api.getMyPosts(page, size) }

        suspend fun myComments(page: Int, size: Int): NetworkResult<CommunityMyCommentPageDto> = apiExecutor.execute { api.getMyComments(page, size) }
    }
