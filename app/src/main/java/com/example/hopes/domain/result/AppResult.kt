package com.example.hopes.domain.result

/** Data 계층의 기술 오류를 UI와 분리해 표현하는 도메인 결과다. */
sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class HttpError(val statusCode: Int) : AppResult<Nothing>
    data object NetworkError : AppResult<Nothing>
    data object SerializationError : AppResult<Nothing>
}
