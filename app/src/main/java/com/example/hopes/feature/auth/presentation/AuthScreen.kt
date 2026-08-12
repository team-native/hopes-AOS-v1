package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesLightLogo
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 피그마 01~04 인증 화면을 로컬 입력 상태와 함께 제공한다. */
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
    when (authStep) {
        AuthStep.Guide -> AuthGuideContent(onNavigateLogin = onNavigateLogin)
        AuthStep.Login -> AuthFormScreen(
            title = stringResource(R.string.login),
            subtitle = stringResource(R.string.login_subtitle),
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
        AuthStep.SignUp -> AuthFormScreen(
            title = stringResource(R.string.signup),
            subtitle = stringResource(R.string.signup_subtitle),
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

@Composable
private fun AuthGuideContent(onNavigateLogin: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_guide_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 402.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 32.dp, vertical = 52.dp),
        ) {
            HopesLightLogo()

            Spacer(modifier = Modifier.height(92.dp))

            Text(
                text = stringResource(R.string.auth_title),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.auth_description),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            HopesSurfaceCard(
                modifier = Modifier,
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(R.string.auth_swipe),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                TextButton(onClick = onNavigateLogin) {
                    Text(text = stringResource(R.string.login))
                }
            }
        }
    }
}

@Composable
private fun AuthFormScreen(
    title: String,
    subtitle: String,
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
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 52.dp)) {
                HopesLightLogo()
                Spacer(modifier = Modifier.height(28.dp))
                Text(text = title, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineMedium)
                Text(text = subtitle, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
            }
        }
        HopesSurfaceCard(modifier = Modifier.padding(horizontal = 24.dp, vertical = 26.dp)) {
            OutlinedTextField(value = emailText, onValueChange = onEmailChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) }, singleLine = true)
            OutlinedTextField(value = passwordText, onValueChange = onPasswordChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.password)) }, singleLine = true)
            if (nameText != null) {
                OutlinedTextField(value = nameText, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.name)) }, singleLine = true)
                OutlinedTextField(value = "AI", onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.department)) }, singleLine = true)
                OutlinedTextField(value = "10기", onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.generation)) }, singleLine = true)
            }
            HopesPrimaryButton(text = actionText, onClick = onActionClick)
            TextButton(onClick = onFooterClick, modifier = Modifier.fillMaxWidth()) {
                Text(text = footerText, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun OnboardingContent(onStartChat: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.login_guide_background), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 52.dp),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            HopesLightLogo()
            Spacer(modifier = Modifier.height(48.dp))
            Text(text = stringResource(R.string.onboarding_title), color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineLarge)
            Text(text = stringResource(R.string.onboarding_description), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyLarge)
            HopesSurfaceCard { Text(text = stringResource(R.string.onboarding_tip_one)) }
            HopesSurfaceCard { Text(text = stringResource(R.string.onboarding_tip_two)) }
            Spacer(modifier = Modifier.weight(1f))
            HopesPrimaryButton(text = stringResource(R.string.start_chat), onClick = onStartChat)
        }
    }
}
