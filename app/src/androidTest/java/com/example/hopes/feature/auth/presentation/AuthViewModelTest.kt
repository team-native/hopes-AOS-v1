package com.example.hopes.feature.auth.presentation

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.PasswordResetRequest
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.ConfirmSignupVerificationUseCase
import com.example.hopes.domain.usecase.LoginUseCase
import com.example.hopes.domain.usecase.RequestPasswordResetUseCase
import com.example.hopes.domain.usecase.ResetPasswordUseCase
import com.example.hopes.domain.usecase.SendSignupVerificationUseCase
import com.example.hopes.domain.usecase.SignUpUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthViewModelTest {
    @Test
    fun loginWithBlankCredentials_doesNotShowServerErrorMessage() {
        val repository = RecordingAuthRepository()
        val viewModel = AuthViewModel(
            loginUseCase = LoginUseCase(repository),
            requestPasswordResetUseCase = RequestPasswordResetUseCase(repository),
            resetPasswordUseCase = ResetPasswordUseCase(repository),
            sendSignupVerificationUseCase = SendSignupVerificationUseCase(repository),
            confirmSignupVerificationUseCase = ConfirmSignupVerificationUseCase(repository),
            signUpUseCase = SignUpUseCase(repository),
        )

        viewModel.onEvent(AuthScreenEvent.LoginClicked)

        assertNull(viewModel.uiState.value.errorMessage)
        assertEquals(0, repository.loginCallCount)
    }
}

private class RecordingAuthRepository : AuthRepository {
    var loginCallCount: Int = 0

    override suspend fun login(username: String, password: String): AppResult<AuthToken> {
        loginCallCount += 1
        return AppResult.Success(AuthToken(accessToken = "access-token", tokenType = "Bearer"))
    }

    override suspend fun signUp(request: SignUpRequest): AppResult<AuthToken> {
        return AppResult.Success(AuthToken(accessToken = "access-token", tokenType = "Bearer"))
    }

    override suspend fun sendSignupCode(email: String): AppResult<Unit> {
        return AppResult.Success(Unit)
    }

    override suspend fun confirmSignupCode(email: String, code: String): AppResult<Unit> {
        return AppResult.Success(Unit)
    }

    override suspend fun requestPasswordReset(email: String): AppResult<String> {
        return AppResult.Success("")
    }

    override suspend fun resetPassword(request: PasswordResetRequest): AppResult<Unit> {
        return AppResult.Success(Unit)
    }
}
