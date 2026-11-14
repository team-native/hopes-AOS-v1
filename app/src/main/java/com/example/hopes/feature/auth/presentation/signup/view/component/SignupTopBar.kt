package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthStepBackButton
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 회원가입 화면 상단의 뒤로가기 버튼과 "회원가입" 제목이다. */
@Composable
fun SignupTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(AppSize.TopBar)
                .padding(horizontal = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuthStepBackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = stringResource(R.string.signup),
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.TopBarTitle,
        )
    }
}
