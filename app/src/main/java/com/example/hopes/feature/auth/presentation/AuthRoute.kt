package com.example.hopes.feature.auth.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState
import com.example.hopes.feature.auth.presentation.signup.view.SignupRoute

/** 인증 데모의 화면 전환과 입력 상태를 소유한다. */
@Composable
fun AuthRoute(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            if (effect is AuthEffect.Authenticated) {
                onAuthenticated()
            }
        }
    }

    // 시작 화면과 로그인 화면이 분리되면서, 로그인에서 뒤로가기는 앱 종료 대신 시작 화면으로 돌아간다.
    BackHandler(enabled = uiState.value.authStep == AuthStep.Login) {
        viewModel.onEvent(AuthScreenEvent.GuideRequested)
    }

    AuthScreen(
        authStep = uiState.value.authStep,
        loginUiState =
            AuthLoginUiState(
                emailText = uiState.value.email,
                passwordText = uiState.value.password,
                isKeepLoginChecked = uiState.value.isKeepLoginChecked,
                isLoading = uiState.value.isLoading,
                failureMessage = uiState.value.errorMessage,
                statusMessage = uiState.value.statusMessage,
                isOffline = uiState.value.isLoginOffline,
            ),
        onLoginEvent = { event ->
            when (event) {
                is AuthLoginScreenEvent.EmailChanged -> {
                    viewModel.onEvent(AuthScreenEvent.EmailChanged(event.value))
                }

                is AuthLoginScreenEvent.PasswordChanged -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordChanged(event.value))
                }

                is AuthLoginScreenEvent.KeepLoginChanged -> {
                    viewModel.onEvent(AuthScreenEvent.KeepLoginChanged(event.isChecked))
                }

                AuthLoginScreenEvent.LoginClicked -> {
                    viewModel.onEvent(AuthScreenEvent.LoginClicked)
                }

                AuthLoginScreenEvent.FindPasswordClicked -> {
                    viewModel.onEvent(AuthScreenEvent.ForgotPasswordClicked)
                }

                AuthLoginScreenEvent.SignUpClicked -> {
                    viewModel.onEvent(AuthScreenEvent.SignUpRequested)
                }
            }
        },
        onNavigateSignup = { viewModel.onEvent(AuthScreenEvent.SignUpRequested) },
        onNavigateLogin = { viewModel.onEvent(AuthScreenEvent.LoginRequested) },
        passwordResetUiState =
            PasswordResetUiState(
                email = uiState.value.passwordResetEmail,
                code = uiState.value.passwordResetCode,
                newPassword = uiState.value.passwordResetNewPassword,
                isLoading = uiState.value.isLoading,
                errorMessage = uiState.value.errorMessage,
                statusMessage = uiState.value.statusMessage,
            ),
        onPasswordResetEvent = { event ->
            when (event) {
                is PasswordResetScreenEvent.EmailChanged -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordResetEmailChanged(event.value))
                }

                is PasswordResetScreenEvent.CodeChanged -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordResetCodeChanged(event.value))
                }

                is PasswordResetScreenEvent.NewPasswordChanged -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordResetNewPasswordChanged(event.value))
                }

                PasswordResetScreenEvent.RequestCodeClicked -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordResetRequestClicked)
                }

                PasswordResetScreenEvent.SubmitClicked -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordResetSubmitClicked)
                }

                PasswordResetScreenEvent.BackClicked -> {
                    viewModel.onEvent(AuthScreenEvent.PasswordResetBackClicked)
                }
            }
        },
        signupContent = {
            SignupRoute(
                onNavigateBack = { viewModel.onEvent(AuthScreenEvent.GuideRequested) },
                onSignupCompleted = { email, message ->
                    viewModel.onEvent(AuthScreenEvent.SignupCompleted(email = email, message = message))
                },
            )
        },
    )
}

enum class AuthStep {
    Guide,
    Login,
    PasswordResetRequest,
    SignUp,
}
