package com.example.hopes.feature.auth.presentation.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.core.validation.isValidSchoolEmail
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.ConfirmSignupVerificationUseCase
import com.example.hopes.domain.usecase.SendSignupVerificationUseCase
import com.example.hopes.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** 회원가입 3단계 흐름과 인증번호 타이머·시도 횟수를 관리한다. */
@HiltViewModel
class SignupViewModel
    @Inject
    constructor(
        private val sendSignupVerificationUseCase: SendSignupVerificationUseCase,
        private val confirmSignupVerificationUseCase: ConfirmSignupVerificationUseCase,
        private val signUpUseCase: SignUpUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SignupUiState())
        val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

        // Route가 잠시 수집하지 않는 동안(화면 회전 등) 발생한 효과도 잃지 않도록 버퍼가 있는 Channel을 쓴다.
        private val _effect = Channel<SignupEffect>(Channel.BUFFERED)
        val effect: Flow<SignupEffect> = _effect.receiveAsFlow()

        private var codeTimerJob: Job? = null

        fun onEvent(event: SignupScreenEvent) {
            when (event) {
                SignupScreenEvent.BackClicked -> {
                    navigateBack()
                }

                SignupScreenEvent.PrimaryActionClicked -> {
                    handlePrimaryAction()
                }

                is SignupScreenEvent.EmailChanged -> {
                    updateEmail(event.value)
                }

                SignupScreenEvent.ChangeEmailClicked -> {
                    returnToEmailInput()
                }

                is SignupScreenEvent.CodeChanged -> {
                    updateCode(event.value)
                }

                SignupScreenEvent.ResendCodeClicked -> {
                    requestVerificationCode()
                }

                is SignupScreenEvent.PasswordChanged -> {
                    updateState { copy(password = event.value) }
                }

                is SignupScreenEvent.PasswordConfirmChanged -> {
                    updateState { copy(passwordConfirm = event.value) }
                }

                is SignupScreenEvent.NameChanged -> {
                    updateState { copy(name = event.value.take(MAX_NAME_LENGTH), requestError = null) }
                }

                is SignupScreenEvent.TermsAgreementChanged -> {
                    updateState { copy(isTermsAgreed = event.isAgreed) }
                }

                // 선택 시트 표시는 Route가 직접 관리한다.
                SignupScreenEvent.DepartmentFieldClicked,
                SignupScreenEvent.GenerationFieldClicked,
                -> {
                    Unit
                }
            }
        }

        /** 학과 선택 시트에서 고른 학과를 반영한다. */
        fun selectDepartment(department: SignupDepartment) {
            updateState { copy(department = department) }
        }

        /** 기수 선택 시트에서 고른 기수를 반영하고 기수 미선택 오류를 내린다. */
        fun selectGeneration(generation: Int) {
            updateState { copy(generation = generation, isGenerationErrorVisible = false) }
        }

        /** 기수를 고르지 않고 시트를 닫으면 피그마 03-10처럼 기수 미선택 오류를 표시한다. */
        fun dismissGenerationSelection() {
            updateState { copy(isGenerationErrorVisible = generation == null) }
        }

        private fun handlePrimaryAction() {
            val state = _uiState.value
            when (state.step) {
                SignupStep.Email -> {
                    when {
                        !state.isCodeSent -> requestVerificationCode()
                        state.isEmailVerified -> updateState { copy(step = SignupStep.Password) }
                        state.isCodeLocked -> requestVerificationCode()
                        else -> confirmVerificationCode()
                    }
                }

                SignupStep.Password -> {
                    if (state.canSubmitPassword) {
                        updateState { copy(step = SignupStep.Profile) }
                    }
                }

                SignupStep.Profile -> {
                    submitSignup()
                }
            }
        }

        private fun navigateBack() {
            when (_uiState.value.step) {
                SignupStep.Email -> {
                    // 회원가입을 벗어나면 다음 진입 때 처음부터 시작하도록 상태와 타이머를 정리한다.
                    resetSignup()
                    _effect.trySend(SignupEffect.NavigateBack)
                }

                SignupStep.Password -> {
                    updateState { copy(step = SignupStep.Email) }
                }

                SignupStep.Profile -> {
                    updateState { copy(step = SignupStep.Password, requestError = null) }
                }
            }
        }

        private fun updateEmail(email: String) {
            // 인증번호를 보낸 뒤에는 "변경"을 눌러야만 이메일을 고칠 수 있다.
            if (_uiState.value.isCodeSent) return

            updateState { copy(email = email.trim(), requestError = null) }
        }

        private fun returnToEmailInput() {
            codeTimerJob?.cancel()
            updateState {
                copy(
                    isCodeSent = false,
                    isEmailVerified = false,
                    code = "",
                    codeError = null,
                    failedCodeAttemptCount = 0,
                    remainingSeconds = 0,
                    requestError = null,
                )
            }
        }

        private fun updateCode(value: String) {
            val state = _uiState.value
            if (state.isEmailVerified || state.isCodeLocked) return

            val numericCode = value.filter { character -> character in '0'..'9' }.take(SIGNUP_VERIFICATION_CODE_LENGTH)
            updateState {
                copy(
                    code = numericCode,
                    // 다시 입력하기 시작하면 직전 불일치 오류를 내린다. 남은 시도 횟수는 유지한다.
                    codeError = null,
                    requestError = null,
                )
            }
        }

        /** 학교 이메일로 인증번호를 (재)발송한다. 409 응답은 이미 가입된 이메일로 안내한다. */
        private fun requestVerificationCode() {
            val state = _uiState.value
            if (!state.email.isValidSchoolEmail() || state.isLoading) return

            viewModelScope.launch {
                updateState { copy(isLoading = true, requestError = null) }
                when (val result = sendSignupVerificationUseCase(state.email)) {
                    is AppResult.Success -> {
                        updateState {
                            copy(
                                isLoading = false,
                                isCodeSent = true,
                                isEmailVerified = false,
                                code = "",
                                codeError = null,
                                failedCodeAttemptCount = 0,
                            )
                        }
                        startCodeTimer()
                    }

                    is AppResult.HttpError -> {
                        if (result.statusCode == HTTP_CONFLICT) {
                            updateState { copy(isLoading = false) }
                            _effect.send(SignupEffect.ShowAlreadyRegisteredDialog)
                        } else {
                            updateState { copy(isLoading = false, requestError = result.toRequestError()) }
                        }
                    }

                    else -> {
                        updateState { copy(isLoading = false, requestError = result.toRequestError()) }
                    }
                }
            }
        }

        /** 입력한 6자리 인증번호를 확인한다. 실패하면 앱에서 센 시도 횟수를 올리고, 5회째에는 재발송만 허용한다. */
        private fun confirmVerificationCode() {
            val state = _uiState.value
            if (!state.canConfirmCode) return

            viewModelScope.launch {
                updateState { copy(isLoading = true, requestError = null) }
                when (val result = confirmSignupVerificationUseCase(state.email, state.code)) {
                    is AppResult.Success -> {
                        codeTimerJob?.cancel()
                        updateState { copy(isLoading = false, isEmailVerified = true, codeError = null) }
                    }

                    is AppResult.HttpError -> {
                        val failedCount = state.failedCodeAttemptCount + 1
                        updateState {
                            copy(
                                isLoading = false,
                                failedCodeAttemptCount = failedCount,
                                codeError =
                                    if (failedCount >= SIGNUP_MAX_CODE_ATTEMPT_COUNT) {
                                        SignupCodeError.AttemptsExceeded
                                    } else {
                                        SignupCodeError.Mismatch(failedAttemptCount = failedCount)
                                    },
                            )
                        }
                        if (failedCount >= SIGNUP_MAX_CODE_ATTEMPT_COUNT) {
                            codeTimerJob?.cancel()
                        }
                    }

                    else -> {
                        updateState { copy(isLoading = false, requestError = result.toRequestError()) }
                    }
                }
            }
        }

        /** 인증번호 발송 시점부터 5분을 센다. 0초가 되면 입력을 비우고 만료 상태로 바꾼다. */
        private fun startCodeTimer() {
            codeTimerJob?.cancel()
            updateState { copy(remainingSeconds = SIGNUP_CODE_VALID_SECONDS) }
            codeTimerJob =
                viewModelScope.launch {
                    while (_uiState.value.remainingSeconds > 0) {
                        delay(ONE_SECOND_MILLIS)
                        updateState { copy(remainingSeconds = (remainingSeconds - 1).coerceAtLeast(0)) }
                    }
                    updateState {
                        if (isEmailVerified) {
                            this
                        } else {
                            copy(code = "", codeError = SignupCodeError.Expired)
                        }
                    }
                }
        }

        private fun submitSignup() {
            val state = _uiState.value
            val department = state.department
            val generation = state.generation
            if (!state.canCompleteSignup || department == null || generation == null) return

            viewModelScope.launch {
                updateState { copy(isLoading = true, requestError = null) }
                val request =
                    SignUpRequest(
                        email = state.email,
                        username = state.name.trim(),
                        password = state.password,
                        passwordConfirm = state.passwordConfirm,
                        verificationCode = state.code,
                        major = department.apiCode,
                        cohort = generation,
                    )
                when (val result = signUpUseCase(request)) {
                    is AppResult.Success -> {
                        val email = state.email
                        resetSignup()
                        _effect.send(
                            SignupEffect.Completed(
                                email = email,
                                message = result.value.message.orEmpty(),
                            ),
                        )
                    }

                    else -> {
                        updateState { copy(isLoading = false, requestError = result.toRequestError()) }
                    }
                }
            }
        }

        private fun resetSignup() {
            codeTimerJob?.cancel()
            _uiState.value = SignupUiState()
        }

        private fun updateState(transform: SignupUiState.() -> SignupUiState) {
            _uiState.value = _uiState.value.transform()
        }
    }

private fun AppResult<*>.toRequestError(): SignupRequestError =
    when (this) {
        AppResult.NetworkError -> SignupRequestError.Network
        is AppResult.HttpError -> SignupRequestError.Server(message.orEmpty())
        else -> SignupRequestError.Server(message = "")
    }

private const val HTTP_CONFLICT = 409
private const val MAX_NAME_LENGTH = 50
private const val ONE_SECOND_MILLIS = 1_000L
