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
    fun `http error preserves status code and server message`() {
        // ApiExecutor가 오류 응답 본문을 이미 안전하게 파싱해 message에 담아 넘기므로,
        // 매퍼는 그 값을 그대로 AppResult로 전달하기만 하면 된다.
        val result = NetworkResult.HttpError(401, "이메일 또는 비밀번호가 올바르지 않습니다.").toAppResult { value: String -> value }

        assertEquals(AppResult.HttpError(401, "이메일 또는 비밀번호가 올바르지 않습니다."), result)
    }

    @Test
    fun `network failure maps to domain network error`() {
        val result = NetworkResult.NetworkError(IllegalStateException()).toAppResult { value: String -> value }

        assertEquals(AppResult.NetworkError, result)
    }
}
