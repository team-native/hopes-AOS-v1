package com.example.hopes.feature.schoolmap.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesInfoBanner
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 22 학교 지도 화면이다. 지도 자리 표시, 층 칩, 현재 위치 카드, 실내 도면 준비 안내로 된다.
 * 층별 도면이 아직 없어 층을 골라도 지도는 바뀌지 않는다.
 *
 * @param accuracyMeters 위치 측정 정확도다. 모르면 null이라 정확도 줄을 숨긴다.
 */
@Composable
fun SchoolMapScreen(
    buildingName: String,
    accuracyMeters: Int?,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
) {
    var selectedFloor by rememberSaveable { mutableIntStateOf(FLOORS.first()) }

    HopesSubScreenFrame(
        title = stringResource(R.string.school_map_title),
        subtitle = stringResource(R.string.school_map_subtitle),
        onBackClick = onBackClick,
    ) {
        SchoolMapPlaceholder(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(MAP_HEIGHT),
            pinSize = MAP_PIN_SIZE,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Row(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FLOORS.forEach { floor ->
                FloorChip(
                    text = stringResource(R.string.school_map_floor, floor),
                    isSelected = floor == selectedFloor,
                    onClick = { selectedFloor = floor },
                )
            }

            Text(
                text = stringResource(R.string.school_map_floor_preparing),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        CurrentPlaceCard(
            buildingName = buildingName,
            accuracyMeters = accuracyMeters,
            onRefreshClick = onRefreshClick,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        HopesInfoBanner(message = stringResource(R.string.school_map_indoor_preparing))
    }
}

@Composable
private fun FloorChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .height(AppSize.Chip)
                .clip(CircleShape)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                ).border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, CircleShape)
                .selectable(selected = isSelected, role = Role.Tab, onClick = onClick)
                .padding(horizontal = AppSpacing.Item + AppSpacing.XSmall),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )
    }
}

@Composable
private fun CurrentPlaceCard(
    buildingName: String,
    accuracyMeters: Int?,
    onRefreshClick: () -> Unit,
) {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .padding(
                    start = AppSpacing.Medium,
                    top = AppSpacing.Item,
                    bottom = AppSpacing.Item,
                    end = AppSpacing.Small,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildingName,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )

            if (accuracyMeters != null) {
                Text(
                    text = stringResource(R.string.chat_location_accuracy, accuracyMeters),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        Box(
            modifier =
                Modifier
                    .size(AppSize.IconButton)
                    .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onRefreshClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = stringResource(R.string.school_map_refresh),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }
    }
}

// 피그마 22 기준 지도 높이와 핀 크기, 층 목록이다. 실내 도면을 받으면 층 목록도 서버 기준으로 바꾼다.
private val MAP_HEIGHT = 320.dp
private val MAP_PIN_SIZE = 36.dp
private val ICON_TILE_SIZE = 36.dp
private val FLOORS = listOf(1, 2, 3)

@Preview(showBackground = true, name = "22 학교 지도")
@Composable
private fun SchoolMapScreenPreview() {
    HopesTheme {
        SchoolMapScreen(
            buildingName = "본관 부근",
            accuracyMeters = 15,
            onBackClick = {},
            onRefreshClick = {},
        )
    }
}
