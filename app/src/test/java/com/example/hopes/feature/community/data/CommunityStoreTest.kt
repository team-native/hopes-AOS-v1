package com.example.hopes.feature.community.data

import com.example.hopes.core.connectivity.NetworkMonitor
import com.example.hopes.core.network.ApiExecutor
import com.example.hopes.data.api.CommunityApiService
import com.example.hopes.data.api.CommunityBoardDto
import com.example.hopes.data.api.CommunityPostPageDto
import com.example.hopes.data.api.CommunityPostPreviewDto
import com.example.hopes.data.api.CommunityPostDto
import com.example.hopes.data.api.CommunityMyCommentPageDto
import com.example.hopes.data.api.CommunityMyCommentDto
import com.example.hopes.data.api.CommunityCommentDto
import com.example.hopes.data.api.CommunityCommentPageDto
import com.example.hopes.data.api.CommunityCommentRequestDto
import retrofit2.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.reflect.Proxy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

class CommunityStoreTest {
    private val network = FakeNetworkMonitor()
    private val api = Proxy.newProxyInstance(CommunityApiService::class.java.classLoader, arrayOf(CommunityApiService::class.java)) { _, _, _ -> null } as CommunityApiService
    private val store = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), api))

    @Test
    fun `인터넷이 끊기면 게시글과 댓글 등록을 API로 보내지 않는다`() = runBlocking {
        network.online.value = false
        assertNull(store.createPost(cohort = 10, title = "새 글", content = "내용"))
        assertFalse(store.addComment(postId = 1, content = "댓글"))
    }

    @Test
    fun `최근 검색어는 중복 없이 최신 항목을 앞에 둔다`() {
        store.addRecentSearch("기숙사")
        store.addRecentSearch("면접")
        store.addRecentSearch(" 기숙사 ")
        assertEquals(listOf("기숙사", "면접"), store.recentSearches.value)
    }

    @Test
    fun `내 게시글 새로고침이 전체 피드 캐시를 덮어쓰지 않는다`() = runBlocking {
        val apiService = apiService { method, args ->
            when (method.name) {
                "getBoards" -> Response.success(mapOf("boards" to listOf(CommunityBoardDto(id = 3, name = "10기", cohort = 10, sortOrder = 0))))
                "getPosts" -> Response.success(CommunityPostPageDto(listOf(preview(id = 1, boardId = 3, mine = false)), 0, 20, false))
                "getMyPosts" -> Response.success(CommunityPostPageDto(listOf(preview(id = 2, boardId = 3, mine = true)), args[0] as Int, 20, false))
                else -> error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))

        assertTrue(repository.loadBoards())
        assertTrue(repository.refreshPosts())
        assertTrue(repository.refreshMyPosts())

        assertEquals(listOf(1L), repository.posts.first().map { it.id })
        assertEquals(listOf(2L), repository.myPosts.first().map { it.id })
    }

    @Test
    fun `내 댓글 페이지 응답이 같은 cursor를 반복하면 중단한다`() = runBlocking {
        var calls = 0
        val apiService = apiService { method, _ ->
            if (method.name == "getMyComments") {
                calls++
                Response.success(CommunityMyCommentPageDto(emptyList(), page = 0, size = 20, hasNext = true))
            } else {
                error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))

        assertFalse(repository.refreshMyComments())
        assertEquals(2, calls)
    }

    @Test
    fun `내 댓글 페이지 로드에 100페이지 상한을 적용한다`() = runBlocking {
        var calls = 0
        val apiService = apiService { method, args ->
            if (method.name == "getMyComments") {
                val page = args[0] as Int
                calls++
                Response.success(CommunityMyCommentPageDto(emptyList(), page = page, size = 20, hasNext = true))
            } else {
                error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))

        assertFalse(repository.refreshMyComments())
        assertEquals(100, calls)
    }

    @Test
    fun `내 게시글 좋아요 실패 시 내 활동 캐시도 원래 값으로 복구한다`() = runBlocking {
        val apiService = apiService { method, args ->
            when (method.name) {
                "getBoards" -> Response.success(mapOf("boards" to listOf(CommunityBoardDto(id = 3, name = "10기", cohort = 10, sortOrder = 0))))
                "getMyPosts" -> Response.success(CommunityPostPageDto(listOf(preview(id = 2, boardId = 3, mine = true)), args[0] as Int, 20, false))
                "like" -> Response.error<Unit>(500, "error".toResponseBody("text/plain".toMediaType()))
                else -> error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))
        assertTrue(repository.refreshMyPosts())

        assertFalse(repository.toggleLike(2))
        val post = repository.myPosts.first().single()
        assertFalse(post.isLiked)
        assertEquals(1L, post.likeCount)
    }

    @Test
    fun `댓글 create update delete가 내 댓글 캐시를 함께 갱신한다`() = runBlocking {
        val initial = myComment(id = 6, content = "기존 댓글")
        var remoteComments = listOf(comment(id = 6, content = "기존 댓글"))
        val apiService = apiService { method, args ->
            when (method.name) {
                "getMyComments" -> Response.success(CommunityMyCommentPageDto(listOf(initial), page = args[0] as Int, size = 20, hasNext = false))
                "getComments" -> Response.success(CommunityCommentPageDto(remoteComments, page = args[1] as Int, size = 20, hasNext = false))
                "createComment" -> {
                    remoteComments = listOf(comment(id = 7, content = (args[1] as CommunityCommentRequestDto).content)) + remoteComments
                    Response.success(comment(id = 7, content = (args[1] as CommunityCommentRequestDto).content))
                }
                "updateComment" -> {
                    val updated = comment(id = 7, content = (args[1] as CommunityCommentRequestDto).content)
                    remoteComments = remoteComments.map { if (it.id == 7L) updated else it }
                    Response.success(updated)
                }
                "deleteComment" -> {
                    remoteComments = remoteComments.filterNot { it.id == args[0] as Long }
                    Response.success(Unit)
                }
                else -> error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))
        assertTrue(repository.refreshMyComments())

        assertTrue(repository.addComment(postId = 1, content = "새 댓글"))
        assertEquals("새 댓글", repository.myComments.first().first { it.comment.id == 7L }.comment.content)
        assertTrue(repository.updateComment(commentId = 7, content = "수정 댓글"))
        assertEquals("수정 댓글", repository.myComments.first().first { it.comment.id == 7L }.comment.content)
        assertTrue(repository.deleteComment(commentId = 7))
        assertFalse(repository.myComments.first().any { it.comment.id == 7L })
    }

    @Test
    fun `댓글 POST 성공 응답은 댓글 목록 GET 실패와 무관하게 캐시에 남는다`() = runBlocking {
        var commentListRequested = false
        val apiService = apiService { method, args ->
            when (method.name) {
                "createComment" -> Response.success(comment(9, (args[1] as CommunityCommentRequestDto).content))
                "getComments" -> {
                    commentListRequested = true
                    Response.error<CommunityCommentPageDto>(500, "error".toResponseBody("text/plain".toMediaType()))
                }
                else -> error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))

        assertTrue(repository.addComment(postId = 1, content = "새 댓글"))
        assertEquals(listOf("새 댓글"), repository.comments(1).first().map { it.content })
        assertFalse(commentListRequested)
    }

    @Test
    fun `게시글 생성 응답을 내 활동 캐시에 즉시 반영한다`() = runBlocking {
        val apiService = apiService { method, args ->
            when (method.name) {
                "getBoards" -> Response.success(mapOf("boards" to listOf(CommunityBoardDto(id = 3, name = "10기", cohort = 10, sortOrder = 0))))
                "createPost" -> Response.success(post(id = 44, boardId = 3, mine = true, title = (args[0] as com.example.hopes.data.api.CommunityPostRequestDto).title))
                "getPosts" -> Response.error<CommunityPostPageDto>(500, "error".toResponseBody("text/plain".toMediaType()))
                else -> error("unexpected ${method.name}")
            }
        }
        val repository = CommunityStore(network, CommunityRemoteDataSource(ApiExecutor(Json), apiService))

        assertTrue(repository.loadBoards())
        assertEquals(44L, repository.createPost(10, "새 글", "본문"))
        assertEquals(listOf(44L), repository.myPosts.first().map { it.id })
    }

    private class FakeNetworkMonitor : NetworkMonitor {
        val online = MutableStateFlow(true)
        override val isOnline: Flow<Boolean> = online
    }

    private fun apiService(handler: (java.lang.reflect.Method, Array<out Any?>) -> Any?): CommunityApiService =
        Proxy.newProxyInstance(CommunityApiService::class.java.classLoader, arrayOf(CommunityApiService::class.java)) { _, method, args ->
            handler(method, args ?: emptyArray())
        } as CommunityApiService

    private fun preview(id: Long, boardId: Long, mine: Boolean) =
        CommunityPostPreviewDto(id, boardId, "제목$id", "내용", "2026-01-01T00:00:00Z", "2026-01-01T00:00:00Z", 1, 2, false, mine, "10기", 0, 0)

    private fun post(id: Long, boardId: Long, mine: Boolean, title: String) =
        CommunityPostDto(id, boardId, title, "본문", "2026-01-01T00:00:00Z", "2026-01-01T00:00:00Z", 0, 0, false, mine, "10기", 0, 0)

    private fun myComment(id: Long, content: String) =
        CommunityMyCommentDto(id, 1, "글", content, "2026-01-01T00:00:00Z", true)

    private fun comment(id: Long, content: String) =
        CommunityCommentDto(id, content, "2026-01-01T00:00:00Z", "2026-01-01T00:00:00Z", true, "10기")
}
