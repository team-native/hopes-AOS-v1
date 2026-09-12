package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.feature.auth.presentation.SignupValidationUiState
import com.example.hopes.feature.auth.presentation.component.AuthSignUpBackground
import com.example.hopes.feature.auth.presentation.component.AuthSignUpFooterLink
import com.example.hopes.feature.auth.presentation.component.AuthSignUpHeroTitle
import com.example.hopes.feature.auth.presentation.component.SignupActionButton

/** 피그마 03 회원가입 화면 콘텐츠다. 헤더부터 로그인 유도 문구까지 세로로 배치한다. */
@Composable
fun AuthSignUpScreenContent(
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCodeText: String,
    signupValidation: SignupValidationUiState,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCodeClick: () -> Unit,
    onActionClick: () -> Unit,
    onFooterClick: () -> Unit,
) {
    val isSignupEnabled = !isLoading
    val signupEmailHint = stringResource(R.string.signup_email_hint)
    val signupNameHint = stringResource(R.string.signup_name_hint)
    val signupDepartmentHint = stringResource(R.string.signup_department_hint)
    val signupGenerationHint = stringResource(R.string.signup_generation_hint)
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    // 첫 번째 페이지를 화면 높이만큼 유지해 카드 내부 스크롤 구조를 보존하고,
    // 페이지 아래의 로그인 링크는 LazyColumn의 다음 아이템으로 배치한다.
    // 페이지에 배경을 함께 넣어 스크롤 시 파랑-하얀 경계도 콘텐츠와 같이 이동한다.
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxHeight),
                ) {
                    AuthSignUpBackground(modifier = Modifier.fillMaxSize())

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding(),
                    ) {
                        FigmaBrandHeader(
                            modifier = Modifier.padding(start = 32.dp, top = 25.dp),
                            isOnBlueBackground = true,
                        )

                        Spacer(modifier = Modifier.height(87.dp))

                        AuthSignUpHeroTitle(modifier = Modifier.padding(start = 32.dp))

                        Spacer(modifier = Modifier.height(52.dp))

                        SignupFormSectionContent(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            emailText = emailText,
                            passwordText = passwordText,
                            nameText = nameText,
                            departmentText = departmentText,
                            generationText = generationText,
                            verificationCodeText = verificationCodeText,
                            signupValidation = signupValidation,
                            isSending = isLoading,
                            errorMessage = errorMessage,
                            emailHint = signupEmailHint,
                            nameHint = signupNameHint,
                            departmentHint = signupDepartmentHint,
                            generationHint = signupGenerationHint,
                            isSignupEnabled = isSignupEnabled,
                            onEmailChange = onEmailChange,
                            onPasswordChange = onPasswordChange,
                            onNameChange = onNameChange,
                            onDepartmentClick = onDepartmentClick,
                            onGenerationClick = onGenerationClick,
                            onVerificationCodeChange = onVerificationCodeChange,
                            onSendVerificationCodeClick = onSendVerificationCodeClick,
                            onSignupClick = onActionClick,
                        )

                        if (!isImeVisible) {
                            Spacer(modifier = Modifier.height(42.dp))

                            SignupActionButton(
                                isEnabled = isSignupEnabled,
                                onClick = onActionClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                            )
                        }
                    }
                }
            }

            if (!isImeVisible) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(10.dp))

                        AuthSignUpFooterLink(onClick = onFooterClick)

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}
