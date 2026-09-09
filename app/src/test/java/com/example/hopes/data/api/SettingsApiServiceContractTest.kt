package com.example.hopes.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.atomic.AtomicReference
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit

class SettingsApiServiceContractTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `회원탈퇴 endpoint는 DELETE body로 비밀번호를 전송한다`() = runBlocking {
        val capturedRequest = AtomicReference<Request>()
        val apiService = createSettingsApiService(capturedRequest)

        val response = apiService.deleteAccount(DeleteAccountRequestDto(password = "wrong-password"))
        val request = capturedRequest.get()

        assertTrue(response.isSuccessful)
        assertEquals(204, response.code())
        assertEquals("DELETE", request.method)
        assertEquals("/api/account", request.url.encodedPath)
        assertEquals("{\"password\":\"wrong-password\"}", request.bodyAsString())
    }

    /** 실제 네트워크 대신 요청을 캡처하고 204 응답을 반환하는 SettingsApiService를 생성한다. */
    private fun createSettingsApiService(capturedRequest: AtomicReference<Request>): SettingsApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                capturedRequest.set(request)

                Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(204)
                    .message("No Content")
                    .body(ByteArray(0).toResponseBody(null))
                    .build()
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com/")
            .client(client)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType()),
            )
            .build()

        return retrofit.create(SettingsApiService::class.java)
    }

    /** 캡처한 HTTP 요청 Body를 UTF-8 JSON 문자열로 변환한다. */
    private fun Request.bodyAsString(): String {
        val requestBody = requireNotNull(body)
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()
    }
}
