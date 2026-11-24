package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UploadedAttachment
import com.example.hopes.domain.repository.AttachmentRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class UploadChatAttachmentUseCase
    @Inject
    constructor(
        private val attachmentRepository: AttachmentRepository,
    ) {
        /** 선택된 파일을 채팅 전송 전에 서버 첨부 리소스로 등록한다. */
        suspend operator fun invoke(
            uri: String,
            fileName: String,
            mimeType: String,
            clientRequestId: String,
            onProgress: (Float) -> Unit,
        ): AppResult<UploadedAttachment> =
            attachmentRepository.upload(uri, fileName, mimeType, clientRequestId, onProgress)
    }
