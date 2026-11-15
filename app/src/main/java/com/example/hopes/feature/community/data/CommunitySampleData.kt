package com.example.hopes.feature.community.data

import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityPost
import java.time.LocalDateTime

// TODO(#230): 화면 확인용 임시 글·댓글이다. PR 올리기 전에 이 파일을 지우고,
//  CommunityStore의 `communitySampleSeed(...)`를 `CommunitySeed()`로 바꾼다.

/** 피그마 13 ~ 18 시안의 글·댓글을 [now] 기준 시간으로 만든다. */
fun communitySampleSeed(now: LocalDateTime): CommunitySeed =
    CommunitySeed(
        posts =
            listOf(
                CommunityPost(
                    id = 1,
                    cohort = 10,
                    authorCohort = 10,
                    title = "기숙사 와이파이 다들 잘 돼?",
                    content =
                        "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서 과제 제출이 힘들어. " +
                            "다른 층은 어떤지, 해결 방법 아는 사람 있으면 알려줘!",
                    createdAt = now.minusMinutes(10),
                    likeCount = 12,
                    isMine = true,
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
                CommunityPost(
                    id = 4,
                    cohort = 10,
                    authorCohort = 10,
                    title = "기숙사 세탁실 이용 시간 아는 사람?",
                    content = "주말에도 밤 10시까지 쓸 수 있는지 궁금해.",
                    createdAt = now.minusDays(1),
                    likeCount = 4,
                    isMine = true,
                ),
                CommunityPost(
                    id = 5,
                    cohort = 8,
                    authorCohort = 8,
                    title = "졸업 전에 꼭 해 볼 것 추천해 줘",
                    content = "곧 졸업인데 학교 다닐 때 해 두면 좋은 거 있으면 알려 줘. 대회, 자격증, 뭐든 좋아.",
                    createdAt = now.minusDays(2),
                    likeCount = 9,
                ),
                CommunityPost(
                    id = 6,
                    cohort = 11,
                    authorCohort = 11,
                    title = "입학 전에 준비하면 좋은 거 있어요?",
                    content = "합격하고 입학까지 시간이 좀 남았는데 미리 공부해 두면 좋은 언어가 있을까요?",
                    createdAt = now.minusDays(3),
                    likeCount = 15,
                ),
            ),
        comments =
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
                CommunityComment(
                    id = 103,
                    postId = 1,
                    authorCohort = 10,
                    content = "오 고마워, 내일 말씀드려 볼게.",
                    createdAt = now,
                    isMine = true,
                ),
                CommunityComment(
                    id = 104,
                    postId = 2,
                    authorCohort = 9,
                    content = "나도 비슷했어. 포트폴리오 얘기는 거의 안 나왔어.",
                    createdAt = now.minusMinutes(50),
                ),
                CommunityComment(
                    id = 105,
                    postId = 2,
                    authorCohort = 10,
                    content = "혹시 포트폴리오도 봤어?",
                    createdAt = now.minusMinutes(40),
                    isMine = true,
                ),
                CommunityComment(
                    id = 106,
                    postId = 3,
                    authorCohort = 10,
                    content = "저는 2학년 겨울부터 했어요!",
                    createdAt = now.minusHours(2),
                    isMine = true,
                ),
                CommunityComment(
                    id = 107,
                    postId = 3,
                    authorCohort = 8,
                    content = "늦지 않아. 3학년 1학기까지 하나만 제대로 만들어도 충분해.",
                    createdAt = now.minusHours(1),
                ),
                CommunityComment(
                    id = 108,
                    postId = 4,
                    authorCohort = 10,
                    content = "주말에도 밤 10시까지 돼.",
                    createdAt = now.minusHours(20),
                ),
                CommunityComment(
                    id = 109,
                    postId = 4,
                    authorCohort = 11,
                    content = "일요일은 9시에 닫는다고 들었어요.",
                    createdAt = now.minusHours(18),
                ),
                CommunityComment(
                    id = 110,
                    postId = 6,
                    authorCohort = 10,
                    content = "파이썬이나 C 기초만 해 와도 1학년 때 훨씬 편해!",
                    createdAt = now.minusDays(2),
                    isMine = true,
                ),
            ),
        recentSearches = listOf("기숙사", "동아리 면접", "포트폴리오"),
    )
