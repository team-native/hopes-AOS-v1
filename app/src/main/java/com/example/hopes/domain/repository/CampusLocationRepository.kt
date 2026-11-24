package com.example.hopes.domain.repository

import com.example.hopes.domain.model.CampusLocationCheckRequest
import com.example.hopes.domain.model.CampusLocationCheckResult
import com.example.hopes.domain.result.AppResult

interface CampusLocationRepository {
    suspend fun checkLocation(request: CampusLocationCheckRequest): AppResult<CampusLocationCheckResult>
}
