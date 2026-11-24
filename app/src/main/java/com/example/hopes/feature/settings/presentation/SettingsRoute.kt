package com.example.hopes.feature.settings.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.teamnative.hopes.BuildConfig
import com.teamnative.hopes.R

/** Swagger가 공개 text/html로 제공하는 개인정보처리방침 경로다. */
private const val PRIVACY_POLICY_PATH = "api/privacy/policy"

private const val PRIVACY_POLICY_LOG_TAG = "SettingsRoute"

/** 설정 화면에 서버 상태와 화면 이동, 로그아웃·회원탈퇴 흐름을 연결한다. */
@Composable
fun SettingsRoute(
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToAppInfo: () -> Unit,
    onNavigateToAccountDeletion: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLogoutDialogVisible by rememberSaveable { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.LoggedOut,
                SettingsEffect.AccountDeleted,
                -> {
                    isLogoutDialogVisible = false
                    onLogout()
                }
            }
        }
    }

    SettingsScreen(
        isDarkModeEnabled = isDarkModeEnabled,
        isDeleteAccountEnabled = true,
        appVersion = BuildConfig.VERSION_NAME,
        onBackClick = onBackClick,
        onEvent = { event ->
            when (event) {
                is SettingsScreenEvent.DarkModeChanged -> {
                    onDarkModeChange(event.isEnabled)
                }

                SettingsScreenEvent.AppInfoClicked -> {
                    onNavigateToAppInfo()
                }

                SettingsScreenEvent.PrivacyPolicyClicked -> {
                    // openUri는 URI 형식 오류나 처리 가능한 앱이 없을 때 예외를 던질 수 있어
                    // 앱이 크래시하지 않도록 실패를 조용히 로그로만 남긴다.
                    try {
                        val policyUrl = "${BuildConfig.BASE_URL.trimEnd('/')}/$PRIVACY_POLICY_PATH"
                        uriHandler.openUri(policyUrl)
                    } catch (exception: Exception) {
                        Log.w(PRIVACY_POLICY_LOG_TAG, "개인정보처리방침 URL을 열지 못했습니다.", exception)
                    }
                }

                SettingsScreenEvent.LogoutClicked -> {
                    isLogoutDialogVisible = true
                }

                SettingsScreenEvent.DeleteAccountClicked -> {
                    onNavigateToAccountDeletion()
                }
            }
        },
    )

    if (isLogoutDialogVisible) {
        HopesAlertDialog(
            title = stringResource(R.string.logout_confirm_title),
            message = stringResource(R.string.logout_confirm_message),
            dismissText = stringResource(R.string.cancel),
            confirmText = stringResource(R.string.logout),
            onDismiss = { isLogoutDialogVisible = false },
            onConfirm = {
                isLogoutDialogVisible = false
                viewModel.logout()
            },
            isConfirmDestructive = true,
        )
    }
}
