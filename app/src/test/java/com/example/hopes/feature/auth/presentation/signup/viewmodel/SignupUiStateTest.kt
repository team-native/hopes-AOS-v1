package com.example.hopes.feature.auth.presentation.signup.viewmodel

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupUiStateTest {
    @Test
    fun codeConfirm_isBlockedAfterExpiryOrTooManyAttempts() {
        val sentState =
            SignupUiState(
                email = "s26055@gsm.hs.kr",
                isCodeSent = true,
                code = "123456",
            )

        assertTrue(sentState.canConfirmCode)
        assertFalse(sentState.copy(code = "12345").canConfirmCode)
        assertFalse(sentState.copy(codeError = SignupCodeError.Expired).canConfirmCode)
        assertFalse(sentState.copy(codeError = SignupCodeError.AttemptsExceeded).canConfirmCode)
        assertTrue(sentState.copy(codeError = SignupCodeError.Mismatch(failedAttemptCount = 2)).canConfirmCode)
    }

    @Test
    fun passwordErrors_appearOnlyAfterConfirmInputStarts() {
        val typingState = SignupUiState(password = "abc123")

        assertFalse(typingState.isPasswordRuleErrorVisible)
        assertFalse(typingState.isPasswordMismatchVisible)
        assertTrue(typingState.copy(passwordConfirm = "abc12").isPasswordRuleErrorVisible)
        assertTrue(typingState.copy(passwordConfirm = "abc12").isPasswordMismatchVisible)
    }

    @Test
    fun passwordStep_canProceedOnlyWhenValidAndMatching() {
        assertTrue(SignupUiState(password = "abcd123!", passwordConfirm = "abcd123!").canSubmitPassword)
        assertFalse(SignupUiState(password = "abcd123!", passwordConfirm = "abcd123?").canSubmitPassword)
    }

    @Test
    fun signupCompletion_requiresAllProfileFieldsAndTerms() {
        val completeState =
            SignupUiState(
                step = SignupStep.Profile,
                name = "임서하",
                department = SignupDepartment.ArtificialIntelligence,
                generation = 10,
                isTermsAgreed = true,
            )

        assertTrue(completeState.canCompleteSignup)
        assertFalse(completeState.copy(generation = null).canCompleteSignup)
        assertFalse(completeState.copy(isTermsAgreed = false).canCompleteSignup)
        assertFalse(completeState.copy(name = " ").canCompleteSignup)
    }
}
