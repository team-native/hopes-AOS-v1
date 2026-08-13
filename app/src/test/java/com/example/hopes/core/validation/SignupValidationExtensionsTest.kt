package com.example.hopes.core.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupValidationExtensionsTest {
    @Test
    fun `학교 이메일은 gsm 도메인과 최대 255자를 만족하면 유효하다`() {
        assertTrue("s26055@gsm.hs.kr".isValidSchoolEmail())
        assertTrue("${"a".repeat(245)}@gsm.hs.kr".isValidSchoolEmail())
    }

    @Test
    fun `학교 이메일은 다른 도메인과 빈 값 그리고 최대 길이 초과를 거부한다`() {
        assertFalse("s26055@example.com".isValidSchoolEmail())
        assertFalse("".isValidSchoolEmail())
        assertFalse("${"a".repeat(246)}@gsm.hs.kr".isValidSchoolEmail())
    }

    @Test
    fun `이름은 비어 있지 않고 최대 50자면 유효하다`() {
        assertTrue("임서하".isValidUsername())
        assertTrue("a".repeat(50).isValidUsername())
        assertFalse("".isValidUsername())
        assertFalse("a".repeat(51).isValidUsername())
    }

    @Test
    fun `비밀번호는 영문과 숫자로만 구성된 8자에서 15자 사이여야 한다`() {
        assertTrue("password1".isValidSignupPassword())
        assertTrue("password1234567".isValidSignupPassword())
        assertFalse("pass1".isValidSignupPassword())
        assertFalse("password12345678".isValidSignupPassword())
        assertFalse("password".isValidSignupPassword())
        assertFalse("12345678".isValidSignupPassword())
        assertFalse("password1!".isValidSignupPassword())
    }
}
