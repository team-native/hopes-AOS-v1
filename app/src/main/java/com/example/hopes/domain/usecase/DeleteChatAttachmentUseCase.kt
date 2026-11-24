package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.AttachmentRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class DeleteChatAttachmentUseCase
    @Inject
    constructor(private val attachmentRepository: AttachmentRepository) {
        suspend operator fun invoke(attachmentId: Long): AppResult<Unit> = attachmentRepository.delete(attachmentId)
    }
