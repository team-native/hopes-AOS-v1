package com.example.hopes.data.remote

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import com.example.hopes.core.network.ApiExecutor
import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.AttachmentApiService
import com.example.hopes.data.api.AttachmentResponseDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

interface AttachmentRemoteDataSource {
    suspend fun upload(
        uri: String,
        fileName: String,
        mimeType: String,
        clientRequestId: String,
        onProgress: (Float) -> Unit,
    ): NetworkResult<AttachmentResponseDto>

    suspend fun delete(attachmentId: Long): NetworkResult<Unit>
}

class AttachmentRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiExecutor: ApiExecutor,
        private val attachmentApiService: AttachmentApiService,
        @ApplicationContext private val context: Context,
    ) : AttachmentRemoteDataSource {
        override suspend fun delete(attachmentId: Long): NetworkResult<Unit> =
            apiExecutor.executeNoContent { attachmentApiService.deleteAttachment(attachmentId) }

        override suspend fun upload(
            uri: String,
            fileName: String,
            mimeType: String,
            clientRequestId: String,
            onProgress: (Float) -> Unit,
        ): NetworkResult<AttachmentResponseDto> =
            withContext(Dispatchers.IO) {
                val tempFile = File.createTempFile("hope-attachment-", ".upload", context.cacheDir)
                try {
                    copyUriWithSizeLimit(Uri.parse(uri), tempFile)
                    if (mimeType.startsWith("image/")) stripGpsMetadata(tempFile)
                    val requestBody = ProgressFileRequestBody(tempFile, mimeType.toMediaType(), onProgress)
                    val filePart = MultipartBody.Part.createFormData("file", fileName, requestBody)
                    apiExecutor.execute { attachmentApiService.uploadAttachment(clientRequestId, filePart) }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    NetworkResult.NetworkError(exception)
                } finally {
                    tempFile.delete()
                }
            }

        private fun copyUriWithSizeLimit(uri: Uri, destination: File) {
            val input = requireNotNull(context.contentResolver.openInputStream(uri)) { "첨부 파일을 열 수 없습니다." }
            input.use { source ->
                FileOutputStream(destination).use { output ->
                    copyWithSizeLimit(source, output)
                }
            }
            if (destination.length() == 0L) throw IOException("빈 첨부 파일은 올릴 수 없습니다.")
        }

        private fun stripGpsMetadata(file: File) {
            val exif = ExifInterface(file)
            GPS_EXIF_TAGS.forEach { tag -> exif.setAttribute(tag, null) }
            exif.saveAttributes()
        }
    }

private class ProgressFileRequestBody(
    private val file: File,
    private val mediaType: MediaType,
    private val onProgress: (Float) -> Unit,
) : RequestBody() {
    override fun contentType(): MediaType = mediaType

    override fun contentLength(): Long = file.length()

    override fun writeTo(sink: BufferedSink) {
        file.inputStream().use { stream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var totalBytesWritten = 0L
            while (true) {
                val bytesRead = stream.read(buffer)
                if (bytesRead < 0) break
                sink.write(buffer, 0, bytesRead)
                totalBytesWritten += bytesRead
                onProgress((totalBytesWritten.toFloat() / file.length()).coerceIn(0f, 1f))
            }
        }
    }
}
