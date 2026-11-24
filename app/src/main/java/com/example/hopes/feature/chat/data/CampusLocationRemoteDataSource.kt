package com.example.hopes.feature.chat.data

import com.example.hopes.core.network.ApiExecutor
import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.CampusApiService
import com.example.hopes.data.api.LocationCheckRequestDto
import com.example.hopes.data.api.LocationCheckResponseDto
import javax.inject.Inject

interface CampusLocationRemoteDataSource {
    suspend fun check(request: LocationCheckRequestDto): NetworkResult<LocationCheckResponseDto>
}

class CampusLocationRemoteDataSourceImpl @Inject constructor(
    private val apiExecutor: ApiExecutor,
    private val api: CampusApiService,
) : CampusLocationRemoteDataSource {
    override suspend fun check(request: LocationCheckRequestDto): NetworkResult<LocationCheckResponseDto> =
        apiExecutor.execute { api.checkLocation(request) }
}
