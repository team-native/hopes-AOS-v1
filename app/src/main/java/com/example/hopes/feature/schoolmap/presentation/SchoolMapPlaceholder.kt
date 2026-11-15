package com.example.hopes.feature.schoolmap.presentation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.teamnative.hopes.R

/**
 * 실제 지도 대신 학교 건물 배치를 흰 블록으로 흉내 낸 자리 표시와 가운데 현재 위치 핀이다(피그마 21, 22).
 * 실내 도면을 받으면 이 자리를 실제 지도로 바꾼다.
 *
 * @param pinSize 위치 핀 원의 크기다. 채팅 카드에서는 작게, 학교 지도 화면에서는 크게 쓴다.
 */
@Composable
fun SchoolMapPlaceholder(
    modifier: Modifier = Modifier,
    pinSize: Dp = DEFAULT_PIN_SIZE,
) {
    val mapShape = RoundedCornerShape(AppRadius.ListCard)
    val pulse = rememberInfiniteTransition(label = "schoolMapPinPulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = PULSE_MAX_SCALE,
        animationSpec = infiniteRepeatable(animation = tween(PULSE_MILLIS), repeatMode = RepeatMode.Reverse),
        label = "schoolMapPinPulseScale",
    )
    val locationDescription = stringResource(R.string.school_map_current_location)

    BoxWithConstraints(
        modifier =
            modifier
                .clip(mapShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .semantics { contentDescription = locationDescription },
    ) {
        BUILDING_BLOCKS.forEach { block ->
            Box(
                modifier =
                    Modifier
                        .offset(x = maxWidth * block.x, y = maxHeight * block.y)
                        .size(width = maxWidth * block.width, height = maxHeight * block.height)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = BLOCK_ALPHA),
                            RoundedCornerShape(AppRadius.SmallButton),
                        ),
            )
        }

        Box(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(pinSize * HALO_RATIO)
                    .scale(pulseScale)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = HALO_ALPHA), CircleShape),
        )

        Box(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(pinSize)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(AppIconSize.Small),
            )
        }
    }
}

/** 지도 영역 대비 건물 블록의 위치·크기 비율이다. */
private data class BuildingBlock(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
)

// 피그마 21·22 지도 자리 표시의 건물 배치를 비율로 옮겼다.
private val BUILDING_BLOCKS =
    listOf(
        BuildingBlock(x = 0.05f, y = 0.06f, width = 0.22f, height = 0.14f),
        BuildingBlock(x = 0.32f, y = 0.04f, width = 0.18f, height = 0.24f),
        BuildingBlock(x = 0.56f, y = 0.08f, width = 0.36f, height = 0.12f),
        BuildingBlock(x = 0.05f, y = 0.28f, width = 0.2f, height = 0.2f),
        BuildingBlock(x = 0.6f, y = 0.32f, width = 0.3f, height = 0.22f),
        BuildingBlock(x = 0.06f, y = 0.6f, width = 0.32f, height = 0.24f),
        BuildingBlock(x = 0.46f, y = 0.66f, width = 0.24f, height = 0.2f),
        BuildingBlock(x = 0.76f, y = 0.64f, width = 0.18f, height = 0.26f),
    )

private val DEFAULT_PIN_SIZE = 28.dp
private const val HALO_RATIO = 2.4f
private const val HALO_ALPHA = 0.18f
private const val BLOCK_ALPHA = 0.85f
private const val PULSE_MAX_SCALE = 1.15f
private const val PULSE_MILLIS = 1_200
