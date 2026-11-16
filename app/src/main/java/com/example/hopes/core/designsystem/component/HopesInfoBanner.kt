package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * 연한 파란 배경의 안내 줄이다(예: "학과와 전공을 고르면 나에게 맞는 질문을 추천해 드려요.").
 *
 * @param actionText 있으면 오른쪽에 "다시 시도" 같은 파란 글자 버튼을 붙인다.
 * @param icon 왼쪽 아이콘이다. 개인정보 안내처럼 뜻이 다르면 바꿔 넘긴다.
 */
@Composable
fun HopesInfoBanner(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(AppRadius.Banner),
                    // 피그마 Banner/Info: 좌우 16, 위아래 12.
                ).padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(BANNER_ICON_SIZE),
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_alert),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(BANNER_ICON_SIZE),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = message,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.Caption,
        )

        if (actionText != null) {
            Text(
                text = actionText,
                modifier =
                    Modifier
                        .clickable(role = Role.Button, onClick = onActionClick)
                        .padding(horizontal = AppSpacing.Compact, vertical = AppSpacing.Compact),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

private val BANNER_ICON_SIZE = 18.dp
