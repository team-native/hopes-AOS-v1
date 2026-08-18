package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.R
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState
import com.example.hopes.feature.auth.presentation.signupverification.SignUpCodeConfirmationScreenEvent
import com.example.hopes.feature.auth.presentation.signupverification.SignUpCodeConfirmationUiState
import com.example.hopes.feature.auth.presentation.signupverification.SignUpEmailVerificationScreenEvent
import com.example.hopes.feature.auth.presentation.signupverification.SignUpEmailVerificationUiState

/** 인증 데모의 화면 전환과 입력 상태를 소유한다. */
@Composable
fun AuthRoute(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val signupEmailSample = stringResource(R.string.signup_email_hint)
    val signupNameSample = stringResource(R.string.signup_name_hint)

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
        passwordConfirmText = uiState.value.passwordConfirm,
        onEmailChange = { value -> viewModel.onEvent(AuthScreenEvent.EmailChanged(value)) },
        onPasswordChange = { value -> viewModel.onEvent(AuthScreenEvent.PasswordChanged(value)) },
        onNameChange = { value -> viewModel.onEvent(AuthScreenEvent.NameChanged(value)) },
        onPasswordConfirmChange = { value -> viewModel.onEvent(AuthScreenEvent.PasswordConfirmChanged(value)) },
        onLoginClick = { viewModel.onEvent(AuthScreenEvent.LoginClicked) },
        onSignupClick = { viewModel.onEvent(AuthScreenEvent.SignUpClicked) },
        onNavigateSignup = {
            viewModel.onEvent(AuthScreenEvent.SignUpRequested(signupEmailSample, signupNameSample))
        },
        onNavigateLogin = { viewModel.onEvent(AuthScreenEvent.LoginRequested) },
        onDismissLogin = { viewModel.onEvent(AuthScreenEvent.LoginDismissed) },
        onForgotPasswordClick = { viewModel.onEvent(AuthScreenEvent.ForgotPasswordClicked) },
        passwordResetUiState = PasswordResetUiState(
            email = uiState.value.passwordResetEmail,
            code = uiState.value.passwordResetCode,
            newPassword = uiState.value.passwordResetNewPassword,
            newPasswordConfirm = uiState.value.passwordResetNewPasswordConfirm,
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
                is PasswordResetScreenEvent.NewPasswordConfirmChanged ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetNewPasswordConfirmChanged(event.value))
                PasswordResetScreenEvent.RequestCodeClicked ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetRequestClicked)
                PasswordResetScreenEvent.SubmitClicked ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetSubmitClicked)
                PasswordResetScreenEvent.BackClicked ->
                    viewModel.onEvent(AuthScreenEvent.PasswordResetBackClicked)
            }
        },
        signUpEmailVerificationUiState = SignUpEmailVerificationUiState(
            email = uiState.value.email,
            isLoading = uiState.value.isLoading,
            errorMessage = uiState.value.errorMessage,
        ),
        onSignUpEmailVerificationEvent = { event ->
            when (event) {
                is SignUpEmailVerificationScreenEvent.EmailChanged ->
                    viewModel.onEvent(AuthScreenEvent.EmailChanged(event.value))
                SignUpEmailVerificationScreenEvent.RequestCodeClicked ->
                    viewModel.onEvent(AuthScreenEvent.SendSignupVerificationClicked)
                SignUpEmailVerificationScreenEvent.BackClicked ->
                    viewModel.onEvent(AuthScreenEvent.SignUpEmailVerificationBackClicked)
            }
        },
        signUpCodeConfirmationUiState = SignUpCodeConfirmationUiState(
            code = uiState.value.verificationCode,
            isLoading = uiState.value.isLoading,
            errorMessage = uiState.value.errorMessage,
        ),
        onSignUpCodeConfirmationEvent = { event ->
            when (event) {
                is SignUpCodeConfirmationScreenEvent.CodeChanged ->
                    viewModel.onEvent(AuthScreenEvent.VerificationCodeChanged(event.value))
                SignUpCodeConfirmationScreenEvent.ConfirmClicked ->
                    viewModel.onEvent(AuthScreenEvent.ConfirmSignupVerificationClicked)
                SignUpCodeConfirmationScreenEvent.BackClicked ->
                    viewModel.onEvent(AuthScreenEvent.SignUpCodeConfirmationBackClicked)
            }
        },
    )
}

enum class AuthStep {
    Guide,
    Login,
    PasswordResetRequest,
    SignUpEmailVerification,
    SignUpCodeConfirmation,
    SignUp,
}
