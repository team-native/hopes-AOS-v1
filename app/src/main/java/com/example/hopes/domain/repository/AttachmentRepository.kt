package com.example.hopes.domain.repository

import com.example.hopes.domain.model.UploadedAttachment
import com.example.hopes.domain.result.AppResult

interface AttachmentRepository {
    suspend fun upload(
        uri: String,
        fileName: String,
        mimeType: String,
        clientRequestId: String,
        onProgress: (Float) -> Unit,
    ): AppResult<UploadedAttachment>

    suspend fun delete(attachmentId: Long): AppResult<Unit>
}
