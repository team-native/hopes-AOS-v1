package com.example.hopes.data.mapper

import com.example.hopes.data.api.AttachmentResponseDto
import com.example.hopes.domain.model.UploadedAttachment

fun AttachmentResponseDto.toDomain(): UploadedAttachment =
    UploadedAttachment(
        id = id,
        type = type,
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        status = status,
        previewUrl = previewUrl,
        expiresAt = expiresAt,
    )
