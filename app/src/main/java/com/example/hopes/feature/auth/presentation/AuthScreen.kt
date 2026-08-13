package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing

/** 로그인·회원가입 흐름을 화면 크기에 맞춰 세로로 배치한다. */
@Composable
fun AuthScreen(
    authStep: AuthStep,
    emailText: String,
    passwordText: String,
    nameText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onNavigateLogin: () -> Unit,
    onDismissLogin: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.ScreenHorizontal),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        Text(stringResource(if (authStep == AuthStep.SignUp) R.string.signup else R.string.login))
        if (authStep == AuthStep.Guide) {
            Text(stringResource(R.string.auth_title))
            Button(onClick = onNavigateLogin) { Text(stringResource(R.string.login)) }
        } else {
            OutlinedTextField(emailText, onEmailChange, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.auth_email)) })
            OutlinedTextField(passwordText, onPasswordChange, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.password)) })
            if (authStep == AuthStep.SignUp) {
                OutlinedTextField(nameText, onNameChange, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.name)) })
                Button(onClick = onSignupClick) { Text(stringResource(R.string.signup)) }
                Button(onClick = onNavigateLogin) { Text(stringResource(R.string.login)) }
            } else {
                Button(onClick = onLoginClick) { Text(stringResource(R.string.login)) }
                Button(onClick = onNavigateSignup) { Text(stringResource(R.string.signup)) }
                Button(onClick = onDismissLogin) { Text(stringResource(R.string.back)) }
            }
        }
    }
}
