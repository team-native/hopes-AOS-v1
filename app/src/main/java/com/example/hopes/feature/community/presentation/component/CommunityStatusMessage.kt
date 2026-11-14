package com.example.hopes.feature.community.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles

/** 게시글 목록 대신 화면 가운데에 띄우는 원형 아이콘 + 제목 + 설명 + 작은 버튼 묶음이다(빈 화면, 불러오기 오류). */
@Composable
fun CommunityStatusMessage(
    icon: ImageVector,
    title: String,
    description: String,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val actionShape = RoundedCornerShape(AppRadius.SmallButton)

    Column(
        modifier = modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ICON_CIRCLE_SIZE)
                    .figmaRaisedShadow(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            // 아래 제목이 같은 상황을 설명하므로 아이콘은 장식으로 취급한다.
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(ICON_SIZE),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.DialogTitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Subtitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Box(
            modifier =
                Modifier
                    .height(AppSize.SmallButton)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = actionShape,
                    ).shapeClickable(shape = actionShape, role = Role.Button, onClick = onActionClick)
                    .padding(horizontal = AppSpacing.Item),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = actionText,
                color = MaterialTheme.colorScheme.onPrimary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

// 피그마 19 빈 화면의 원형 아이콘 배경과 아이콘 크기다.
private val ICON_CIRCLE_SIZE = 64.dp
private val ICON_SIZE = 28.dp
