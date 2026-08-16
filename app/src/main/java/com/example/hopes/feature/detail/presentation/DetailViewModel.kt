package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetSettingsUseCase
import com.example.hopes.domain.usecase.SubmitInquiryUseCase
import com.example.hopes.domain.usecase.UpdateCustomPromptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 개인 설정과 문의하기의 서버 요청 및 화면 상태를 관리한다. */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateCustomPromptUseCase: UpdateCustomPromptUseCase,
    private val submitInquiryUseCase: SubmitInquiryUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    /** 개인 설정 화면 진입 시 서버의 현재 사용자 프롬프트를 불러온다. */
    fun loadPersonalPrompt() {
        if (_uiState.value.isPromptLoading) {
            return
        }

        viewModelScope.launch {
            updateState {
                copy(
                    isPromptLoading = true,
                    isPromptLoadError = false,
                    isPromptSaveError = false,
                )
            }

            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> {
                    updateState {
                        copy(
                            personalPrompt = result.value.customPrompt,
                            isPromptLoading = false,
                            isPromptLoadError = false,
                            isPromptSaved = false,
                        )
                    }
                }

                else -> {
                    updateState {
                        copy(
                            isPromptLoading = false,
                            isPromptLoadError = true,
                        )
                    }
                }
            }
        }
    }

    /** 프롬프트 입력 변경 시 이전 저장·오류 상태를 초기화한다. */
    fun updatePersonalPrompt(value: String) {
        updateState {
            copy(
                personalPrompt = value,
                isPromptLoadError = false,
                isPromptSaved = false,
                isPromptSaveError = false,
            )
        }
    }

    /** 저장 버튼에서 호출되어 프롬프트를 서버에 반영하고 결과를 화면 상태로 갱신한다. */
    fun savePersonalPrompt() {
        if (_uiState.value.isPromptSaving || _uiState.value.isPromptLoading) {
            return
        }

        val customPrompt = _uiState.value.personalPrompt.trim().ifEmpty { null }

        viewModelScope.launch {
            updateState {
                copy(
                    isPromptSaving = true,
                    isPromptLoadError = false,
                    isPromptSaved = false,
                    isPromptSaveError = false,
                )
            }

            when (val result = updateCustomPromptUseCase(customPrompt)) {
                is AppResult.Success -> {
                    updateState {
                        copy(
                            personalPrompt = result.value.customPrompt,
                            isPromptSaving = false,
                            isPromptSaved = true,
                        )
                    }
                }

                else -> {
                    updateState {
                        copy(
                            isPromptSaving = false,
                            isPromptSaveError = true,
                        )
                    }
                }
            }
        }
    }

    /** 문의 이메일 입력 변경 시 이전 전송·오류 상태를 초기화한다. */
    fun updateContactEmail(value: String) {
        updateState {
            copy(
                contactEmail = value,
                isContactSent = false,
                isContactSubmitError = false,
            )
        }
    }

    /** 문의 내용 입력 변경 시 이전 전송·오류 상태를 초기화한다. */
    fun updateContactMessage(value: String) {
        updateState {
            copy(
                contactMessage = value,
                isContactSent = false,
                isContactSubmitError = false,
            )
        }
    }

    /** 문의 전송 버튼에서 호출되어 내용이 있을 때만 서버에 문의를 전송한다. */
    fun submitContact() {
        if (_uiState.value.isContactSubmitting) {
            return
        }

        val contactContent = _uiState.value.contactMessage.trim()
        if (_uiState.value.contactEmail.isBlank() || contactContent.isEmpty()) {
            updateState {
                copy(
                    isContactSent = false,
                    isContactSubmitError = true,
                )
            }
            return
        }

        viewModelScope.launch {
            updateState {
                copy(
                    isContactSubmitting = true,
                    isContactSent = false,
                    isContactSubmitError = false,
                )
            }

            when (submitInquiryUseCase(contactContent)) {
                is AppResult.Success -> {
                    updateState {
                        copy(
                            contactMessage = "",
                            isContactSubmitting = false,
                            isContactSent = true,
                        )
                    }
                }

                else -> {
                    updateState {
                        copy(
                            isContactSubmitting = false,
                            isContactSubmitError = true,
                        )
                    }
                }
            }
        }
    }

    private fun updateState(transform: DetailUiState.() -> DetailUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
