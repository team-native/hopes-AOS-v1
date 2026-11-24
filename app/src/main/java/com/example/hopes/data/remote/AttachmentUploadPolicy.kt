package com.example.hopes.data.remote

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

internal const val MAX_UPLOAD_SIZE_BYTES = 20L * 1024L * 1024L

internal val GPS_EXIF_TAGS = listOf(
    "GPSVersionID",
    "GPSLatitudeRef", "GPSLatitude", "GPSLongitudeRef", "GPSLongitude",
    "GPSAltitudeRef", "GPSAltitude", "GPSTimeStamp", "GPSSatellites", "GPSStatus",
    "GPSMeasureMode", "GPSDOP", "GPSSpeedRef", "GPSSpeed", "GPSTrackRef", "GPSTrack",
    "GPSImgDirectionRef", "GPSImgDirection", "GPSMapDatum",
    "GPSDestLatitudeRef", "GPSDestLatitude", "GPSDestLongitudeRef", "GPSDestLongitude",
    "GPSDestBearingRef", "GPSDestBearing", "GPSDestDistanceRef", "GPSDestDistance",
    "GPSProcessingMethod", "GPSAreaInformation", "GPSDateStamp", "GPSDifferential",
    "GPSHPositioningError",
)

internal fun copyWithSizeLimit(input: InputStream, output: OutputStream, maxBytes: Long = MAX_UPLOAD_SIZE_BYTES): Long {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var total = 0L
    while (true) {
        val count = input.read(buffer)
        if (count < 0) break
        total += count
        if (total > maxBytes) throw IOException("첨부 파일이 20 MiB 제한을 초과했습니다.")
        output.write(buffer, 0, count)
    }
    return total
}
