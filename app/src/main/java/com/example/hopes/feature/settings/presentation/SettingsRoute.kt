package com.example.hopes.feature.settings.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.R
import com.example.hopes.feature.settings.presentation.component.SettingsAccountDeletionBottomSheet
import com.example.hopes.feature.settings.presentation.component.SettingsAccountDeletionConfirmDialog
import com.example.hopes.navigation.HopesDestination

/** 개인정보처리방침 전문을 호스팅하는 외부 URL. */
private const val PRIVACY_POLICY_URL = "https://spotless-beret-a3e.notion.site/Hopes-3c497507c37c8052ba2cd6fb48f568a3"

private const val PRIVACY_POLICY_LOG_TAG = "SettingsRoute"

/** 설정 화면에 앱 탐색 콜백을 제공한다. */
@Composable
fun SettingsRoute(
    onNavigate: (HopesDestination) -> Unit,
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isDeletionSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isDeletionConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    // 비밀번호 원문은 SavedState에 저장하지 않고 화면 생명주기 동안만 메모리에 유지한다.
    var deletionPassword by remember { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.LoggedOut,
                SettingsEffect.AccountDeleted,
                -> {
                    isDeletionSheetVisible = false
                    isDeletionConfirmDialogVisible = false
                    deletionPassword = ""
                    onLogout()
                }
            }
        }
    }

    LaunchedEffect(uiState.accountDeletionError) {
        if (uiState.accountDeletionError != null) {
            isDeletionConfirmDialogVisible = false
            isDeletionSheetVisible = true
        }
    }

    val accountDeletionErrorMessage = uiState.accountDeletionError?.let { error ->
        when (error) {
            is AccountDeletionError.Http -> error.serverMessage ?: stringResource(
                if (error.statusCode == UNAUTHORIZED_STATUS_CODE) {
                    R.string.account_deletion_password_error
                } else {
                    R.string.account_deletion_request_error
                },
            )

            AccountDeletionError.Network -> stringResource(R.string.account_deletion_network_error)
            AccountDeletionError.Serialization -> stringResource(R.string.account_deletion_request_error)
        }
    }

    SettingsScreen(
        onNavigate = onNavigate,
        isDarkModeEnabled = isDarkModeEnabled,
        onDarkModeChange = onDarkModeChange,
        onBackClick = onBackClick,
        onNavigateToPersonalSettings = onNavigateToPersonalSettings,
        onNavigateToContact = onNavigateToContact,
        onNavigateToPrivacyPolicy = {
            // openUri는 URI 형식 오류나 처리 가능한 앱이 없을 때 예외를 던질 수 있어
            // 앱이 크래시하지 않도록 실패를 조용히 로그로만 남긴다.
            try {
                uriHandler.openUri(PRIVACY_POLICY_URL)
            } catch (exception: Exception) {
                Log.w(PRIVACY_POLICY_LOG_TAG, "개인정보처리방침 URL을 열지 못했습니다.", exception)
            }
        },
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                SettingsScreenEvent.LogoutClicked -> viewModel.logout()
                SettingsScreenEvent.DeleteAccountClicked -> {
                    viewModel.onEvent(event)
                    isDeletionSheetVisible = true
                }
            }
        },
    )

    if (isDeletionSheetVisible) {
        SettingsAccountDeletionBottomSheet(
            password = deletionPassword,
            onPasswordChange = {
                deletionPassword = it
                if (uiState.accountDeletionError != null) {
                    viewModel.clearAccountDeletionError()
                }
            },
            errorMessage = accountDeletionErrorMessage,
            isDeletingAccount = uiState.isDeletingAccount,
            onDeleteClick = {
                isDeletionSheetVisible = false
                isDeletionConfirmDialogVisible = true
            },
            onDismiss = {
                isDeletionSheetVisible = false
                deletionPassword = ""
                viewModel.clearAccountDeletionError()
            },
        )
    }

    if (isDeletionConfirmDialogVisible) {
        SettingsAccountDeletionConfirmDialog(
            onConfirm = {
                isDeletionConfirmDialogVisible = false
                viewModel.deleteAccount(deletionPassword)
            },
            onDismiss = {
                isDeletionConfirmDialogVisible = false
                isDeletionSheetVisible = true
            },
        )
    }
}

private const val UNAUTHORIZED_STATUS_CODE = 401
