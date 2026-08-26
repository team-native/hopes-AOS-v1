package com.example.hopes.core.network

import com.example.hopes.core.session.SessionManager
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

/** 인증 Retrofit 요청에 현재 세션의 tokenType과 access token을 Authorization 헤더로 추가한다. */
class AuthorizationInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sessionManager.currentAccessToken()
        val tokenType = sessionManager.currentTokenType()
        val requestBuilder = chain.request().newBuilder()

        buildAuthorizationHeader(tokenType, accessToken)?.let { authorizationHeader ->
            requestBuilder.header("Authorization", authorizationHeader)
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

/** 저장된 tokenType과 access token으로 Authorization 헤더 값을 생성한다. */
internal fun buildAuthorizationHeader(
    tokenType: String?,
    accessToken: String?,
): String? {
    if (tokenType.isNullOrBlank() || accessToken.isNullOrBlank()) {
        return null
    }

    return "$tokenType $accessToken"
}
