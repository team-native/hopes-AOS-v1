package com.example.hopes.feature.home.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class HomeGreetingNameTest {
    @Test
    fun threeLetterKoreanName_dropsFamilyName() {
        assertEquals("서하", "임서하".toHomeGreetingName())
    }

    @Test
    fun twoLetterKoreanName_isKeptAsIs() {
        assertEquals("서하", "서하".toHomeGreetingName())
    }

    @Test
    fun nonKoreanOrLongName_isKeptAsIs() {
        assertEquals("Seoha", "Seoha".toHomeGreetingName())
        assertEquals("남궁서하", "남궁서하".toHomeGreetingName())
        assertEquals("서하1", "서하1".toHomeGreetingName())
    }

    @Test
    fun blankName_returnsNull() {
        assertNull("  ".toHomeGreetingName())
    }

    @Test
    fun surroundingSpaces_areTrimmed() {
        assertEquals("서하", " 임서하 ".toHomeGreetingName())
    }
}
