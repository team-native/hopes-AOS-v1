package com.example.hopes.core.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupValidationExtensionsTest {
    @Test
    fun signupPassword_requiresLengthLetterDigitAndSymbol() {
        assertTrue("abcd123!".isValidSignupPassword())
        assertFalse("abc12!".isValidSignupPassword())
        assertFalse("abcdefg!".isValidSignupPassword())
        assertFalse("12345678!".isValidSignupPassword())
        assertFalse("abcd1234".isValidSignupPassword())
        assertFalse("abcd 123!".isValidSignupPassword())
    }

    @Test
    fun specialCharacter_ignoresKoreanAndWhitespace() {
        assertFalse("abcd1234가".containsSpecialCharacter())
        assertFalse("abcd 1234".containsSpecialCharacter())
        assertTrue("abcd1234~".containsSpecialCharacter())
    }

    @Test
    fun schoolEmailInProgress_flagsOnlyConfirmedOtherDomains() {
        assertTrue("s26055".canStillBecomeSchoolEmail())
        assertTrue("s26055@".canStillBecomeSchoolEmail())
        assertTrue("s26055@gsm.h".canStillBecomeSchoolEmail())
        assertTrue("s26055@GSM.HS.KR".canStillBecomeSchoolEmail())
        assertFalse("imseoha@gmail.com".canStillBecomeSchoolEmail())
        assertFalse("s26055@gsm.hs.kr.com".canStillBecomeSchoolEmail())
    }
}
