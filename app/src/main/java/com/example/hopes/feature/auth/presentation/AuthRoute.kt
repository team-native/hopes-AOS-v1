package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.domain.result.AppError
import com.example.hopes.R

/** 인증 데모의 화면 전환과 입력 상태를 소유한다. */
@Composable
fun AuthRoute(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var authStep by rememberSaveable { mutableStateOf(AuthStep.Guide) }
    var nameText by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val signupEmailSample = stringResource(R.string.signup_email_hint)
    val signupNameSample = stringResource(R.string.signup_name_hint)

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onAuthenticated()
        }
    }

    AuthScreen(
        authStep = authStep,
        emailText = uiState.username,
        passwordText = uiState.password,
        nameText = nameText,
        onEmailChange = viewModel::updateUsername,
        onPasswordChange = viewModel::updatePassword,
        onNameChange = { nameText = it },
        onLoginClick = viewModel::login,
        onSignupClick = {
            Toast.makeText(context, R.string.signup_unavailable, Toast.LENGTH_SHORT).show()
        },
        onNavigateSignup = {
            // 원본 회원가입 프레임의 예시 값을 첫 진입에만 보여 주되, 사용자가 입력한 값은 유지한다.
            if (uiState.username.isBlank()) viewModel.updateUsername(signupEmailSample)
            if (nameText.isBlank()) nameText = signupNameSample
            authStep = AuthStep.SignUp
        },
        onNavigateLogin = {
            authStep = AuthStep.Login
        },
        onDismissLogin = { authStep = AuthStep.Guide },
        loginErrorText = uiState.error?.toAuthErrorText(),
        isLoginLoading = uiState.isLoading,
    )
}

@Composable
private fun AppError.toAuthErrorText(): String {
    val textRes = when (this) {
        AppError.Unauthorized -> R.string.login_error_invalid_credentials
        AppError.Validation -> R.string.login_error_validation
        AppError.RateLimited -> R.string.login_error_rate_limited
        AppError.Network -> R.string.login_error_network
        else -> R.string.login_error_unknown
    }

    return stringResource(textRes)
}

enum class AuthStep {
    Guide,
    Login,
    SignUp,
}
