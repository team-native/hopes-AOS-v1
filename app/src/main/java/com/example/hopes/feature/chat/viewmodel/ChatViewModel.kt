package com.example.hopes.feature.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentStatus
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.example.hopes.feature.chat.detail.viewmodel.limitAttachmentCount
import com.example.hopes.domain.model.ChatAssistCategory
import com.example.hopes.domain.model.ChatPreferenceUpdate
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetChatDepartmentsUseCase
import com.example.hopes.domain.usecase.GetChatPreferencesUseCase
import com.example.hopes.domain.usecase.GetChatSpecialtiesUseCase
import com.example.hopes.domain.usecase.GetRecommendationConsentUseCase
import com.example.hopes.domain.usecase.GetRecommendationsUseCase
import com.example.hopes.domain.usecase.UpdateChatPreferencesUseCase
import com.example.hopes.domain.usecase.UpdateRecommendationConsentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ChatEffect {
    /** 대화 생성은 상세 화면 자체에서 진행하므로, 질문과 첨부만 들고 곧바로 상세 화면으로 이동한다. */
    data class NavigateToNewChat(
        val question: String,
        val attachments: List<ChatAttachmentUiModel>,
        val department: ChatDepartment?,
        val major: ChatMajor?,
        val departmentId: Long?,
        val specialtyId: Long?,
    ) : ChatEffect
}

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val getChatDepartments: GetChatDepartmentsUseCase,
        private val getChatSpecialties: GetChatSpecialtiesUseCase,
        private val getChatPreferences: GetChatPreferencesUseCase,
        private val updateChatPreferences: UpdateChatPreferencesUseCase,
        private val getRecommendationConsent: GetRecommendationConsentUseCase,
        private val updateRecommendationConsent: UpdateRecommendationConsentUseCase,
        private val getRecommendations: GetRecommendationsUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ChatUiState())
        val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<ChatEffect>()
        val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

        private var departmentCategories: List<ChatAssistCategory> = emptyList()
        private var specialtiesByDepartment: Map<String, List<ChatAssistCategory>> = emptyMap()
        private var lastRequestToken: String? = null
        private var isServerContextLoaded = false

        fun initialize() {
            if (isServerContextLoaded) return
            isServerContextLoaded = true
            updateState { copy(availableDepartments = emptyList(), availableMajors = emptyList()) }
            loadServerContext()
        }

        /** 질문 입력·제출 이벤트를 처리한다. */
        fun onEvent(event: ChatScreenEvent) {
            when (event) {
                is ChatScreenEvent.QuestionChanged -> {
                    updateState {
                        copy(questionText = event.question)
                    }
                }

                ChatScreenEvent.QuestionSubmitted -> {
                    navigateToNewChat(_uiState.value.questionText)
                }

                is ChatScreenEvent.SuggestionClicked -> {
                    updateState { copy(questionText = event.question) }
                }

                ChatScreenEvent.NewChatClicked -> {
                    updateState { copy(questionText = "", draftAttachments = emptyList()) }
                }

                is ChatScreenEvent.AttachmentsAdded -> {
                    updateState {
                        val existingUris = draftAttachments.map { it.uri }.toSet()
                        val newAttachments = event.attachments.filter { it.uri !in existingUris }
                        copy(draftAttachments = (draftAttachments + newAttachments).limitAttachmentCount())
                    }
                }

                is ChatScreenEvent.AttachmentRemoved -> {
                    // 하나를 빼면 개수 제한에 걸렸던 첨부가 다시 들어갈 수 있다.
                    updateState {
                        val remaining = draftAttachments.filterNot { it.uri == event.uri }
                        copy(draftAttachments = remaining.limitAttachmentCount())
                    }
                }

                is ChatScreenEvent.DepartmentSelected -> {
                    val id = departmentCategories.firstOrNull { it.isActive && it.name.toDepartment() == event.department }?.id
                    val availableMajors = event.department?.let(::specialtiesFor).orEmpty().filter { it.isActive }.mapNotNull { it.name.toMajor() }.distinct()
                    updateState { copy(department = event.department, departmentId = id, major = null, specialtyId = null, availableMajors = availableMajors) }
                    persistPreferencesAndRefresh()
                }

                is ChatScreenEvent.MajorSelected -> {
                    val categories = stateSpecialties()
                    val id = categories.firstOrNull { it.isActive && it.name.toMajor() == event.major }?.id
                    updateState { copy(major = event.major, specialtyId = id) }
                    persistPreferencesAndRefresh()
                }

                ChatScreenEvent.RecommendationsRefreshClicked -> {
                    loadRecommendations(refresh = true)
                }

                ChatScreenEvent.RecommendationConsentClicked -> enableRecommendationConsent()
                ChatScreenEvent.RecommendationConsentWithdrawClicked -> updateRecommendationConsent(enabled = false)
            }
        }

        private fun loadServerContext() {
            viewModelScope.launch {
                val departments = getChatDepartments()
                if (departments is AppResult.Success) {
                    departmentCategories = departments.value.values.flatten().sortedBy { it.sortOrder }
                    val selectableDepartments = departmentCategories.filter { it.isActive }.mapNotNull { it.name.toDepartment() }.distinct()
                    updateState { copy(availableDepartments = selectableDepartments) }
                }
                val specialties = getChatSpecialties()
                if (specialties is AppResult.Success) specialtiesByDepartment = specialties.value
                val consentResult = getRecommendationConsent()
                val consent = (consentResult as? AppResult.Success)?.value
                updateState { copy(recommendationConsentEnabled = consent?.enabled == true, recommendationPolicyVersion = consent?.policyVersion.orEmpty()) }
                val preferenceResult = getChatPreferences()
                if (preferenceResult is AppResult.Success) {
                    val preferences = preferenceResult.value
                    val department = departmentCategories.firstOrNull { it.id == preferences.departmentId && it.isActive }
                    val selectedDepartment = department?.name?.toDepartment()
                    val specialtyOptions = selectedDepartment?.let { specialtiesFor(it) }.orEmpty()
                    val specialty = specialtyOptions.firstOrNull { it.id == preferences.specialtyId && it.isActive }
                    updateState {
                        copy(
                            department = selectedDepartment,
                            departmentId = department?.id,
                            major = specialty?.name?.toMajor(),
                            specialtyId = specialty?.id,
                            availableMajors = specialtyOptions.filter { it.isActive }.mapNotNull { it.name.toMajor() }.distinct(),
                        )
                    }
                }
                loadRecommendations(refresh = false)
            }
        }

        private fun persistPreferencesAndRefresh() {
            viewModelScope.launch {
                val current = _uiState.value
                updateChatPreferences(
                    ChatPreferenceUpdate(
                        departmentId = current.departmentId,
                        specialtyId = current.specialtyId,
                        specialtyIds = current.specialtyId?.let(::listOf).orEmpty(),
                    ),
                )
                loadRecommendations(refresh = false)
            }
        }

        private fun loadRecommendations(refresh: Boolean) {
            viewModelScope.launch {
                val state = _uiState.value
                if (!state.recommendationConsentEnabled) {
                    updateState { copy(recommendedQuestions = emptyList(), recommendationState = ChatRecommendationState.Loaded) }
                    return@launch
                }
                updateState { copy(recommendationState = ChatRecommendationState.Loading) }
                val result = getRecommendations(
                    state.departmentId,
                    state.specialtyId,
                    if (refresh) lastRequestToken else null,
                    if (refresh) state.recommendedQuestions.map { it.id } else emptyList(),
                )
                if (result is AppResult.Success) {
                    lastRequestToken = result.value.nextRefreshToken
                    updateState {
                        copy(
                            recommendedQuestions = result.value.items.map { ChatQuestionRecommendation(it.id, it.content) },
                            recommendationRefreshToken = result.value.nextRefreshToken,
                            excludedRecommendationIds = result.value.items.map { it.id },
                            recommendationState = ChatRecommendationState.Loaded,
                        )
                    }
                } else {
                    updateState { copy(recommendationState = ChatRecommendationState.LoadError) }
                }
            }
        }

        private fun enableRecommendationConsent() {
            updateRecommendationConsent(enabled = true)
        }

        private fun updateRecommendationConsent(enabled: Boolean) {
            val policyVersion = _uiState.value.recommendationPolicyVersion
            if (policyVersion.isBlank()) return
            viewModelScope.launch {
                val result = updateRecommendationConsent(enabled, policyVersion)
                if (result is AppResult.Success) {
                    updateState { copy(recommendationConsentEnabled = result.value.enabled, recommendationPolicyVersion = result.value.policyVersion) }
                    if (enabled) loadRecommendations(refresh = false) else updateState { copy(recommendedQuestions = emptyList()) }
                }
            }
        }

        private fun stateSpecialties(): List<ChatAssistCategory> = _uiState.value.department?.let(::specialtiesFor).orEmpty()

        private fun specialtiesFor(department: ChatDepartment): List<ChatAssistCategory> =
            specialtiesByDepartment.entries
                .firstOrNull { (departmentName, _) -> departmentName.toDepartment() == department }
                ?.value
                .orEmpty()
                .filter { it.name.toMajor() != null }

        private fun String.toDepartment(): ChatDepartment? = when {
            contains("소프트웨어", true) || contains("software", true) -> ChatDepartment.Software
            contains("스마트", true) || contains("smart", true) -> ChatDepartment.SmartIot
            contains("인공지능", true) || contains("artificial intelligence", true) -> ChatDepartment.ArtificialIntelligence
            else -> null
        }

        private fun String.toMajor(): ChatMajor? = ChatMajor.entries.firstOrNull { major ->
            major == ChatMajor.Other && (equals("기타", true) || equals("other", true)) || major.displayName?.equals(this, true) == true
        }

        /** 질문을 곧바로 상세 화면으로 넘겨, 대화 생성·첫 메시지 전송을 상세 화면에서 진행하게 한다. */
        private fun navigateToNewChat(question: String) {
            val trimmedQuestion = question.trim()
            val state = _uiState.value
            if (trimmedQuestion.isEmpty() || !state.canSendDraftAttachments) {
                return
            }

            viewModelScope.launch {
                updateState { copy(questionText = "", draftAttachments = emptyList()) }
                val currentState = _uiState.value
                _effect.emit(
                    ChatEffect.NavigateToNewChat(
                        question = trimmedQuestion,
                        attachments = state.draftAttachments.filter { it.status == ChatAttachmentStatus.Ready },
                        department = currentState.department,
                        major = currentState.major,
                        departmentId = currentState.departmentId,
                        specialtyId = currentState.specialtyId,
                    ),
                )
            }
        }

        private fun updateState(transform: ChatUiState.() -> ChatUiState) {
            _uiState.value = _uiState.value.transform()
        }
    }
