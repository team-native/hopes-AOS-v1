package com.example.hopes.domain.repository

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.result.AppResult

interface AuthRepository {
    /** 로그인 화면의 제출 이벤트에서 자격 증명을 서버로 검증한다. */
    suspend fun login(username: String, password: String): AppResult<AuthToken>
}
