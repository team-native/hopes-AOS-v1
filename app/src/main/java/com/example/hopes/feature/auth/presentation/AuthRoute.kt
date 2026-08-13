package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.R
import com.example.hopes.feature.auth.presentation.component.FigmaSingleSelectionDialog

/** 인증 데모의 화면 전환과 입력 상태를 소유한다. */
@Composable
fun AuthRoute(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    var isDepartmentDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isGenerationDialogVisible by rememberSaveable { mutableStateOf(false) }
    val departmentOptions = listOf(
        stringResource(R.string.signup_department_software),
        stringResource(R.string.signup_department_iot),
        stringResource(R.string.signup_department_ai),
    )
    val generationOptions = listOf(
        stringResource(R.string.signup_generation_eighth),
        stringResource(R.string.signup_generation_ninth),
        stringResource(R.string.signup_generation_tenth),
    )

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            if (effect is AuthEffect.Authenticated) {
                onAuthenticated()
            }
        }
    }

    AuthScreen(
        authStep = uiState.value.authStep,
        emailText = uiState.value.email,
        passwordText = uiState.value.password,
        nameText = uiState.value.name,
        departmentText = uiState.value.department,
        generationText = uiState.value.generation,
        verificationCode = uiState.value.verificationCode,
        signupValidation = uiState.value.signupValidation,
        isLoading = uiState.value.isLoading,
        requestError = uiState.value.requestError,
        isSelectionDialogVisible = isDepartmentDialogVisible || isGenerationDialogVisible,
        onEmailChange = { value -> viewModel.onEvent(AuthScreenEvent.EmailChanged(value)) },
        onPasswordChange = { value -> viewModel.onEvent(AuthScreenEvent.PasswordChanged(value)) },
        onNameChange = { value -> viewModel.onEvent(AuthScreenEvent.NameChanged(value)) },
        onVerificationCodeChange = { value ->
            viewModel.onEvent(AuthScreenEvent.VerificationCodeChanged(value))
        },
        onDepartmentClick = { isDepartmentDialogVisible = true },
        onGenerationClick = { isGenerationDialogVisible = true },
        onLoginClick = { viewModel.onEvent(AuthScreenEvent.LoginClicked) },
        onSignupClick = { viewModel.onEvent(AuthScreenEvent.SignUpClicked) },
        onSendVerificationCode = {
            viewModel.onEvent(AuthScreenEvent.SendVerificationCodeClicked)
        },
        onNavigateSignup = { viewModel.onEvent(AuthScreenEvent.SignUpRequested) },
        onNavigateLogin = { viewModel.onEvent(AuthScreenEvent.LoginRequested) },
        onDismissLogin = { viewModel.onEvent(AuthScreenEvent.LoginDismissed) },
    )

    if (isDepartmentDialogVisible) {
        FigmaSingleSelectionDialog(
            titleRes = R.string.signup_department_dialog_title,
            selectedValue = uiState.value.department,
            options = departmentOptions,
            onValueSelected = { department ->
                viewModel.onEvent(AuthScreenEvent.DepartmentChanged(department))
                isDepartmentDialogVisible = false
            },
            onDismissRequest = { isDepartmentDialogVisible = false },
        )
    }

    if (isGenerationDialogVisible) {
        FigmaSingleSelectionDialog(
            titleRes = R.string.signup_generation_dialog_title,
            selectedValue = uiState.value.generation,
            options = generationOptions,
            onValueSelected = { generation ->
                viewModel.onEvent(AuthScreenEvent.GenerationChanged(generation))
                isGenerationDialogVisible = false
            },
            onDismissRequest = { isGenerationDialogVisible = false },
        )
    }
}

enum class AuthStep {
    Guide,
    Login,
    SignUp,
}
