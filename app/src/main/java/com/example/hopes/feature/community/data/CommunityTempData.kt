package com.example.hopes.feature.community.data

import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityPost
import java.time.LocalDateTime

/**
 * 임시 글·댓글이다. 커뮤니티 API가 붙기 전에 하트·상세 화면을 눈으로 확인하려고 넣었다(피그마 13·14 예시와 같다).
 *
 * 확인이 끝나면 이 파일을 지우고 [CommunityStore]의 `CommunitySeed(...)` 인자를 빈 값으로 되돌리면 된다.
 * 홈 인기글은 저장소에서 읽으므로 따로 지울 것이 없다.
 */
object CommunityTempData {
    private val now: LocalDateTime = LocalDateTime.now()

    val posts: List<CommunityPost> =
        listOf(
            CommunityPost(
                id = 1,
                cohort = 10,
                authorCohort = 10,
                title = "기숙사 와이파이 다들 잘 돼?",
                content = "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서 과제 제출이 힘들어. 다른 층은 어떤지, 해결 방법 아는 사람 있으면 알려줘!",
                createdAt = now.minusMinutes(10),
                likeCount = 12,
            ),
            CommunityPost(
                id = 2,
                cohort = null,
                authorCohort = 10,
                title = "전공 동아리 면접 후기 공유",
                content = "어제 보고 왔는데 생각보다 분위기 편했어. 질문은 주로 지원 동기 위주였음.",
                createdAt = now.minusHours(1),
                likeCount = 34,
            ),
            CommunityPost(
                id = 3,
                cohort = 9,
                authorCohort = 9,
                title = "취업 준비 언제부터 시작했어?",
                content = "2학년 여름방학부터 포트폴리오 만들기 시작했는데 늦은 건지 모르겠어.",
                createdAt = now.minusHours(3),
                likeCount = 21,
            ),
        )

    val comments: List<CommunityComment> =
        listOf(
            CommunityComment(
                id = 101,
                postId = 1,
                authorCohort = 10,
                content = "2층도 저녁엔 느려. 공유기 위치 문제 같아.",
                createdAt = now.minusMinutes(5),
            ),
            CommunityComment(
                id = 102,
                postId = 1,
                authorCohort = 9,
                content = "사감 선생님께 말씀드리면 점검해 주신대!",
                createdAt = now.minusMinutes(3),
            ),
        )
}
