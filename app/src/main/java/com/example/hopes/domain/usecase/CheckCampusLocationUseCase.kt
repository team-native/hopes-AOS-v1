package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.CampusLocationCheckRequest
import com.example.hopes.domain.model.CampusLocationCheckResult
import com.example.hopes.domain.repository.CampusLocationRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class CheckCampusLocationUseCase @Inject constructor(private val repository: CampusLocationRepository) {
    suspend operator fun invoke(request: CampusLocationCheckRequest): AppResult<CampusLocationCheckResult> = repository.checkLocation(request)
}
