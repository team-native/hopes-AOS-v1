package com.example.hopes.data.mapper

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.domain.result.AppResult
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkResultMapperTest {
    @Test
    fun `success maps dto value to domain value`() {
        val result = NetworkResult.Success("42").toAppResult(String::toInt)

        assertEquals(AppResult.Success(42), result)
    }

    @Test
    fun `http error preserves status code without exposing response body`() {
        val result = NetworkResult.HttpError(401, "secret server detail").toAppResult { value: String -> value }

        assertEquals(AppResult.HttpError(401), result)
    }

    @Test
    fun `network failure maps to domain network error`() {
        val result = NetworkResult.NetworkError(IllegalStateException()).toAppResult { value: String -> value }

        assertEquals(AppResult.NetworkError, result)
    }
}
