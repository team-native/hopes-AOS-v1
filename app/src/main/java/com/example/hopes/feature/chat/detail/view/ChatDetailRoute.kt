package com.example.hopes.feature.chat.detail.view

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.feature.chat.detail.view.component.ChatAttachmentSheet
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailViewModel
import com.example.hopes.feature.chat.view.label
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.teamnative.hopes.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 서버 대화 상세 상태를 수집하고 뒤로가기·복사·공유를 연결한다.
 *
 * @param department 채팅 홈에서 고른 학과다. 학과·전공을 모두 알면 부제목에 쓰고, 아니면 기본 부제목을 쓴다.
 */
@Composable
fun ChatDetailRoute(
    chatId: Long,
    question: String,
    department: ChatDepartment?,
    major: ChatMajor?,
    onBackClick: () -> Unit,
    onStartNewChat: () -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(chatId, question) { viewModel.initialize(chatId, question) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var toastMessageResId by rememberSaveable { mutableStateOf<Int?>(null) }
    val subtitle =
        if (department != null && major != null) {
            stringResource(R.string.chat_detail_subtitle, department.label(), major.label())
        } else {
            stringResource(R.string.chat_answer_label)
        }
    val shareTitle = stringResource(R.string.chat_share_answer)
    val imageFallbackName = stringResource(R.string.chat_attach_image)
    val pdfFallbackName = stringResource(R.string.chat_attach_pdf_label)
    var isAttachmentSheetVisible by rememberSaveable { mutableStateOf(false) }
    // 카메라 앱이 사진을 저장할 자리다. 화면 회전으로 Route가 다시 만들어져도 결과를 받을 수 있게 저장해 둔다.
    var pendingCameraUri by rememberSaveable { mutableStateOf<String?>(null) }

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
            viewModel.onEvent(ChatDetailScreenEvent.AttachmentsAdded(attachments))
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

    fun copyToClipboard(
        text: String,
        toastResId: Int,
    ) {
        coroutineScope.launch {
            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, text)))
            toastMessageResId = toastResId
        }
    }

    ChatDetailScreen(
        uiState = uiState,
        subtitle = subtitle,
        onEvent = { event ->
            if (event == ChatDetailScreenEvent.BackClicked) {
                onBackClick()
            } else {
                viewModel.onEvent(event)
            }
        },
        onCopyAnswer = { answer -> copyToClipboard(answer, R.string.chat_answer_copied_toast) },
        onShareAnswer = { answer ->
            val sendIntent =
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, answer)
                }
            context.startActivity(Intent.createChooser(sendIntent, shareTitle))
        },
        onCopyCode = { code -> copyToClipboard(code, R.string.chat_code_copied_toast) },
        toastMessage = toastMessageResId?.let { stringResource(it) },
        onToastDismiss = { toastMessageResId = null },
        onNewChatConfirm = onStartNewChat,
        onAttachClick = { isAttachmentSheetVisible = true },
    )

    if (isAttachmentSheetVisible) {
        ChatAttachmentSheet(
            onPhotoClick = {
                isAttachmentSheetVisible = false
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onCameraClick = {
                isAttachmentSheetVisible = false
                val cameraUri = context.createCameraPhotoUri()
                pendingCameraUri = cameraUri.toString()
                try {
                    cameraLauncher.launch(cameraUri)
                } catch (exception: ActivityNotFoundException) {
                    pendingCameraUri = null
                    toastMessageResId = R.string.chat_attach_camera_unavailable
                }
            },
            onPdfClick = {
                isAttachmentSheetVisible = false
                pdfPickerLauncher.launch(arrayOf(PDF_MIME_TYPE))
            },
            onDismiss = { isAttachmentSheetVisible = false },
        )
    }
}

/** 카메라로 찍은 사진을 받을 캐시 파일을 만들고, 카메라 앱에 넘길 FileProvider URI를 돌려준다. */
private fun Context.createCameraPhotoUri(): Uri {
    val directory = File(cacheDir, CAMERA_PHOTO_DIRECTORY).apply { mkdirs() }
    val photoFile = File(directory, "photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
}

private const val PDF_MIME_TYPE = "application/pdf"

// res/xml/file_provider_paths.xml의 cache-path와 같은 폴더다.
private const val CAMERA_PHOTO_DIRECTORY = "chat_attachments"
