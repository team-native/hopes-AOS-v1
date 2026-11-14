package com.example.hopes.feature.community.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.hopes.navigation.HopesDestination

/**
 * 커뮤니티 탭이다. 게시판 서버 기능이 아직 없어 빈 화면만 보여주고,
 * 기수 필터 선택만 화면 안에서 유지한다. 검색·내 활동·글쓰기는 연동 전까지 동작하지 않는다.
 */
@Composable
fun CommunityRoute(onNavigate: (HopesDestination) -> Unit) {
    var selectedCohort by rememberSaveable { mutableStateOf<Int?>(null) }

    CommunityScreen(
        contentState = CommunityContentState.Empty,
        cohorts = COMMUNITY_COHORTS,
        selectedCohort = selectedCohort,
        onCohortSelect = { cohort -> selectedCohort = cohort },
        onSearchClick = {},
        onMyActivityClick = {},
        onWriteClick = {},
        onRetryClick = {},
        onNavigate = onNavigate,
    )
}

// 피그마 시안의 기수 필터 목록이다. 서버에서 기수 목록을 받게 되면 대체한다.
private val COMMUNITY_COHORTS = listOf(8, 9, 10, 11)
