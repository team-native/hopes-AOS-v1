package com.example.hopes.feature.community.presentation.search

import com.example.hopes.feature.community.model.CommunityPost
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class CommunitySearchTest {
    private val posts =
        listOf(
            post(id = 1, cohort = 10, title = "기숙사 와이파이 다들 잘 돼?", content = "3층 끝방"),
            post(id = 2, cohort = 10, title = "세탁실 이용 시간", content = "기숙사 세탁실은 몇 시까지?"),
            post(id = 3, cohort = 9, title = "기숙사 점호 시간", content = "몇 시야?"),
            post(id = 4, cohort = null, title = "동아리 면접 후기", content = "분위기 편했어"),
        )

    @Test
    fun `제목이나 내용에 검색어가 있으면 찾는다`() {
        assertEquals(listOf(1L, 2L, 3L), searchPosts(posts, "기숙사", cohort = null).map { it.id })
    }

    @Test
    fun `게시판을 고르면 그 기수 글에서만 찾는다`() {
        assertEquals(listOf(1L, 2L), searchPosts(posts, "기숙사", cohort = 10).map { it.id })
    }

    @Test
    fun `검색어가 비어 있으면 결과가 없다`() {
        assertEquals(emptyList<CommunityPost>(), searchPosts(posts, "  ", cohort = null))
    }

    @Test
    fun `검색 API 실패는 기존 캐시가 있어도 오류 상태다`() {
        assertEquals(CommunitySearchStatus.LoadError, searchStatusFor(succeeded = false, results = posts))
    }

    @Test
    fun `성공한 검색은 결과 유무에 따라 빈 결과와 목록 상태를 구분한다`() {
        assertEquals(CommunitySearchStatus.Empty, searchStatusFor(succeeded = true, results = emptyList()))
        assertEquals(CommunitySearchStatus.Results, searchStatusFor(succeeded = true, results = posts))
    }

    private fun post(
        id: Long,
        cohort: Int?,
        title: String,
        content: String,
    ) = CommunityPost(
        id = id,
        cohort = cohort,
        authorCohort = 10,
        title = title,
        content = content,
        createdAt = LocalDateTime.of(2026, 11, 15, 12, 0),
    )
}
