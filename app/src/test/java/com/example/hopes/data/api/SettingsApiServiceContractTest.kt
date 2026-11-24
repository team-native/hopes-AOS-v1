package com.example.hopes.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
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
import java.util.concurrent.atomic.AtomicReference

class SettingsApiServiceContractTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

    @Test
    fun `회원탈퇴 endpoint는 DELETE body로 비밀번호를 전송한다`() =
        runBlocking {
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

    @Test
    fun `비밀번호 재설정 요청은 Swagger 필드명을 사용한다`() {
        val jsonBody = json.encodeToString(PasswordResetRequestDto("school@gsm.hs.kr", "123456", "NewPass123", null))

        assertEquals(
            "{\"email\":\"school@gsm.hs.kr\",\"code\":\"123456\",\"newPassword\":\"NewPass123\"}",
            jsonBody,
        )
    }

    @Test
    fun `문의 요청은 내용과 답변 이메일을 별도 필드로 전송한다`() {
        val jsonBody = json.encodeToString(InquiryRequestDto("도움이 필요해요", "reply@gsm.hs.kr"))

        assertEquals(
            "{\"content\":\"도움이 필요해요\",\"replyEmail\":\"reply@gsm.hs.kr\"}",
            jsonBody,
        )
    }

    /** 실제 네트워크 대신 요청을 캡처하고 204 응답을 반환하는 SettingsApiService를 생성한다. */
    private fun createSettingsApiService(capturedRequest: AtomicReference<Request>): SettingsApiService {
        val client =
            OkHttpClient
                .Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    capturedRequest.set(request)

                    Response
                        .Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(204)
                        .message("No Content")
                        .body(ByteArray(0).toResponseBody(null))
                        .build()
                }.build()

        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://example.com/")
                .client(client)
                .addConverterFactory(
                    json.asConverterFactory("application/json".toMediaType()),
                ).build()

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
