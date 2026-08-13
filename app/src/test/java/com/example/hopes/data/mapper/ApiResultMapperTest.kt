package com.example.hopes.data.mapper

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiResultMapperTest {

    @Test
    fun `401 HTTP error maps to unauthorized app error`() {
        val result = NetworkResult.HttpError(
            statusCode = 401,
            message = null,
        ).toAppResult { value: Unit ->
            value
        }

        assertEquals(
            AppResult.Failure(AppError.Unauthorized),
            result,
        )
    }

    @Test
    fun `service unavailable status maps to retryable error`() {
        val result = NetworkResult.HttpError(
            statusCode = 503,
            message = null,
        ).toAppResult { value: Unit ->
            value
        }

        assertEquals(
            AppResult.Failure(AppError.ServiceUnavailable),
            result,
        )
    }
}
