package com.example.hopes.core.network

import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response

/** Retrofit 응답을 공통 결과로 정규화한다. 취소는 호출자에게 그대로 전파한다. */
class ApiExecutor @Inject constructor(
    private val json: Json,
) {
    suspend fun <T> execute(request: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = request()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body)
            } else {
                val rawErrorBody = response.errorBody()?.string()
                NetworkResult.HttpError(response.code(), rawErrorBody.toErrorMessage())
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: SerializationException) {
            NetworkResult.SerializationError(exception)
        } catch (exception: IOException) {
            NetworkResult.NetworkError(exception)
        }
    }

    /** 서버 오류 응답 본문(`{"message": "..."}`)에서 사용자에게 보여줄 메시지만 뽑아낸다. */
    private fun String?.toErrorMessage(): String? {
        if (this.isNullOrBlank()) return null

        return try {
            json.decodeFromString<ErrorEnvelopeDto>(this).message
        } catch (exception: SerializationException) {
            null
        }
    }
}

@kotlinx.serialization.Serializable
private data class ErrorEnvelopeDto(val message: String? = null)
