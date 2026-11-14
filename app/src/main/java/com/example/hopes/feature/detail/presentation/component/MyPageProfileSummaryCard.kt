package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 마이페이지 상단의 이름 첫 글자 아바타, 이름·학과·이메일, "프로필 수정" 버튼 카드다(피그마 31). */
@Composable
fun MyPageProfileSummaryCard(
    name: String,
    major: String?,
    email: String,
    onProfileEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)
    val buttonShape = RoundedCornerShape(AppRadius.Field)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = cardShape,
                ).padding(AppSpacing.Medium + AppSpacing.Small),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(AVATAR_SIZE)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                        ).clearAndSetSemantics {},
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = name.take(1),
                    color = MaterialTheme.colorScheme.primary,
                    style = HopesTextStyles.Headline,
                )
            }

            Spacer(modifier = Modifier.width(AppSpacing.Medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = HopesTextStyles.SectionTitle,
                )

                if (!major.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(AppSpacing.XSmall))

                    Text(
                        text = major,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = HopesTextStyles.Subtitle,
                    )
                }

                Text(
                    text = email,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(AppSize.SmallButton + AppSpacing.Small)
                    .border(
                        width = AppBorderWidth.Default,
                        color = MaterialTheme.colorScheme.outline,
                        shape = buttonShape,
                    ).shapeClickable(shape = buttonShape, role = Role.Button, onClick = onProfileEditClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.my_page_profile_edit),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

private val AVATAR_SIZE = 52.dp
