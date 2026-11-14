package com.example.hopes.feature.chat.detail.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesErrorBanner
import com.example.hopes.core.designsystem.component.HopesInfoBanner
import com.example.hopes.core.designsystem.component.HopesRetryMessage
import com.example.hopes.core.designsystem.component.HopesSecondaryButton
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.chat.detail.viewmodel.ChatLocationUiState
import com.example.hopes.feature.schoolmap.presentation.SchoolMapPlaceholder
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * "지금 내 위치 알려줘" 같은 질문 아래에 붙는 위치 안내 카드다(피그마 20 ~ 23).
 * 상태마다 권한 요청, 측정 중, 위치 결과, 학교 밖, 정확도 낮음, 실패 안내를 그린다.
 */
@Composable
fun ChatLocationCard(
    state: ChatLocationUiState,
    onAllowClick: () -> Unit,
    onLaterClick: () -> Unit,
    onRetryClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
    onViewMapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ChatLocationUiState.PermissionRequired -> {
            LocationCardSurface(modifier = modifier) {
                LocationHeaderRow(
                    title = stringResource(R.string.chat_location_permission_title),
                    description = stringResource(R.string.chat_location_permission_description),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
                    LocationTextButton(
                        text = stringResource(R.string.chat_location_later),
                        onClick = onLaterClick,
                        isPrimary = false,
                        modifier = Modifier.weight(1f),
                    )

                    LocationTextButton(
                        text = stringResource(R.string.chat_location_allow),
                        onClick = onAllowClick,
                        isPrimary = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        is ChatLocationUiState.Locating -> {
            LocationCardSurface(modifier = modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
                LocationHeaderRow(
                    title = stringResource(R.string.chat_location_locating_title),
                    description = stringResource(R.string.chat_location_locating_description),
                )

                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(PROGRESS_HEIGHT),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                    drawStopIndicator = {},
                )
            }
        }

        is ChatLocationUiState.NearBuilding -> {
            Column(modifier = modifier.fillMaxWidth()) {
                LocationCardSurface {
                    LocationTitle(text = stringResource(R.string.chat_location_near_title, state.buildingName))

                    SchoolMapPlaceholder(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(MINI_MAP_HEIGHT),
                    )

                    Column {
                        Text(
                            text = stringResource(R.string.chat_location_place, state.buildingName),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = HopesTextStyles.LabelStrong,
                        )

                        Text(
                            text = stringResource(R.string.chat_location_accuracy, state.accuracyMeters),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = HopesTextStyles.Caption,
                        )
                    }

                    LocationTextButton(
                        text = stringResource(R.string.chat_location_view_map),
                        onClick = onViewMapClick,
                        isPrimary = false,
                        isTonal = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.Compact))

                Text(
                    text = stringResource(R.string.chat_location_gps_limit),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        is ChatLocationUiState.OutsideSchool -> {
            LocationCardSurface(modifier = modifier) {
                LocationTitle(text = stringResource(R.string.chat_location_outside_title, state.distanceKm))

                HopesInfoBanner(message = stringResource(R.string.chat_location_outside_hint))

                LocationTextButton(
                    text = stringResource(R.string.chat_location_view_map),
                    onClick = onViewMapClick,
                    isPrimary = false,
                    isTonal = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        is ChatLocationUiState.LowAccuracy -> {
            LocationCardSurface(modifier = modifier) {
                LocationTitle(text = stringResource(R.string.chat_location_low_accuracy_title))

                HopesErrorBanner(
                    message = stringResource(R.string.chat_location_low_accuracy_warning, state.accuracyMeters),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
                    LocationTextButton(
                        text = stringResource(R.string.chat_location_measure_again),
                        onClick = onRetryClick,
                        isPrimary = false,
                        modifier = Modifier.weight(1f),
                    )

                    LocationTextButton(
                        text = stringResource(R.string.chat_location_view_map),
                        onClick = onViewMapClick,
                        isPrimary = false,
                        isTonal = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        ChatLocationUiState.Failed -> {
            HopesRetryMessage(
                title = stringResource(R.string.chat_location_failed_title),
                description = stringResource(R.string.chat_location_failed_description),
                onRetryClick = onRetryClick,
                secondaryActionText = stringResource(R.string.open_settings),
                onSecondaryActionClick = onOpenSettingsClick,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun LocationCardSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .padding(AppSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        content = content,
    )
}

@Composable
private fun LocationHeaderRow(
    title: String,
    description: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier =
                Modifier
                    .size(ICON_TILE_SIZE)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(AppRadius.SmallButton)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Column {
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
    }
}

@Composable
private fun LocationTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        style = HopesTextStyles.Body,
    )
}

/**
 * 카드 안의 작은 버튼이다. [isPrimary]면 파란 배경, [isTonal]이면 연한 파란 배경, 둘 다 아니면 흰 테두리 버튼이다.
 */
@Composable
private fun LocationTextButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    isTonal: Boolean = false,
) {
    if (!isPrimary && !isTonal) {
        HopesSecondaryButton(
            text = text,
            onClick = onClick,
            modifier = modifier.height(AppSize.SmallButton),
        )
        return
    }

    val buttonShape = RoundedCornerShape(AppRadius.SmallButton)

    Box(
        modifier =
            modifier
                .height(AppSize.SmallButton)
                .background(
                    color =
                        if (isPrimary) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                    shape = buttonShape,
                ).shapeClickable(shape = buttonShape, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.LabelStrong,
        )
    }
}

private val ICON_TILE_SIZE = 36.dp
private val MINI_MAP_HEIGHT = 96.dp
private val PROGRESS_HEIGHT = 4.dp
