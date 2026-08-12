package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesLightLogo
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 로그인, 회원가입, 온보딩의 표현을 상태에 따라 전환한다. */
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
    onStartChat: () -> Unit,
) {
    val isGradientScreen = authStep == AuthStep.Guide || authStep == AuthStep.Onboarding
    val backgroundModifier = if (isGradientScreen) {
        Modifier.background(
            Brush.verticalGradient(
                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer),
            ),
        )
    } else {
        Modifier.background(MaterialTheme.colorScheme.background)
    }

    Column(
        modifier = backgroundModifier
            .fillMaxSize()
            .padding(AppSpacing.ScreenHorizontal),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
    ) {
        if (isGradientScreen) HopesLightLogo()

        when (authStep) {
            AuthStep.Guide -> AuthGuideContent(onNavigateLogin = onNavigateLogin)
            AuthStep.Login -> AuthFormContent(
                title = stringResource(R.string.login),
                emailText = emailText,
                passwordText = passwordText,
                nameText = null,
                actionText = stringResource(R.string.login),
                footerText = stringResource(R.string.no_account),
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onNameChange = {},
                onActionClick = onLoginClick,
                onFooterClick = onNavigateSignup,
            )
            AuthStep.SignUp -> AuthFormContent(
                title = stringResource(R.string.signup),
                emailText = emailText,
                passwordText = passwordText,
                nameText = nameText,
                actionText = stringResource(R.string.signup),
                footerText = stringResource(R.string.has_account),
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onNameChange = onNameChange,
                onActionClick = onSignupClick,
                onFooterClick = onNavigateLogin,
            )
            AuthStep.Onboarding -> OnboardingContent(onStartChat = onStartChat)
        }
    }
}

@Composable
private fun AuthGuideContent(onNavigateLogin: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
        Text(text = stringResource(R.string.auth_title), color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineLarge)
        Text(text = stringResource(R.string.auth_description), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyLarge)
        HopesSurfaceCard {
            Text(text = stringResource(R.string.login), style = MaterialTheme.typography.titleMedium)
            Text(text = stringResource(R.string.auth_swipe), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onNavigateLogin) { Text(text = stringResource(R.string.login)) }
        }
    }
}

@Composable
private fun AuthFormContent(
    title: String,
    emailText: String,
    passwordText: String,
    nameText: String?,
    actionText: String,
    footerText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onActionClick: () -> Unit,
    onFooterClick: () -> Unit,
) {
    HopesSurfaceCard {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(value = emailText, onValueChange = onEmailChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) })
        OutlinedTextField(value = passwordText, onValueChange = onPasswordChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.password)) })
        if (nameText != null) OutlinedTextField(value = nameText, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.name)) })
        HopesPrimaryButton(text = actionText, onClick = onActionClick)
        TextButton(onClick = onFooterClick, modifier = Modifier.fillMaxWidth()) { Text(footerText) }
    }
}

@Composable
private fun OnboardingContent(onStartChat: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
        Text(text = stringResource(R.string.onboarding_title), color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineLarge)
        Text(text = stringResource(R.string.onboarding_description), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyLarge)
        HopesSurfaceCard { Text(text = stringResource(R.string.onboarding_tip_one)) }
        HopesSurfaceCard { Text(text = stringResource(R.string.onboarding_tip_two)) }
        HopesPrimaryButton(text = stringResource(R.string.start_chat), onClick = onStartChat)
    }
}
