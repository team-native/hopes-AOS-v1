package com.example.hopes.domain.result

/** Data 계층의 기술 오류를 UI와 분리해 표현하는 도메인 결과다. */
sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>
    data class HttpError(val statusCode: Int, val message: String? = null) : AppResult<Nothing>
    data object NetworkError : AppResult<Nothing>
    data object SerializationError : AppResult<Nothing>
}

/** 실패 결과를 사용자에게 보여줄 문구로 변환한다. 서버가 보낸 메시지가 있으면 그대로 쓰고, 없으면 fallback을 쓴다. */
fun AppResult<*>.toDisplayMessage(fallback: String): String {
    return when (this) {
        is AppResult.Success -> fallback
        is AppResult.HttpError -> message ?: fallback
        AppResult.NetworkError, AppResult.SerializationError -> fallback
    }
}
