package com.example.hopes.feature.chat.viewmodel

import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentStatus
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.example.hopes.feature.chat.detail.viewmodel.MAX_ATTACHMENT_COUNT
import com.example.hopes.feature.chat.detail.viewmodel.toArg
import com.example.hopes.feature.chat.detail.viewmodel.toUiModel
import com.example.hopes.domain.model.ChatAssistCategory
import com.example.hopes.domain.model.ChatPreference
import com.example.hopes.domain.model.ChatPreferenceUpdate
import com.example.hopes.domain.model.RecommendationConsent
import com.example.hopes.domain.model.RecommendationPage
import com.example.hopes.domain.repository.ChatAssistRepository
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.GetChatDepartmentsUseCase
import com.example.hopes.domain.usecase.GetChatPreferencesUseCase
import com.example.hopes.domain.usecase.GetChatSpecialtiesUseCase
import com.example.hopes.domain.usecase.GetRecommendationConsentUseCase
import com.example.hopes.domain.usecase.GetRecommendationsUseCase
import com.example.hopes.domain.usecase.UpdateChatPreferencesUseCase
import com.example.hopes.domain.usecase.UpdateRecommendationConsentUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatHomeAttachmentTest {
    private fun image(index: Int) =
        ChatAttachmentUiModel(uri = "content://image/$index", kind = ChatAttachmentKind.Image, name = "사진$index")

    @Test
    fun attachmentsAdded_skipsDuplicateUris() {
        val viewModel = createViewModel()

        viewModel.onEvent(ChatScreenEvent.AttachmentsAdded(listOf(image(1))))
        viewModel.onEvent(ChatScreenEvent.AttachmentsAdded(listOf(image(1), image(2))))

        assertEquals(
            listOf("content://image/1", "content://image/2"),
            viewModel.uiState.value.draftAttachments
                .map { it.uri },
        )
    }

    @Test
    fun overLimitAttachment_blocksSend_untilOneIsRemoved() {
        val viewModel = createViewModel()

        viewModel.onEvent(ChatScreenEvent.AttachmentsAdded(List(MAX_ATTACHMENT_COUNT + 1) { image(it) }))
        assertFalse(viewModel.uiState.value.canSendDraftAttachments)
        assertEquals(
            ChatAttachmentStatus.OverLimit,
            viewModel.uiState.value.draftAttachments
                .last()
                .status,
        )

        viewModel.onEvent(ChatScreenEvent.AttachmentRemoved("content://image/0"))
        assertTrue(viewModel.uiState.value.canSendDraftAttachments)
    }

    @Test
    fun newChatClicked_clearsAttachments() {
        val viewModel = createViewModel()
        viewModel.onEvent(ChatScreenEvent.AttachmentsAdded(listOf(image(1))))

        viewModel.onEvent(ChatScreenEvent.NewChatClicked)

        assertTrue(
            viewModel.uiState.value.draftAttachments
                .isEmpty(),
        )
    }

    @Test
    fun attachmentArg_roundTripsToReadyAttachment() {
        val pdf =
            ChatAttachmentUiModel(
                uri = "content://pdf/1",
                kind = ChatAttachmentKind.Pdf,
                name = "수칙.pdf",
                pageCount = 4,
            )

        assertEquals(pdf, pdf.toArg().toUiModel())
    }

    private fun createViewModel() =
        ChatViewModel(
            GetChatDepartmentsUseCase(FakeChatAssistRepository),
            GetChatSpecialtiesUseCase(FakeChatAssistRepository),
            GetChatPreferencesUseCase(FakeChatAssistRepository),
            UpdateChatPreferencesUseCase(FakeChatAssistRepository),
            GetRecommendationConsentUseCase(FakeChatAssistRepository),
            UpdateRecommendationConsentUseCase(FakeChatAssistRepository),
            GetRecommendationsUseCase(FakeChatAssistRepository),
        )

    private object FakeChatAssistRepository : ChatAssistRepository {
        private fun <T> failure(): AppResult<T> = AppResult.NetworkError
        override suspend fun getDepartments() = failure<Map<String, List<ChatAssistCategory>>>()
        override suspend fun getSpecialties() = failure<Map<String, List<ChatAssistCategory>>>()
        override suspend fun getPreferences() = failure<ChatPreference>()
        override suspend fun updatePreferences(update: ChatPreferenceUpdate) = failure<ChatPreference>()
        override suspend fun getRecommendationConsent() = failure<RecommendationConsent>()
        override suspend fun updateRecommendationConsent(enabled: Boolean, policyVersion: String) = failure<RecommendationConsent>()
        override suspend fun getRecommendations(departmentId: Long?, specialtyId: Long?, refreshToken: String?, excludeIds: List<Long>) = failure<RecommendationPage>()
    }
}
