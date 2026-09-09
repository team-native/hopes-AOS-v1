package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.R
import com.example.hopes.feature.auth.presentation.component.FigmaSingleSelectionDialog
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState

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
        verificationCodeText = uiState.value.verificationCode,
        signupValidation = uiState.value.signupValidation,
        isSignupLoading = uiState.value.isLoading,
        signupErrorMessage = uiState.value.errorMessage,
        loginErrorMessage = uiState.value.errorMessage,
        loginStatusMessage = uiState.value.statusMessage,
        onEmailChange = { value -> viewModel.onEvent(AuthScreenEvent.EmailChanged(value)) },
        onPasswordChange = { value -> viewModel.onEvent(AuthScreenEvent.PasswordChanged(value)) },
        onNameChange = { value -> viewModel.onEvent(AuthScreenEvent.NameChanged(value)) },
        onDepartmentClick = { isDepartmentDialogVisible = true },
        onGenerationClick = { isGenerationDialogVisible = true },
        onVerificationCodeChange = { value -> viewModel.onEvent(AuthScreenEvent.VerificationCodeChanged(value)) },
        onSendVerificationCodeClick = { viewModel.onEvent(AuthScreenEvent.SendSignupVerificationClicked) },
        onLoginClick = { viewModel.onEvent(AuthScreenEvent.LoginClicked) },
        onSignupClick = { viewModel.onEvent(AuthScreenEvent.SignUpClicked) },
        onNavigateSignup = { viewModel.onEvent(AuthScreenEvent.SignUpRequested) },
        onNavigateLogin = { viewModel.onEvent(AuthScreenEvent.LoginRequested) },
        onDismissLogin = { viewModel.onEvent(AuthScreenEvent.LoginDismissed) },
        onForgotPasswordClick = { viewModel.onEvent(AuthScreenEvent.ForgotPasswordClicked) },
        passwordResetUiState = PasswordResetUiState(
            email = uiState.value.passwordResetEmail,
            code = uiState.value.passwordResetCode,
            newPassword = uiState.value.passwordResetNewPassword,
            isLoading = uiState.value.isLoading,
            errorMessage = uiState.value.errorMessage,
            statusMessage = uiState.value.statusMessage,
        ),
        onPasswordResetEvent = { event ->
            when (event) {
                is PasswordResetScreenEvent.EmailChanged ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetEmailChanged(event.value))
                is PasswordResetScreenEvent.CodeChanged ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetCodeChanged(event.value))
                is PasswordResetScreenEvent.NewPasswordChanged ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetNewPasswordChanged(event.value))
                PasswordResetScreenEvent.RequestCodeClicked ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetRequestClicked)
                PasswordResetScreenEvent.SubmitClicked ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetSubmitClicked)
                PasswordResetScreenEvent.BackClicked ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetBackClicked)
            }
        },
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
    PasswordResetRequest,
    SignUp,
}
