package com.example.hopes.feature.community.data

import com.example.hopes.core.connectivity.NetworkMonitor
import com.example.hopes.feature.community.model.CommunityReportReason
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CommunityStoreTest {
    private val network = FakeNetworkMonitor()
    private val store = CommunityStore(network)

    @Test
    fun `새 글은 내 글로 목록 맨 위에 들어간다`() =
        runBlocking {
            val id = store.createPost(cohort = 10, title = "새 글", content = "내용")

            val first = store.posts.first().first()
            assertEquals(id, first.id)
            assertTrue(first.isMine)
            assertEquals(store.myCohort, first.authorCohort)
        }

    @Test
    fun `인터넷이 끊기면 글과 댓글 등록이 실패한다`() =
        runBlocking {
            network.online.value = false
            val postCount = store.posts.first().size

            assertNull(store.createPost(cohort = 10, title = "새 글", content = "내용"))
            assertFalse(store.addComment(postId = 1, content = "댓글"))
            assertEquals(postCount, store.posts.first().size)
        }

    @Test
    fun `좋아요를 누르면 수가 늘고 다시 누르면 돌아온다`() =
        runBlocking {
            val postId = store.createPost(cohort = null, title = "글", content = "내용")!!

            store.toggleLike(postId)
            assertEquals(1, store.post(postId).first()!!.likeCount)

            store.toggleLike(postId)
            assertEquals(0, store.post(postId).first()!!.likeCount)
        }

    @Test
    fun `글을 지우면 그 글의 댓글도 함께 지운다`() =
        runBlocking {
            val postId = store.createPost(cohort = null, title = "글", content = "내용")!!
            store.addComment(postId, "댓글")
            assertEquals(1, store.post(postId).first()!!.commentCount)

            store.deletePost(postId)

            assertNull(store.post(postId).first())
            assertTrue(store.comments(postId).first().isEmpty())
        }

    @Test
    fun `신고한 글은 신고됨으로 기억한다`() {
        val target = CommunityReportTarget.Post(postId = 1)
        assertFalse(store.isReported(target))

        store.report(target, CommunityReportReason.Spam)

        assertTrue(store.isReported(target))
    }

    @Test
    fun `최근 검색어는 맨 앞에 넣고 같은 검색어는 한 번만 남긴다`() {
        store.clearRecentSearches()

        store.addRecentSearch("기숙사")
        store.addRecentSearch("면접")
        store.addRecentSearch(" 기숙사 ")

        assertEquals(listOf("기숙사", "면접"), store.recentSearches.value)
    }

    private class FakeNetworkMonitor : NetworkMonitor {
        val online = MutableStateFlow(true)
        override val isOnline: Flow<Boolean> = online
    }
}
