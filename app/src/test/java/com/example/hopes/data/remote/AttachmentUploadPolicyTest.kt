package com.example.hopes.data.remote

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class AttachmentUploadPolicyTest {
    @Test
    fun `GPS IFD에 위치 설명과 정확도 태그가 포함된다`() {
        assertEquals(true, GPS_EXIF_TAGS.contains("GPSAreaInformation"))
        assertEquals(true, GPS_EXIF_TAGS.contains("GPSHPositioningError"))
        assertEquals(true, GPS_EXIF_TAGS.contains("GPSVersionID"))
    }

    @Test
    fun `크기가 알려지지 않은 스트림도 정확히 20MiB까지 복사한다`() {
        val output = ByteArrayOutputStream()

        val copied = copyWithSizeLimit(ByteArrayInputStream(ByteArray(20 * 1024 * 1024)), output)

        assertEquals(20L * 1024L * 1024L, copied)
        assertEquals(copied, output.size().toLong())
    }

    @Test
    fun `20MiB를 초과하는 스트림 복사를 거부한다`() {
        val output = ByteArrayOutputStream()

        assertThrows(IOException::class.java) {
            copyWithSizeLimit(ByteArrayInputStream(ByteArray(20 * 1024 * 1024 + 1)), output)
        }
    }
}
