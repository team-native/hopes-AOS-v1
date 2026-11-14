package com.example.hopes.feature.detail.presentation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactValidationTest {
    @Test
    fun schoolAndOtherEmails_areValid() {
        assertTrue("s26055@gsm.hs.kr".isValidContactEmail())
        assertTrue("seoha.lim+hopes@gmail.com".isValidContactEmail())
    }

    @Test
    fun surroundingSpaces_areIgnored() {
        assertTrue(" s26055@gsm.hs.kr ".isValidContactEmail())
    }

    @Test
    fun missingDomainOrTopLevelDomain_isInvalid() {
        assertFalse("s26055@gsm".isValidContactEmail())
        assertFalse("s26055".isValidContactEmail())
        assertFalse("@gsm.hs.kr".isValidContactEmail())
        assertFalse("".isValidContactEmail())
    }
}
