package com.example.hopes.feature.auth.presentation.signup.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupAlreadyRegisteredDialog
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupOptionBottomSheet
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SIGNUP_GENERATION_OPTIONS
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupDepartment
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupEffect
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupScreenEvent
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupViewModel
import com.teamnative.hopes.R

/**
 * 회원가입 상태 수집, 일회성 효과, 뒤로가기, 선택 시트와 다이얼로그 표시를 맡는다.
 *
 * @param onSignupCompleted 가입이 끝나면 가입한 이메일과 서버 안내 문구를 넘겨 로그인 단계로 보낸다.
 */
@Composable
fun SignupRoute(
    onNavigateBack: () -> Unit,
    onSignupCompleted: (email: String, message: String) -> Unit,
    viewModel: SignupViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    var isAlreadyRegisteredDialogVisible by rememberSaveable { mutableStateOf(false) }
    var visibleSelectionSheet by rememberSaveable { mutableStateOf<SignupSelectionSheet?>(null) }

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    SignupEffect.ShowAlreadyRegisteredDialog -> {
                        isAlreadyRegisteredDialogVisible = true
                    }

                    SignupEffect.NavigateBack -> {
                        onNavigateBack()
                    }

                    is SignupEffect.Completed -> {
                        onSignupCompleted(effect.email, effect.message)
                    }
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(SignupScreenEvent.BackClicked)
    }

    SignupScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                SignupScreenEvent.DepartmentFieldClicked -> {
                    visibleSelectionSheet = SignupSelectionSheet.Department
                }

                SignupScreenEvent.GenerationFieldClicked -> {
                    visibleSelectionSheet = SignupSelectionSheet.Generation
                }

                else -> {
                    viewModel.onEvent(event)
                }
            }
        },
    )

    when (visibleSelectionSheet) {
        SignupSelectionSheet.Department -> {
            val departments = SignupDepartment.entries

            SignupOptionBottomSheet(
                title = stringResource(R.string.signup_department_sheet_title),
                options = departments.map { department -> stringResource(department.labelResId()) },
                initialSelectedIndex = departments.indexOf(uiState.department).takeIf { index -> index >= 0 },
                onConfirm = { index ->
                    viewModel.selectDepartment(departments[index])
                    visibleSelectionSheet = null
                },
                onDismiss = { visibleSelectionSheet = null },
            )
        }

        SignupSelectionSheet.Generation -> {
            SignupOptionBottomSheet(
                title = stringResource(R.string.signup_generation_dialog_title),
                options =
                    SIGNUP_GENERATION_OPTIONS.map { generation ->
                        stringResource(R.string.signup_generation_option, generation)
                    },
                initialSelectedIndex =
                    SIGNUP_GENERATION_OPTIONS.indexOf(uiState.generation).takeIf { index ->
                        index >=
                            0
                    },
                onConfirm = { index ->
                    viewModel.selectGeneration(SIGNUP_GENERATION_OPTIONS[index])
                    visibleSelectionSheet = null
                },
                onDismiss = {
                    viewModel.dismissGenerationSelection()
                    visibleSelectionSheet = null
                },
            )
        }

        null -> {
            Unit
        }
    }

    if (isAlreadyRegisteredDialogVisible) {
        SignupAlreadyRegisteredDialog(onDismiss = { isAlreadyRegisteredDialogVisible = false })
    }
}

/** Route가 띄우는 선택 시트 종류다. */
private enum class SignupSelectionSheet {
    Department,
    Generation,
}
