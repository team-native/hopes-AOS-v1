package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination
import com.teamnative.hopes.R

/**
 * 마이페이지 서버 상태를 화면에 연결하고 하위 화면 이동을 처리한다.
 * 커뮤니티 서버 기능이 없어 활동 통계는 피그마 시안의 예시 값으로 채운다.
 *
 * @param isProfileSavedToastVisible 프로필 수정 화면에서 저장하고 돌아왔을 때 true다. 프로필을 다시 읽고 토스트를 띄운다.
 */
@Composable
fun MyPageRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToAppSettings: () -> Unit,
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToContact: () -> Unit,
    isProfileSavedToastVisible: Boolean,
    onProfileSavedToastDismiss: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(isProfileSavedToastVisible) {
        if (isProfileSavedToastVisible) {
            viewModel.refreshProfile()
        }
    }

    MyPageScreen(
        uiState = uiState.copy(activityStats = SAMPLE_ACTIVITY_STATS),
        toastMessage =
            if (isProfileSavedToastVisible) {
                stringResource(R.string.profile_edit_saved_toast)
            } else {
                null
            },
        onToastDismiss = onProfileSavedToastDismiss,
        onEvent = { event ->
            when (event) {
                MyPageScreenEvent.AppSettingsClicked -> onNavigateToAppSettings()

                MyPageScreenEvent.ProfileEditClicked -> onNavigateToProfileEdit()

                // 내 활동 화면이 생기기 전까지는 커뮤니티 탭으로 보낸다.
                MyPageScreenEvent.MyActivityClicked -> onNavigate(HopesDestination.Community)

                MyPageScreenEvent.PersonalSettingsClicked -> onNavigateToPersonalSettings()

                MyPageScreenEvent.NotificationClicked -> onNavigateToNotifications()

                MyPageScreenEvent.ContactClicked -> onNavigateToContact()
            }
        },
        onNavigate = onNavigate,
    )
}

private val SAMPLE_ACTIVITY_STATS = MyPageActivityStats(postCount = 2, commentCount = 5, chatCount = 12)
