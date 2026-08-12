package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.hopes.R

/** 인증 데모의 화면 전환과 입력 상태를 소유한다. */
@Composable
fun AuthRoute(onAuthenticated: () -> Unit) {
    var authStep by rememberSaveable { mutableStateOf(AuthStep.Guide) }
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var nameText by rememberSaveable { mutableStateOf("") }
    val signupEmailSample = stringResource(R.string.signup_email_hint)
    val signupNameSample = stringResource(R.string.signup_name_hint)

    AuthScreen(
        authStep = authStep,
        emailText = emailText,
        passwordText = passwordText,
        nameText = nameText,
        onEmailChange = { emailText = it },
        onPasswordChange = { passwordText = it },
        onNameChange = { nameText = it },
        onLoginClick = {
            if (emailText.isNotBlank() && passwordText.isNotBlank()) {
                onAuthenticated()
            }
        },
        onSignupClick = {
            if (emailText.isNotBlank() && passwordText.isNotBlank() && nameText.isNotBlank()) {
                onAuthenticated()
            }
        },
        onNavigateSignup = {
            // 원본 회원가입 프레임의 예시 값을 첫 진입에만 보여 주되, 사용자가 입력한 값은 유지한다.
            if (emailText.isBlank()) emailText = signupEmailSample
            if (nameText.isBlank()) nameText = signupNameSample
            authStep = AuthStep.SignUp
        },
        onNavigateLogin = {
            authStep = AuthStep.Login
        },
        onDismissLogin = { authStep = AuthStep.Guide },
    )
}

enum class AuthStep {
    Guide,
    Login,
    SignUp,
}
