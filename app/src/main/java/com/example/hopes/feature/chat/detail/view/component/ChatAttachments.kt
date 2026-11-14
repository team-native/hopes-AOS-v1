package com.example.hopes.feature.chat.detail.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.overlay.ApplyDialogWindowBackgroundBlur
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 입력창 + 버튼으로 여는 자료 첨부 시트다(피그마 09). 사진 보관함·카메라·PDF 중 하나를 고른다. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAttachmentSheet(
    onPhotoClick: () -> Unit,
    onCameraClick: () -> Unit,
    onPdfClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        ApplyDialogWindowBackgroundBlur(blurRadius = AppBlurRadius.DialogWindowBackdrop)

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
        ) {
            Text(
                text = stringResource(R.string.chat_attach_sheet_title),
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.SheetTitle,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            ChatAttachmentSheetRow(
                icon = Icons.Outlined.Image,
                title = stringResource(R.string.chat_attach_photo),
                description = stringResource(R.string.chat_attach_photo_description),
                onClick = onPhotoClick,
            )

            ChatAttachmentSheetRow(
                icon = Icons.Outlined.PhotoCamera,
                title = stringResource(R.string.chat_attach_camera),
                description = stringResource(R.string.chat_attach_camera_description),
                onClick = onCameraClick,
            )

            ChatAttachmentSheetRow(
                icon = Icons.Outlined.Description,
                title = stringResource(R.string.chat_attach_pdf),
                description = stringResource(R.string.chat_attach_pdf_description),
                onClick = onPdfClick,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            Text(
                text = stringResource(R.string.chat_attach_sheet_footer),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))
        }
    }
}

/**
 * 입력창 바로 위에 붙는 "첨부 N개" 미리보기다(피그마 10). 항목마다 X로 뺄 수 있다.
 */
@Composable
fun ChatDraftAttachments(
    attachments: List<ChatAttachmentUiModel>,
    onRemove: (ChatAttachmentUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.chat_attach_count, attachments.size),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.CaptionStrong,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = REMOVE_BADGE_OVERFLOW, end = REMOVE_BADGE_OVERFLOW),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            attachments.forEach { attachment ->
                when (attachment.kind) {
                    ChatAttachmentKind.Image -> {
                        Box {
                            ChatImageTile(size = DRAFT_IMAGE_SIZE)
                            ChatRemoveAttachmentBadge(
                                name = attachment.name,
                                onClick = { onRemove(attachment) },
                                modifier =
                                    Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = REMOVE_BADGE_OVERFLOW, y = -REMOVE_BADGE_OVERFLOW),
                            )
                        }
                    }

                    ChatAttachmentKind.Pdf -> {
                        ChatPdfChip(
                            attachment = attachment,
                            showsDetail = true,
                            onRemove = { onRemove(attachment) },
                        )
                    }
                }
            }
        }
    }
}

/** 보낸 질문 위에 붙는 첨부 자료 줄이다(피그마 11). 오른쪽 정렬로 쌓는다. */
@Composable
fun ChatMessageAttachments(
    attachments: List<ChatAttachmentUiModel>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        attachments.forEach { attachment ->
            when (attachment.kind) {
                ChatAttachmentKind.Image -> ChatImageTile(size = MESSAGE_IMAGE_SIZE)
                ChatAttachmentKind.Pdf -> ChatPdfChip(attachment = attachment, showsDetail = false, onRemove = null)
            }
        }
    }
}

/** "이미지 2장, PDF 1개"처럼 첨부 종류별 개수를 한 줄로 만든다. */
@Composable
fun chatAttachmentSummary(attachments: List<ChatAttachmentUiModel>): String {
    val imageCount = attachments.count { it.kind == ChatAttachmentKind.Image }
    val pdfCount = attachments.count { it.kind == ChatAttachmentKind.Pdf }

    return listOfNotNull(
        if (imageCount > 0) stringResource(R.string.chat_attach_summary_images, imageCount) else null,
        if (pdfCount > 0) stringResource(R.string.chat_attach_summary_pdfs, pdfCount) else null,
    ).joinToString(separator = ", ")
}

@Composable
private fun ChatAttachmentSheetRow(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button, onClick = onClick)
                .padding(vertical = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(SHEET_ICON_SIZE)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(AppRadius.SmallButton),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )

            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Medium),
        )
    }
}

@Composable
private fun ChatImageTile(size: Dp) {
    val tileShape = RoundedCornerShape(AppRadius.Field)

    Box(
        modifier =
            Modifier
                .size(size)
                .figmaSubtleShadow(tileShape)
                .background(MaterialTheme.colorScheme.surface, tileShape)
                .border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, tileShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = stringResource(R.string.chat_attach_image),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppIconSize.Medium),
        )
    }
}

@Composable
private fun ChatPdfChip(
    attachment: ChatAttachmentUiModel,
    showsDetail: Boolean,
    onRemove: (() -> Unit)?,
) {
    val chipShape = RoundedCornerShape(AppRadius.Field)

    Row(
        modifier =
            Modifier
                .figmaSubtleShadow(chipShape)
                .background(MaterialTheme.colorScheme.surface, chipShape)
                .border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, chipShape)
                .padding(start = AppSpacing.Item, end = if (onRemove != null) AppSpacing.XSmall else AppSpacing.Item)
                .height(if (showsDetail) DRAFT_IMAGE_SIZE else MESSAGE_PDF_HEIGHT),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small + AppSpacing.XSmall))

        Column(modifier = Modifier.widthIn(max = PDF_NAME_MAX_WIDTH)) {
            Text(
                text = attachment.name,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = HopesTextStyles.LabelStrong,
            )

            if (showsDetail) {
                Text(
                    text =
                        attachment.pageCount?.let { stringResource(R.string.chat_attach_pdf_pages, it) }
                            ?: stringResource(R.string.chat_attach_pdf_label),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        if (onRemove != null) {
            Box(
                modifier =
                    Modifier
                        .size(REMOVE_TOUCH_SIZE)
                        .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.chat_attach_remove, attachment.name),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AppIconSize.Inline),
                )
            }
        }
    }
}

@Composable
private fun ChatRemoveAttachmentBadge(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .size(REMOVE_BADGE_SIZE)
                .background(MaterialTheme.colorScheme.inverseSurface, CircleShape)
                .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.chat_attach_remove, name),
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.size(REMOVE_BADGE_ICON_SIZE),
        )
    }
}

// 피그마 09~11 첨부 시트 아이콘, 미리보기 타일, 보낸 메시지 타일 크기다.
private val SHEET_ICON_SIZE = 40.dp
private val DRAFT_IMAGE_SIZE = 56.dp
private val MESSAGE_IMAGE_SIZE = 44.dp
private val MESSAGE_PDF_HEIGHT = 44.dp
private val PDF_NAME_MAX_WIDTH = 120.dp
private val REMOVE_TOUCH_SIZE = 32.dp
private val REMOVE_BADGE_SIZE = 20.dp
private val REMOVE_BADGE_ICON_SIZE = 12.dp
private val REMOVE_BADGE_OVERFLOW = 6.dp
