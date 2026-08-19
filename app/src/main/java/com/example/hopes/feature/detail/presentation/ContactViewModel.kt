package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetSettingsUseCase
import com.example.hopes.domain.usecase.SubmitInquiryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 문의하기 입력과 전송의 단방향 상태 흐름을 관리한다. */
@HiltViewModel
class ContactViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val submitInquiryUseCase: SubmitInquiryUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUiState())
    val uiState: StateFlow<ContactUiState> = _uiState.asStateFlow()

    init {
        prefillEmail()
    }

    /** 입력 변경과 전송 요청을 처리한다. */
    fun onEvent(event: ContactScreenEvent) {
        when (event) {
            is ContactScreenEvent.ContactEmailChanged -> updateState {
                copy(email = event.value, isSent = false, isSendFailed = false)
            }
            is ContactScreenEvent.ContactMessageChanged -> updateState {
                copy(message = event.value, isSent = false, isSendFailed = false)
            }
            ContactScreenEvent.ContactSendClicked -> sendInquiry()
        }
    }

    /** 학교 이메일을 직접 다시 입력하지 않도록, 저장된 계정 이메일로 미리 채운다. 실패해도 수동 입력은 가능하므로 조용히 무시한다. */
    private fun prefillEmail() {
        viewModelScope.launch {
            val result = getSettingsUseCase()
            if (result is AppResult.Success) {
                updateState { copy(email = result.value.profile.email) }
            }
        }
    }

    /** POST /api/setting/inquiry로 문의 내용을 전송한다. */
    private fun sendInquiry() {
        val currentState = _uiState.value
        if (currentState.isSending || currentState.email.isBlank() || currentState.message.isBlank()) return

        viewModelScope.launch {
            updateState { copy(isSending = true, isSendFailed = false) }
            when (submitInquiryUseCase(currentState.email, currentState.message)) {
                is AppResult.Success -> updateState { copy(isSending = false, isSent = true) }
                else -> updateState { copy(isSending = false, isSendFailed = true) }
            }
        }
    }

    private fun updateState(transform: ContactUiState.() -> ContactUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
