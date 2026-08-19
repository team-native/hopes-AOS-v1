package com.example.hopes.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetSettingsUseCase
import com.example.hopes.domain.usecase.UpdateCustomPromptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 개인 설정(시스템 프롬프트) 조회와 저장의 단방향 상태 흐름을 관리한다. */
@HiltViewModel
class PersonalSettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateCustomPromptUseCase: UpdateCustomPromptUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonalSettingsUiState())
    val uiState: StateFlow<PersonalSettingsUiState> = _uiState.asStateFlow()

    init {
        loadPrompt()
    }

    /** 입력 변경과 저장·재시도 요청을 처리한다. */
    fun onEvent(event: PersonalSettingsScreenEvent) {
        when (event) {
            is PersonalSettingsScreenEvent.PersonalPromptChanged -> updateState {
                copy(personalPrompt = event.value, isPromptSaved = false, isPromptSaveFailed = false)
            }
            PersonalSettingsScreenEvent.PersonalPromptSaveClicked -> savePrompt()
            PersonalSettingsScreenEvent.PersonalPromptRetryClicked -> loadPrompt()
        }
    }

    /** GET /api/setting 결과로 기존에 저장된 프롬프트를 불러온다. */
    private fun loadPrompt() {
        viewModelScope.launch {
            updateState { copy(isPromptLoading = true, isPromptLoadFailed = false) }
            when (val result = getSettingsUseCase()) {
                is AppResult.Success -> updateState {
                    copy(
                        personalPrompt = result.value.customPrompt,
                        isPromptLoading = false,
                        isPromptLoadFailed = false,
                    )
                }
                else -> updateState { copy(isPromptLoading = false, isPromptLoadFailed = true) }
            }
        }
    }

    /** PATCH /api/setting으로 시스템 프롬프트를 저장한다. */
    private fun savePrompt() {
        val currentState = _uiState.value
        if (currentState.isPromptSaving) return

        viewModelScope.launch {
            updateState { copy(isPromptSaving = true, isPromptSaved = false, isPromptSaveFailed = false) }
            when (val result = updateCustomPromptUseCase(currentState.personalPrompt)) {
                is AppResult.Success -> updateState {
                    copy(
                        personalPrompt = result.value.customPrompt,
                        isPromptSaving = false,
                        isPromptSaved = true,
                    )
                }
                else -> updateState { copy(isPromptSaving = false, isPromptSaveFailed = true) }
            }
        }
    }

    private fun updateState(transform: PersonalSettingsUiState.() -> PersonalSettingsUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
