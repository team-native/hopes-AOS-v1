package com.example.hopes.core.network

import com.example.hopes.core.session.SessionManager
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/** 인증 Retrofit 요청에만 현재 access token을 Bearer 헤더로 추가한다. */
class AuthorizationInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.currentAccessToken()
        val requestBuilder = chain.request().newBuilder()

        if (token != null) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
