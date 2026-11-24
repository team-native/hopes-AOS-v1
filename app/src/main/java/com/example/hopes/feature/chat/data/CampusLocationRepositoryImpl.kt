package com.example.hopes.feature.chat.data

import com.example.hopes.data.api.LocationCheckRequestDto
import com.example.hopes.data.mapper.toAppResult
import com.example.hopes.domain.model.CampusLocationCheckRequest
import com.example.hopes.domain.model.CampusLocationCheckResult
import com.example.hopes.domain.repository.CampusLocationRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class CampusLocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: CampusLocationRemoteDataSource,
) : CampusLocationRepository {
    override suspend fun checkLocation(request: CampusLocationCheckRequest): AppResult<CampusLocationCheckResult> =
        remoteDataSource.check(
            LocationCheckRequestDto(
                latitude = request.latitude,
                longitude = request.longitude,
                horizontalAccuracyMeters = request.horizontalAccuracyMeters,
                measuredAt = request.measuredAt,
                clientRequestId = request.clientRequestId,
            ),
        ).toAppResult { it.toDomain() }
}
