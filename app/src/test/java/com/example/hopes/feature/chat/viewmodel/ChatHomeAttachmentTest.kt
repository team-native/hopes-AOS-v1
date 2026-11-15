package com.example.hopes.feature.chat.viewmodel

import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentStatus
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.example.hopes.feature.chat.detail.viewmodel.MAX_ATTACHMENT_COUNT
import com.example.hopes.feature.chat.detail.viewmodel.toArg
import com.example.hopes.feature.chat.detail.viewmodel.toUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatHomeAttachmentTest {
    private fun image(index: Int) =
        ChatAttachmentUiModel(uri = "content://image/$index", kind = ChatAttachmentKind.Image, name = "사진$index")

    @Test
    fun attachmentsAdded_skipsDuplicateUris() {
        val viewModel = ChatViewModel()

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
        val viewModel = ChatViewModel()

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
        val viewModel = ChatViewModel()
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
}
