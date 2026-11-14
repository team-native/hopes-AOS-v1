package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import kotlinx.coroutines.delay

/** 화면 위쪽에 붙는 연한 빨간 배경의 오류 안내 줄이다(예: "저장하지 못했어요. 잠시 후 다시 시도해 주세요."). */
@Composable
fun HopesErrorBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.error.copy(alpha = ERROR_BANNER_ALPHA),
                    shape = RoundedCornerShape(AppRadius.Field),
                ).padding(horizontal = AppSpacing.Item, vertical = AppSpacing.Compact + AppSpacing.XSmall)
                .semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = HopesTextStyles.Caption,
        )
    }
}

/**
 * 화면 아래쪽에 잠깐 떴다 사라지는 어두운 알약 모양 안내다(예: "프로필을 저장했어요").
 * [message]가 바뀔 때마다 [durationMillis] 뒤에 [onDismiss]를 부른다.
 */
@Composable
fun HopesToast(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = DEFAULT_TOAST_DURATION_MILLIS,
) {
    val currentOnDismiss = rememberUpdatedState(onDismiss)

    LaunchedEffect(message) {
        delay(durationMillis)
        currentOnDismiss.value()
    }

    Row(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = CircleShape,
                ).padding(
                    horizontal = AppSpacing.Item + AppSpacing.Small,
                    vertical =
                        AppSpacing.Compact + AppSpacing.XSmall,
                ).semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = message,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = HopesTextStyles.Label,
        )
    }
}

private const val ERROR_BANNER_ALPHA = 0.08f
private const val DEFAULT_TOAST_DURATION_MILLIS = 2_000L
