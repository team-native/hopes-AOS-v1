package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 하위 화면 헤더 오른쪽의 흰색 글자 버튼("편집", "저장") 정보다. */
data class HopesHeaderAction(
    val text: String,
    val onClick: () -> Unit,
    val isEnabled: Boolean = true,
)

/**
 * 하단 탭 없이 뒤로가기 헤더로 시작하는 하위 화면 틀이다(알림, 프로필 수정, 개인 설정, 문의하기 등).
 * 본문은 세로 스크롤되고, [bottomButton]은 키보드가 올라오면 키보드 바로 위로 따라 올라간다.
 *
 * @param subtitle null이면 제목만 표시한다.
 * @param toast 화면 아래쪽, 하단 버튼 위에 겹쳐 띄울 안내다. 보통 [HopesToast]를 넘긴다.
 * @param scrollState 채팅처럼 본문을 코드에서 맨 아래로 스크롤해야 하는 화면은 직접 만든 상태를 넘긴다.
 */
@Composable
fun HopesSubScreenFrame(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    headerAction: HopesHeaderAction? = null,
    bottomButton: (@Composable () -> Unit)? = null,
    toast: (@Composable () -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
    ) {
        HopesSubScreenHeader(
            title = title,
            subtitle = subtitle,
            onBackClick = onBackClick,
            action = headerAction,
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(
                            start = AppSpacing.ScreenHorizontal,
                            top = AppSpacing.Item,
                            end = AppSpacing.ScreenHorizontal,
                            bottom = AppSpacing.Section,
                        ),
                content = content,
            )

            if (toast != null) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = AppSpacing.Item),
                ) {
                    toast()
                }
            }
        }

        if (bottomButton != null) {
            Box(
                modifier =
                    Modifier.padding(
                        start = AppSpacing.ScreenHorizontal,
                        end = AppSpacing.ScreenHorizontal,
                        bottom = AppSpacing.Item,
                    ),
            ) {
                bottomButton()
            }
        }
    }
}

@Composable
private fun HopesSubScreenHeader(
    title: String,
    subtitle: String?,
    onBackClick: () -> Unit,
    action: HopesHeaderAction?,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(AppSize.TopBar + AppSpacing.Compact)
                .padding(start = AppSpacing.Compact + AppSpacing.Small, end = AppSpacing.ScreenHorizontal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(AppSize.IconButton)
                    .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Spacer(modifier = Modifier.size(AppSpacing.Small))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = HopesTextStyles.TopBarTitle,
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        if (action != null) {
            val actionShape = RoundedCornerShape(AppRadius.SmallButton)

            Box(
                modifier =
                    Modifier
                        .height(AppSize.Chip)
                        .figmaSubtleShadow(actionShape)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = actionShape,
                        ).shapeClickable(
                            shape = actionShape,
                            enabled = action.isEnabled,
                            role = Role.Button,
                            onClick = action.onClick,
                        ).padding(horizontal = AppSpacing.Item),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = action.text,
                    color =
                        if (action.isEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    style = HopesTextStyles.LabelStrong,
                )
            }
        }
    }
}
