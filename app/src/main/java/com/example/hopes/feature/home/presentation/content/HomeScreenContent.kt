package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.core.designsystem.component.FigmaBrandLogoShadow
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.LocalHopesExtendedColors

@Composable
fun HomeScreenContent(onStartChatClick: () -> Unit, onNavigate: (HopesDestination) -> Unit) {
    val extendedColors = LocalHopesExtendedColors.current
    HopesScaffold(HopesDestination.Home, onNavigate) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Image(painterResource(R.drawable.login_guide_background), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = AppSpacing.ScreenHorizontal, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
            ) {
                FigmaBrandHeader(isOnBlueBackground = true, logoShadow = FigmaBrandLogoShadow.Subtle)
                Text(stringResource(R.string.onboarding_title), color = Color.White, style = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold, lineHeight = 43.sp))
                Text(stringResource(R.string.onboarding_description), color = extendedColors.authDescription, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, lineHeight = 26.sp))
                HomeTipCard(1, R.string.onboarding_tip_one_top, R.string.onboarding_tip_one_bottom)
                HomeTipCard(2, R.string.onboarding_tip_two_top, R.string.onboarding_tip_two_bottom)
                Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).clickable(onClick = onStartChatClick).padding(14.dp), contentAlignment = Alignment.Center) { Text(stringResource(R.string.start_chat), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)) }
            }
        }
    }
}

@Composable
private fun HomeTipCard(index: Int, topRes: Int, bottomRes: Int) {
    val extendedColors = LocalHopesExtendedColors.current
    Row(Modifier.fillMaxWidth().figmaSubtleShadow(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)).padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
        Box(Modifier.background(extendedColors.onboardingStepContainer, RoundedCornerShape(12.dp)).padding(4.dp), contentAlignment = Alignment.Center) { Text(index.toString(), color = extendedColors.onboardingStepText) }
        Column { Text(stringResource(topRes), color = MaterialTheme.colorScheme.onSurfaceVariant, style = TextStyle(fontSize = 10.sp)); Text(stringResource(bottomRes), style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)) }
    }
}
