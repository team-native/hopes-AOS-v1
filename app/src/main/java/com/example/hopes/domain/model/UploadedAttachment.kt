package com.example.hopes.domain.model

data class UploadedAttachment(
    val id: Long,
    val type: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val status: String,
    val previewUrl: String?,
    val expiresAt: String,
)
