package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 네트워크 오류로 로그인에 실패했을 때 화면 상단에 띄우는 어두운 안내 배너다. */
@Composable
fun AuthOfflineBanner(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = RoundedCornerShape(AppRadius.Field),
                ).padding(
                    horizontal = BANNER_HORIZONTAL_PADDING,
                    vertical = BANNER_VERTICAL_PADDING,
                ).semantics { liveRegion = LiveRegionMode.Polite },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.WifiOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.size(AppIconSize.Small),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = stringResource(R.string.auth_login_offline_message),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            style = HopesTextStyles.Caption.copy(fontWeight = FontWeight.Medium),
        )
    }
}

// 피그마 배너의 안쪽 여백이다. 다른 컴포넌트와 공유하지 않는 값이라 파일 안에 둔다.
private val BANNER_HORIZONTAL_PADDING = 14.dp
private val BANNER_VERTICAL_PADDING = 10.dp
