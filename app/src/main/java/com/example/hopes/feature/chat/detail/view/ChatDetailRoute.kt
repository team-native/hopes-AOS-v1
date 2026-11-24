package com.example.hopes.feature.chat.detail.view

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.CancellationSignal
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ClipEntry
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailViewModel
import com.example.hopes.feature.chat.view.label
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.teamnative.hopes.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import kotlin.coroutines.resume

/**
 * 서버 대화 상세 상태를 수집하고 뒤로가기·복사·공유를 연결한다.
 *
 * @param attachments 채팅 홈에서 질문과 함께 붙인 첨부 자료다. 첫 질문에 붙여 보낸다.
 * @param department 채팅 홈에서 고른 학과다. 학과·전공을 모두 알면 부제목에 쓰고, 아니면 기본 부제목을 쓴다.
 */
@Composable
fun ChatDetailRoute(
    chatId: Long,
    question: String,
    attachments: List<ChatAttachmentUiModel>,
    department: ChatDepartment?,
    major: ChatMajor?,
    departmentId: Long?,
    specialtyId: Long?,
    onBackClick: () -> Unit,
    onStartNewChat: () -> Unit,
    onViewSchoolMap: (buildingName: String, accuracyMeters: Int?) -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(chatId, question) { viewModel.initialize(chatId, question, attachments, departmentId, specialtyId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) fetchAndSubmitLocation(context, coroutineScope, viewModel)
        else viewModel.locationCheckFailed()
    }
    var toastMessageResId by rememberSaveable { mutableStateOf<Int?>(null) }
    val subtitle =
        if (uiState.location != null) {
            stringResource(R.string.chat_location_subtitle)
        } else if (department != null && major != null) {
            stringResource(R.string.chat_detail_subtitle, department.label(), major.label())
        } else {
            stringResource(R.string.chat_answer_label)
        }
    val shareTitle = stringResource(R.string.chat_share_answer)
    var isAttachmentSheetVisible by rememberSaveable { mutableStateOf(false) }

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
            } else if (event == ChatDetailScreenEvent.LocationAllowClicked || event == ChatDetailScreenEvent.LocationRetryClicked) {
                val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (hasPermission) fetchAndSubmitLocation(context, coroutineScope, viewModel)
                else permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
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
        onOpenSettings = { context.openAppSettings() },
        onViewSchoolMap = onViewSchoolMap,
    )

    ChatAttachmentPicker(
        isSheetVisible = isAttachmentSheetVisible,
        onSheetDismiss = { isAttachmentSheetVisible = false },
        onAttachmentsPicked = { viewModel.onEvent(ChatDetailScreenEvent.AttachmentsAdded(it)) },
        onCameraUnavailable = { toastMessageResId = R.string.chat_attach_camera_unavailable },
    )
}

private fun fetchAndSubmitLocation(
    context: Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    viewModel: ChatDetailViewModel,
) {
    viewModel.locationCheckStarted()
    coroutineScope.launch {
        val location = try {
            context.currentCoarseLocation()
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            null
        }
        if (location == null || !location.hasAccuracy() || location.accuracy <= 0f) {
            viewModel.locationCheckFailed()
        } else {
            viewModel.submitLocationCheck(
                latitude = location.latitude,
                longitude = location.longitude,
                accuracyMeters = location.accuracy.toDouble(),
                measuredAt = Instant.ofEpochMilli(location.time).toString(),
            )
        }
    }
}

@Suppress("MissingPermission")
private suspend fun Context.currentCoarseLocation(): Location? = suspendCancellableCoroutine { continuation ->
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val provider = locationManager.getBestProvider(Criteria().apply { accuracy = Criteria.ACCURACY_COARSE }, true)
    if (provider == null) {
        continuation.resume(null)
        return@suspendCancellableCoroutine
    }
    val cancellationSignal = CancellationSignal()
    continuation.invokeOnCancellation { cancellationSignal.cancel() }
    locationManager.getCurrentLocation(provider, cancellationSignal, mainExecutor) { location ->
        if (continuation.isActive) continuation.resume(location)
    }
}

/** 이 앱의 시스템 설정 화면을 연다. 권한을 직접 허용하도록 안내할 때 쓴다. */
internal fun Context.openAppSettings() {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        startActivity(intent)
    } catch (exception: ActivityNotFoundException) {
        startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
