package com.example.hopes.feature.community.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesHeaderIconButton
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 커뮤니티 화면의 "커뮤니티" 제목과 검색·내 활동 버튼이다. */
@Composable
fun CommunityHeader(
    onSearchClick: () -> Unit,
    onMyActivityClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        Text(
            text = stringResource(R.string.community_title),
            modifier =
                Modifier
                    .weight(1f)
                    .semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.PageTitle,
        )

        HopesHeaderIconButton(
            imageVector = Icons.Outlined.Search,
            contentDescription = stringResource(R.string.community_search),
            onClick = onSearchClick,
        )

        HopesHeaderIconButton(
            imageVector = Icons.Outlined.Person,
            contentDescription = stringResource(R.string.community_my_activity),
            onClick = onMyActivityClick,
        )
    }
}
