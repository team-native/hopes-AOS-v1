package com.example.hopes.domain.result

/** Domain과 Presentation이 공유하는 서버 호출 결과다. */
sealed interface AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>

    data class Failure(val error: AppError) : AppResult<Nothing>
}

/** UI가 원시 HTTP·예외 타입 없이 처리할 수 있는 오류다. */
sealed interface AppError {
    data object Unauthorized : AppError

    data object Validation : AppError

    data object NotFound : AppError

    data object RateLimited : AppError

    data object ServiceUnavailable : AppError

    data object Network : AppError

    data object Serialization : AppError

    data class Unknown(val statusCode: Int?) : AppError
}
