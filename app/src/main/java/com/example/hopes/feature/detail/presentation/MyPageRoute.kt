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
 * 화면에 들어올 때마다 대화 수를 다시 센다.
 *
 * @param isProfileSavedToastVisible 프로필 수정 화면에서 저장하고 돌아왔을 때 true다. 프로필을 다시 읽고 토스트를 띄운다.
 */
@Composable
fun MyPageRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToAppSettings: () -> Unit,
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
    onNavigateToMyActivity: () -> Unit,
    isProfileSavedToastVisible: Boolean,
    onProfileSavedToastDismiss: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refreshChatCount()
    }

    LaunchedEffect(isProfileSavedToastVisible) {
        if (isProfileSavedToastVisible) {
            viewModel.refreshProfile()
        }
    }

    MyPageScreen(
        uiState = uiState,
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
                MyPageScreenEvent.MyActivityClicked -> onNavigateToMyActivity()
                MyPageScreenEvent.PersonalSettingsClicked -> onNavigateToPersonalSettings()
                MyPageScreenEvent.ContactClicked -> onNavigateToContact()
            }
        },
        onNavigate = onNavigate,
    )
}
