package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.feature.auth.presentation.component.FigmaAuthBrandHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.ui.theme.LocalHopesExtendedColors

@Composable
fun AuthScreen(authStep: AuthStep, emailText: String, passwordText: String, nameText: String, onEmailChange: (String) -> Unit, onPasswordChange: (String) -> Unit, onNameChange: (String) -> Unit, onLoginClick: () -> Unit, onSignupClick: () -> Unit, onNavigateSignup: () -> Unit, onNavigateLogin: () -> Unit, onDismissLogin: () -> Unit) {
    val colors = LocalHopesExtendedColors.current
    Box(Modifier.fillMaxSize()) {
        if (authStep == AuthStep.SignUp) Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(colors.signupGradientStart, colors.signupGradientEnd)))) else Image(painterResource(R.drawable.login_guide_background), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Column(Modifier.fillMaxSize().imePadding().padding(horizontal = AppSpacing.ScreenHorizontal, vertical = 32.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            FigmaAuthBrandHeader()
            Text(if (authStep == AuthStep.SignUp) stringResource(R.string.signup_hero_title) else stringResource(R.string.auth_title), color = MaterialTheme.colorScheme.onPrimary, style = TextStyle(fontSize = 31.sp, fontWeight = FontWeight.Bold, lineHeight = 39.sp))
            Text(stringResource(R.string.auth_description), color = colors.authDescription, style = TextStyle(fontSize = 15.sp, lineHeight = 24.sp))
            if (authStep == AuthStep.Guide) { HopesPrimaryButton(stringResource(R.string.login), onNavigateLogin); return@Column }
            Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp)).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (authStep == AuthStep.SignUp) stringResource(R.string.signup) else stringResource(R.string.login), style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold))
                FigmaAuthTextField(emailText, onEmailChange, R.string.auth_email)
                FigmaAuthTextField(passwordText, onPasswordChange, R.string.password, isPassword = true, onImeAction = if (authStep == AuthStep.Login) onLoginClick else onSignupClick)
                if (authStep == AuthStep.SignUp) FigmaAuthTextField(nameText, onNameChange, R.string.name)
                HopesPrimaryButton(if (authStep == AuthStep.SignUp) stringResource(R.string.signup) else stringResource(R.string.login), if (authStep == AuthStep.SignUp) onSignupClick else onLoginClick)
                Text(if (authStep == AuthStep.SignUp) stringResource(R.string.has_account) else stringResource(R.string.no_account), Modifier.align(Alignment.CenterHorizontally).clickable { if (authStep == AuthStep.SignUp) onNavigateLogin() else onNavigateSignup() }, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
