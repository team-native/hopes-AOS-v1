package com.example.hopes.feature.chat.detail.view

import android.content.ContentResolver
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel

/**
 * 기기에서 고른 파일 URI를 화면에 보여줄 첨부 정보(이름, PDF 쪽수)로 바꾼다.
 * 이름이나 쪽수를 읽지 못해도 첨부는 그대로 두고 [fallbackName]·쪽수 없음으로 보여준다.
 */
internal fun ContentResolver.toChatAttachment(
    uri: Uri,
    kind: ChatAttachmentKind,
    fallbackName: String,
): ChatAttachmentUiModel =
    ChatAttachmentUiModel(
        uri = uri.toString(),
        kind = kind,
        name = displayName(uri) ?: fallbackName,
        pageCount = if (kind == ChatAttachmentKind.Pdf) pdfPageCount(uri) else null,
    )

private fun ContentResolver.displayName(uri: Uri): String? =
    runCatching {
        query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    }.getOrNull()

private fun ContentResolver.pdfPageCount(uri: Uri): Int? =
    try {
        openFileDescriptor(uri, "r")?.use { descriptor ->
            PdfRenderer(descriptor).use { renderer -> renderer.pageCount }
        }
    } catch (exception: Exception) {
        // 암호가 걸렸거나 깨진 PDF도 첨부는 할 수 있게 쪽수만 비운다.
        Log.w(ATTACHMENT_LOG_TAG, "PDF 쪽수를 읽지 못했습니다.", exception)
        null
    }

private const val ATTACHMENT_LOG_TAG = "ChatAttachment"
