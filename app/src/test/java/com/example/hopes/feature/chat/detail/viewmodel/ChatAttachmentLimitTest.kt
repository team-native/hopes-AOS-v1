package com.example.hopes.feature.chat.detail.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatAttachmentLimitTest {
    private fun image(
        index: Int,
        status: ChatAttachmentStatus = ChatAttachmentStatus.Ready,
    ) = ChatAttachmentUiModel(
        uri = "content://image/$index",
        kind = ChatAttachmentKind.Image,
        name = "$index.jpg",
        status = status,
    )

    @Test
    fun attachmentsBeyondLimit_areMarkedOverLimit() {
        val limited = List(MAX_ATTACHMENT_COUNT + 1) { image(it) }.limitAttachmentCount()

        assertEquals(List(MAX_ATTACHMENT_COUNT) { ChatAttachmentStatus.Ready }, limited.dropLast(1).map { it.status })
        assertEquals(ChatAttachmentStatus.OverLimit, limited.last().status)
    }

    @Test
    fun removingOne_letsOverLimitAttachmentBackIn() {
        val limited = List(MAX_ATTACHMENT_COUNT + 1) { image(it) }.limitAttachmentCount()

        val afterRemove = limited.drop(1).limitAttachmentCount()

        assertTrue(afterRemove.all { it.status == ChatAttachmentStatus.Ready })
    }

    @Test
    fun rejectedForOtherReasons_keepStatusAndDoNotCount() {
        val attachments =
            listOf(image(0, ChatAttachmentStatus.TooLarge)) + List(MAX_ATTACHMENT_COUNT) { image(it + 1) }

        val limited = attachments.limitAttachmentCount()

        assertEquals(ChatAttachmentStatus.TooLarge, limited.first().status)
        assertTrue(limited.drop(1).all { it.status == ChatAttachmentStatus.Ready })
    }

    @Test
    fun uploadingOrRejectedAttachment_blocksSending() {
        assertFalse(
            ChatDetailUiState(
                draftAttachments = listOf(image(0, ChatAttachmentStatus.Uploading(0.5f))),
            ).canSendDraftAttachments,
        )
        assertFalse(
            ChatDetailUiState(
                draftAttachments = listOf(image(0, ChatAttachmentStatus.Unreadable)),
            ).canSendDraftAttachments,
        )
        assertTrue(
            ChatDetailUiState(
                draftAttachments = listOf(image(0, ChatAttachmentStatus.UploadFailed)),
            ).canSendDraftAttachments,
        )
    }
}
