package com.example.hopes.feature.auth.presentation

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.PasswordResetRequest
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.LoginUseCase
import com.example.hopes.domain.usecase.SendSignupCodeUseCase
import com.example.hopes.domain.usecase.SignUpUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        viewModel = AuthViewModel(
            loginUseCase = LoginUseCase(fakeAuthRepository),
            sendSignupCodeUseCase = SendSignupCodeUseCase(fakeAuthRepository),
            signUpUseCase = SignUpUseCase(fakeAuthRepository),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `입력값을 수정하면 해당 필드의 형식 오류가 즉시 갱신된다`() {
        viewModel.onEvent(AuthScreenEvent.EmailChanged("student@example.com"))
        viewModel.onEvent(AuthScreenEvent.NameChanged("임서하"))
        viewModel.onEvent(AuthScreenEvent.PasswordChanged("password1"))

        val signupValidation = viewModel.uiState.value.signupValidation

        assertEquals(SignupInputError.InvalidSchoolEmail, signupValidation.emailError)
        assertNull(signupValidation.nameError)
        assertNull(signupValidation.passwordError)
    }

    @Test
    fun `회원가입 클릭 시 모든 필수 검증 오류를 표시하고 인증 요청을 중단한다`() {
        viewModel.onEvent(AuthScreenEvent.SignUpClicked)

        val signupValidation = viewModel.uiState.value.signupValidation

        assertEquals(SignupInputError.InvalidSchoolEmail, signupValidation.emailError)
        assertEquals(SignupInputError.InvalidUsername, signupValidation.nameError)
        assertEquals(SignupInputError.GenerationRequired, signupValidation.generationError)
        assertEquals(SignupInputError.InvalidPassword, signupValidation.passwordError)
        assertNull(fakeAuthRepository.signUpRequest)
    }

    @Test
    fun `번호 발송 클릭은 이메일 인증번호 발송만 요청하고 회원가입 화면을 유지한다`() = runTest(testDispatcher) {
        fillValidSignupForm()

        viewModel.onEvent(AuthScreenEvent.SendVerificationCodeClicked)
        advanceUntilIdle()

        assertEquals("s26055@gsm.hs.kr", fakeAuthRepository.sentVerificationEmail)
        assertEquals(AuthStep.SignUp, viewModel.uiState.value.authStep)
        assertNull(viewModel.uiState.value.requestError)
    }

    @Test
    fun `인증번호는 숫자 여섯 자리만 유지한다`() {
        viewModel.onEvent(AuthScreenEvent.VerificationCodeChanged("12a345678"))

        assertEquals("123456", viewModel.uiState.value.verificationCode)
    }

    @Test
    fun `회원가입 클릭은 인증번호 확인 API 없이 가입 요청에 선택값을 매핑한다`() = runTest(testDispatcher) {
        fillValidSignupForm()
        viewModel.onEvent(AuthScreenEvent.VerificationCodeChanged("123456"))

        viewModel.onEvent(AuthScreenEvent.SignUpClicked)
        advanceUntilIdle()

        assertEquals(
            SignUpRequest(
                email = "s26055@gsm.hs.kr",
                username = "임서하",
                password = "password1",
                passwordConfirm = "password1",
                verificationCode = "123456",
                major = "AI",
                cohort = 10,
            ),
            fakeAuthRepository.signUpRequest,
        )
    }

    @Test
    fun `번호 발송 실패 시 회원가입 카드에 오류를 유지한다`() = runTest(testDispatcher) {
        fakeAuthRepository.sendSignupCodeResult = AppResult.NetworkError
        fillValidSignupForm()

        viewModel.onEvent(AuthScreenEvent.SendVerificationCodeClicked)
        advanceUntilIdle()

        assertEquals(AuthStep.SignUp, viewModel.uiState.value.authStep)
        assertEquals(
            AuthRequestError.SendVerificationCodeFailed,
            viewModel.uiState.value.requestError,
        )
    }

    private fun fillValidSignupForm() {
        viewModel.onEvent(AuthScreenEvent.SignUpRequested)
        viewModel.onEvent(AuthScreenEvent.EmailChanged("s26055@gsm.hs.kr"))
        viewModel.onEvent(AuthScreenEvent.NameChanged("임서하"))
        viewModel.onEvent(AuthScreenEvent.DepartmentChanged("AI과"))
        viewModel.onEvent(AuthScreenEvent.GenerationChanged("10기"))
        viewModel.onEvent(AuthScreenEvent.PasswordChanged("password1"))
    }
}

private class FakeAuthRepository : AuthRepository {
    var sendSignupCodeResult: AppResult<Unit> = AppResult.Success(Unit)
    var signUpResult: AppResult<AuthToken> = AppResult.Success(AuthToken("access", "Bearer"))
    var sentVerificationEmail: String? = null
    var signUpRequest: SignUpRequest? = null

    override suspend fun login(username: String, password: String): AppResult<AuthToken> {
        return AppResult.NetworkError
    }

    override suspend fun signUp(request: SignUpRequest): AppResult<AuthToken> {
        signUpRequest = request
        return signUpResult
    }

    override suspend fun sendSignupCode(email: String): AppResult<Unit> {
        sentVerificationEmail = email
        return sendSignupCodeResult
    }

    override suspend fun requestPasswordReset(email: String): AppResult<Unit> {
        return AppResult.NetworkError
    }

    override suspend fun resetPassword(request: PasswordResetRequest): AppResult<Unit> {
        return AppResult.NetworkError
    }
}
