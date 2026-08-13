package com.example.hopes.core.network

import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.Response

/** Retrofit 응답을 공통 결과로 정규화한다. 취소는 호출자에게 그대로 전파한다. */
class ApiExecutor @Inject constructor() {
    suspend fun <T> execute(request: suspend () -> Response<T>): NetworkResult<T> {
        return try {
            val response = request()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body)
            } else {
                NetworkResult.HttpError(response.code(), response.errorBody()?.string())
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: SerializationException) {
            NetworkResult.SerializationError(exception)
        } catch (exception: IOException) {
            NetworkResult.NetworkError(exception)
        }
    }
}
