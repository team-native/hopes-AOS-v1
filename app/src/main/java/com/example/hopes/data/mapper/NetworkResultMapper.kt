package com.example.hopes.data.mapper

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.domain.result.AppResult

/** 기술 계층 오류를 도메인 결과로 한 번만 변환한다. */
fun <T, R> NetworkResult<T>.toAppResult(mapper: (T) -> R): AppResult<R> {
    return when (this) {
        is NetworkResult.Success -> AppResult.Success(mapper(value))
        is NetworkResult.HttpError -> AppResult.HttpError(statusCode, message)
        is NetworkResult.NetworkError -> AppResult.NetworkError
        is NetworkResult.SerializationError -> AppResult.SerializationError
    }
}
