package com.example.hopes.core.network

import java.io.IOException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ApiExecutorTest {
    private val apiExecutor = ApiExecutor(Json { ignoreUnknownKeys = true })

    @Test
    fun `executeNoContent maps 204 response to success`() = runBlocking {
        val result = apiExecutor.executeNoContent {
            Response.success<Unit>(204, null)
        }

        assertEquals(NetworkResult.Success(Unit), result)
    }

    @Test
    fun `executeNoContent maps http error and server message`() = runBlocking {
        val errorBody = """{"message":"현재 비밀번호가 올바르지 않습니다."}"""
            .toResponseBody("application/json".toMediaType())

        val result = apiExecutor.executeNoContent {
            Response.error<Unit>(401, errorBody)
        }

        assertEquals(
            NetworkResult.HttpError(401, "현재 비밀번호가 올바르지 않습니다."),
            result,
        )
    }

    @Test
    fun `executeNoContent maps io failure to network error`() = runBlocking {
        val result = apiExecutor.executeNoContent {
            throw IOException("network unavailable")
        }

        assertTrue(result is NetworkResult.NetworkError)
    }
}
