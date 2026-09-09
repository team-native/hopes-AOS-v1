package com.example.hopes.core.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthorizationInterceptorTest {
    @Test
    fun `buildAuthorizationHeader uses the stored token type`() {
        val header = buildAuthorizationHeader(
            tokenType = "Custom",
            accessToken = "access-token",
        )

        assertEquals("Custom access-token", header)
    }

    @Test
    fun `buildAuthorizationHeader returns null when session credentials are incomplete`() {
        assertNull(
            buildAuthorizationHeader(
                tokenType = null,
                accessToken = "access-token",
            ),
        )
        assertNull(
            buildAuthorizationHeader(
                tokenType = "Bearer",
                accessToken = null,
            ),
        )
    }
}
