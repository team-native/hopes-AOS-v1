package com.example.hopes.feature.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamnative.hopes.R

/**
 * 회원탈퇴 화면에 탈퇴 요청과 오류 문구를 연결한다. 탈퇴 API 호출은 [SettingsViewModel.deleteAccount]를 그대로 쓴다.
 *
 * @param onAccountDeleted 탈퇴에 성공했을 때 부른다. 세션이 끊기면 앱이 시작 화면으로 돌아가므로, 거기서 보여줄 완료 안내를 켜는 데 쓴다.
 */
@Composable
fun AccountDeletionRoute(
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isConfirmDialogVisible by rememberSaveable { mutableStateOf(false) }
    // 비밀번호 원문은 SavedState에 저장하지 않고 화면 생명주기 동안만 메모리에 유지한다.
    var password by remember { mutableStateOf("") }

    LaunchedEffect(viewModel) {
        viewModel.clearAccountDeletionError()
        viewModel.effect.collect { effect ->
            if (effect == SettingsEffect.AccountDeleted) {
                isConfirmDialogVisible = false
                password = ""
                onAccountDeleted()
            }
        }
    }

    // 실패하면 확인 다이얼로그를 닫고 오류를 입력란 아래에 보여준다.
    LaunchedEffect(uiState.accountDeletionError) {
        if (uiState.accountDeletionError != null) isConfirmDialogVisible = false
    }

    val errorMessage =
        uiState.accountDeletionError?.let { error ->
            when (error) {
                is AccountDeletionError.Http -> {
                    error.serverMessage ?: stringResource(
                        if (error.statusCode == UNAUTHORIZED_STATUS_CODE) {
                            R.string.account_deletion_password_error
                        } else {
                            R.string.account_deletion_request_error
                        },
                    )
                }

                AccountDeletionError.Network -> {
                    stringResource(R.string.account_deletion_network_error)
                }

                AccountDeletionError.Serialization -> {
                    stringResource(R.string.account_deletion_request_error)
                }
            }
        }

    AccountDeletionScreen(
        password = password,
        onPasswordChange = {
            password = it
            if (uiState.accountDeletionError != null) viewModel.clearAccountDeletionError()
        },
        errorMessage = errorMessage,
        isDeleting = uiState.isDeletingAccount,
        isConfirmDialogVisible = isConfirmDialogVisible,
        onSubmitClick = { isConfirmDialogVisible = true },
        onConfirmDismiss = { isConfirmDialogVisible = false },
        onConfirm = {
            isConfirmDialogVisible = false
            viewModel.deleteAccount(password)
        },
        onBackClick = onBackClick,
    )
}

private const val UNAUTHORIZED_STATUS_CODE = 401
