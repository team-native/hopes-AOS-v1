package com.example.hopes.feature.chat.detail.view

import android.content.ContentResolver
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentStatus
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.example.hopes.feature.chat.detail.viewmodel.MAX_ATTACHMENT_SIZE_BYTES

/**
 * 기기에서 고른 파일 URI를 화면에 보여줄 첨부 정보로 바꾸고, 보낼 수 있는 파일인지 검사한다.
 * 이미지·PDF가 아니면 [ChatAttachmentStatus.Unsupported](10-4), 용량을 넘으면 [ChatAttachmentStatus.TooLarge](10-5),
 * 열 수 없는 PDF면 [ChatAttachmentStatus.Unreadable](10-6)로 표시한다.
 */
internal fun ContentResolver.toChatAttachment(
    uri: Uri,
    kind: ChatAttachmentKind,
    fallbackName: String,
): ChatAttachmentUiModel {
    val metadata = readMetadata(uri)
    val mimeType = getType(uri)
    val isSupported =
        when (kind) {
            ChatAttachmentKind.Image -> mimeType == null || mimeType.startsWith(IMAGE_MIME_PREFIX)
            ChatAttachmentKind.Pdf -> mimeType == null || mimeType == PDF_MIME_TYPE
        }
    val pageCount = if (isSupported && kind == ChatAttachmentKind.Pdf) pdfPageCount(uri) else null
    val status =
        when {
            !isSupported -> ChatAttachmentStatus.Unsupported
            (metadata.sizeBytes ?: 0L) > MAX_ATTACHMENT_SIZE_BYTES -> ChatAttachmentStatus.TooLarge
            kind == ChatAttachmentKind.Pdf && pageCount == null -> ChatAttachmentStatus.Unreadable
            else -> ChatAttachmentStatus.Ready
        }

    return ChatAttachmentUiModel(
        uri = uri.toString(),
        kind = kind,
        name = metadata.displayName ?: fallbackName,
        pageCount = pageCount,
        status = status,
    )
}

private data class AttachmentMetadata(
    val displayName: String?,
    val sizeBytes: Long?,
)

private fun ContentResolver.readMetadata(uri: Uri): AttachmentMetadata =
    runCatching {
        query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)?.use { cursor ->
            if (!cursor.moveToFirst()) return@use null
            AttachmentMetadata(
                displayName = cursor.getString(0),
                sizeBytes = if (cursor.isNull(1)) null else cursor.getLong(1),
            )
        }
    }.getOrNull() ?: AttachmentMetadata(displayName = null, sizeBytes = null)

private fun ContentResolver.pdfPageCount(uri: Uri): Int? =
    try {
        openFileDescriptor(uri, "r")?.use { descriptor ->
            PdfRenderer(descriptor).use { renderer -> renderer.pageCount }
        }
    } catch (exception: Exception) {
        // 암호가 걸렸거나 깨진 PDF다. 호출한 쪽이 Unreadable로 표시한다.
        Log.w(ATTACHMENT_LOG_TAG, "PDF를 열지 못했습니다.", exception)
        null
    }

private const val ATTACHMENT_LOG_TAG = "ChatAttachment"
private const val IMAGE_MIME_PREFIX = "image/"
private const val PDF_MIME_TYPE = "application/pdf"
