package com.example.hopes.data.mapper

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult

/** 공통 네트워크 결과를 Domain 결과로 한 번만 변환한다. */
fun <Input, Output> NetworkResult<Input>.toAppResult(
    transform: (Input) -> Output,
): AppResult<Output> {
    return when (this) {
        is NetworkResult.Success -> AppResult.Success(transform(value))
        is NetworkResult.HttpError -> AppResult.Failure(statusCode.toAppError())
        is NetworkResult.NetworkError -> AppResult.Failure(AppError.Network)
        is NetworkResult.SerializationError -> AppResult.Failure(AppError.Serialization)
    }
}

/** HTTP 상태 코드를 Presentation에 노출하지 않는 앱 오류로 바꾼다. */
private fun Int.toAppError(): AppError {
    return when (this) {
        400, 409 -> AppError.Validation
        401 -> AppError.Unauthorized
        404 -> AppError.NotFound
        429 -> AppError.RateLimited
        502, 503 -> AppError.ServiceUnavailable
        else -> AppError.Unknown(this)
    }
}
