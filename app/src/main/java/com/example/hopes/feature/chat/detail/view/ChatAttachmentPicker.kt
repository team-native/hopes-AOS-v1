package com.example.hopes.feature.chat.detail.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.feature.chat.detail.view.component.ChatAttachmentSheet
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.teamnative.hopes.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 자료 첨부 시트(피그마 09)와 사진 보관함·카메라·PDF 선택, 카메라 권한 안내(09-2)를 묶었다.
 * 채팅 홈과 채팅 상세가 같이 쓴다. 고른 파일을 화면에서 쓸 첨부 정보로 바꿔 [onAttachmentsPicked]로 넘긴다.
 *
 * 선택 화면의 결과를 받으려면 시트가 닫혀 있어도 늘 그려 두어야 한다.
 *
 * @param isSheetVisible 첨부 시트를 열지 여부다.
 * @param onCameraUnavailable 카메라 앱이 없어 촬영을 못 할 때 부른다. 화면에서 안내를 띄운다.
 */
@Composable
fun ChatAttachmentPicker(
    isSheetVisible: Boolean,
    onSheetDismiss: () -> Unit,
    onAttachmentsPicked: (List<ChatAttachmentUiModel>) -> Unit,
    onCameraUnavailable: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val imageFallbackName = stringResource(R.string.chat_attach_image)
    val pdfFallbackName = stringResource(R.string.chat_attach_pdf_label)
    // 카메라 앱이 사진을 저장할 자리다. 화면 회전으로 다시 그려져도 결과를 받을 수 있게 저장해 둔다.
    var pendingCameraUri by rememberSaveable { mutableStateOf<String?>(null) }
    var isMediaPermissionDialogVisible by rememberSaveable { mutableStateOf(false) }

    fun addAttachments(
        uris: List<Uri>,
        kind: ChatAttachmentKind,
        fallbackName: String,
    ) {
        if (uris.isEmpty()) return
        coroutineScope.launch {
            // PDF 쪽수를 읽느라 파일을 여므로 IO 스레드에서 정보를 만든다.
            val attachments =
                withContext(Dispatchers.IO) {
                    uris.map { uri -> context.contentResolver.toChatAttachment(uri, kind, fallbackName) }
                }
            onAttachmentsPicked(attachments)
        }
    }

    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            addAttachments(uris, ChatAttachmentKind.Image, imageFallbackName)
        }
    val pdfPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            addAttachments(uris, ChatAttachmentKind.Pdf, pdfFallbackName)
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->
            val cameraUri = pendingCameraUri
            pendingCameraUri = null
            if (isSaved && cameraUri != null) {
                addAttachments(listOf(Uri.parse(cameraUri)), ChatAttachmentKind.Image, imageFallbackName)
            }
        }

    if (isSheetVisible) {
        ChatAttachmentSheet(
            onPhotoClick = {
                onSheetDismiss()
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onCameraClick = {
                onSheetDismiss()
                val cameraUri = context.createCameraPhotoUri()
                pendingCameraUri = cameraUri.toString()
                try {
                    cameraLauncher.launch(cameraUri)
                } catch (exception: ActivityNotFoundException) {
                    pendingCameraUri = null
                    onCameraUnavailable()
                } catch (exception: SecurityException) {
                    // 기기 정책 등으로 카메라 접근이 막히면 설정에서 허용하도록 안내한다(피그마 09-2).
                    pendingCameraUri = null
                    isMediaPermissionDialogVisible = true
                }
            },
            onPdfClick = {
                onSheetDismiss()
                pdfPickerLauncher.launch(arrayOf(PDF_MIME_TYPE))
            },
            onDismiss = onSheetDismiss,
        )
    }

    if (isMediaPermissionDialogVisible) {
        HopesAlertDialog(
            title = stringResource(R.string.chat_attach_permission_title),
            message = stringResource(R.string.chat_attach_permission_message),
            dismissText = stringResource(R.string.cancel),
            confirmText = stringResource(R.string.open_settings),
            onDismiss = { isMediaPermissionDialogVisible = false },
            onConfirm = {
                isMediaPermissionDialogVisible = false
                context.openAppSettings()
            },
        )
    }
}

/** 카메라 앱이 찍은 사진을 저장할 캐시 파일의 content URI를 만든다. */
private fun Context.createCameraPhotoUri(): Uri {
    val directory = File(cacheDir, CAMERA_PHOTO_DIRECTORY).apply { mkdirs() }
    val photoFile = File(directory, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
}

private const val PDF_MIME_TYPE = "application/pdf"

// res/xml/file_provider_paths.xml의 cache-path와 같은 폴더다.
private const val CAMERA_PHOTO_DIRECTORY = "chat_attachments"
