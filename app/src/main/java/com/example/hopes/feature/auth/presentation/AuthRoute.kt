package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** 인증 데모의 화면 전환과 입력 상태를 소유한다. */
@Composable
fun AuthRoute(onAuthenticated: () -> Unit) {
    var authStep by rememberSaveable { mutableStateOf(AuthStep.Guide) }
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var nameText by rememberSaveable { mutableStateOf("") }

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
                authStep = AuthStep.Onboarding
            }
        },
        onSignupClick = {
            if (emailText.isNotBlank() && passwordText.isNotBlank() && nameText.isNotBlank()) {
                authStep = AuthStep.Onboarding
            }
        },
        onNavigateSignup = { authStep = AuthStep.SignUp },
        onNavigateLogin = { authStep = AuthStep.Login },
        onDismissLogin = { authStep = AuthStep.Guide },
        onStartChat = onAuthenticated,
    )
}

enum class AuthStep {
    Guide,
    Login,
    SignUp,
    Onboarding,
}
