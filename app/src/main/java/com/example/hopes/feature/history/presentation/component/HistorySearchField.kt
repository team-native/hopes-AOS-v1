package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing

/** 입력이 멈추면 서버의 지난 대화 검색을 시작하는 검색 필드다. */
@Composable
fun HistorySearchField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    val searchLabel = stringResource(R.string.history_search)
    val fieldShape = RoundedCornerShape(AppRadius.Field)

    Box(
        modifier = Modifier
            .padding(
                start = AppSpacing.ScreenHorizontal,
                top = HISTORY_SEARCH_TOP_PADDING,
                end = AppSpacing.ScreenHorizontal,
            )
            .fillMaxWidth()
            .height(HISTORY_SEARCH_HEIGHT)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = fieldShape,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = fieldShape,
            ),
    ) {
        if (value.isEmpty()) {
            Text(
                text = searchLabel,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = HISTORY_SEARCH_HORIZONTAL_PADDING),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = HISTORY_SEARCH_HORIZONTAL_PADDING)
                .width(HISTORY_SEARCH_TEXT_WIDTH)
                .semantics { contentDescription = searchLabel },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )

        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = HISTORY_SEARCH_HORIZONTAL_PADDING),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private val HISTORY_SEARCH_TOP_PADDING = 157.dp
private val HISTORY_SEARCH_HEIGHT = 41.dp
private val HISTORY_SEARCH_HORIZONTAL_PADDING = 20.dp
private val HISTORY_SEARCH_TEXT_WIDTH = 270.dp
