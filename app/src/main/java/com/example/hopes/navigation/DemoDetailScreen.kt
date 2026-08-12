package com.example.hopes.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaBrandHeader

/** 피그마 06·10·13·14 프레임을 목적별 상세 화면으로 제공한다. */
@Composable
fun DemoDetailScreen(
    destination: HopesDestination,
    question: String = "",
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    when (destination) {
        HopesDestination.ChatDetail -> FigmaChatDetailScreen(
            question = question,
            onBackClick = onBackClick,
            onNavigate = onNavigate,
        )
        HopesDestination.MyPage -> FigmaMyPageScreen(onBackClick = onBackClick, onNavigate = onNavigate)
        HopesDestination.PersonalSettings -> FigmaPersonalSettingsScreen(onBackClick = onBackClick, onNavigate = onNavigate)
        HopesDestination.Contact -> FigmaContactScreen(onBackClick = onBackClick, onNavigate = onNavigate)
        else -> Unit
    }
}

@Composable
private fun FigmaChatDetailScreen(
    question: String,
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    var replyText by rememberSaveable { mutableStateOf("") }
    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
    ) {
        FigmaBackHeader(
            title = stringResource(R.string.chat_detail_title),
            subtitle = stringResource(R.string.chat_answer_label),
            onBackClick = onBackClick,
            actionText = stringResource(R.string.chat_save),
        )
        FigmaBubble(
            text = question.ifBlank { stringResource(R.string.chat_detail_title) },
            modifier = Modifier.offset(x = 113.dp, y = 150.dp),
            isUser = true,
        )
        FigmaBubble(
            text = stringResource(R.string.chat_detail_answer),
            modifier = Modifier.offset(x = 24.dp, y = 232.dp),
            isUser = false,
        )
        FigmaReplyBar(
            value = replyText,
            onValueChange = { replyText = it },
        )
    }
}

@Composable
private fun FigmaMyPageScreen(onBackClick: () -> Unit, onNavigate: (HopesDestination) -> Unit) {
    var introductionText by rememberSaveable { mutableStateOf("") }
    FigmaAppFrame(selectedDestination = HopesDestination.Settings, onNavigate = onNavigate) {
        FigmaBrandTitle(
            title = stringResource(R.string.my_page),
            actionText = stringResource(R.string.navigation_settings),
        )
        FigmaCard(modifier = Modifier.offset(x = 24.dp, y = 194.dp).height(128.dp)) {
            Text(text = stringResource(R.string.my_page_account), modifier = Modifier.offset(x = 24.dp, y = 26.dp), style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
            Text(text = stringResource(R.string.my_page_email), modifier = Modifier.offset(x = 24.dp, y = 63.dp), style = TextStyle(fontSize = 13.sp))
            Text(text = stringResource(R.string.my_page_major), modifier = Modifier.offset(x = 24.dp, y = 87.dp), style = TextStyle(fontSize = 13.sp))
        }
        FigmaCard(modifier = Modifier.offset(x = 24.dp, y = 350.dp).height(326.dp)) {
            Text(text = stringResource(R.string.profile), modifier = Modifier.offset(x = 24.dp, y = 32.dp), style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            FigmaFieldLabel(text = stringResource(R.string.name), modifier = Modifier.offset(x = 24.dp, y = 80.dp))
            FigmaThinInput(value = "", onValueChange = {}, hint = stringResource(R.string.name), modifier = Modifier.offset(x = 24.dp, y = 101.dp))
            FigmaFieldLabel(text = stringResource(R.string.introduction) + " (AI 응답 개인화에 활용됩니다)", modifier = Modifier.offset(x = 24.dp, y = 172.dp))
            FigmaTextArea(value = introductionText, onValueChange = { introductionText = it }, hint = stringResource(R.string.profile_placeholder), height = 92, modifier = Modifier.offset(x = 24.dp, y = 204.dp))
            FigmaPrimaryButton(text = stringResource(R.string.save), modifier = Modifier.offset(x = 24.dp, y = 262.dp).width(96.dp).height(44.dp)) {}
        }
        FigmaBackButton(onBackClick = onBackClick)
    }
}

@Composable
private fun FigmaPersonalSettingsScreen(onBackClick: () -> Unit, onNavigate: (HopesDestination) -> Unit) {
    var promptText by rememberSaveable { mutableStateOf("") }
    FigmaAppFrame(selectedDestination = HopesDestination.Settings, onNavigate = onNavigate) {
        FigmaBackHeader(
            title = stringResource(R.string.personal_settings),
            subtitle = "AI 답변 스타일을 관리해요.",
            onBackClick = onBackClick,
        )
        FigmaCard(modifier = Modifier.offset(x = 24.dp, y = 158.dp).height(408.dp)) {
            Text(text = stringResource(R.string.personal_settings), modifier = Modifier.offset(x = 24.dp, y = 32.dp), style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            FigmaFieldLabel(text = stringResource(R.string.system_prompt) + " (AI 응답 생성 시 반영됩니다)", modifier = Modifier.offset(x = 24.dp, y = 80.dp))
            FigmaTextArea(value = promptText, onValueChange = { promptText = it }, hint = stringResource(R.string.prompt_placeholder), height = 210, modifier = Modifier.offset(x = 24.dp, y = 112.dp))
            FigmaPrimaryButton(text = stringResource(R.string.prompt_save), modifier = Modifier.offset(x = 230.dp, y = 341.dp).width(100.dp).height(46.dp)) {}
        }
    }
}

@Composable
private fun FigmaContactScreen(onBackClick: () -> Unit, onNavigate: (HopesDestination) -> Unit) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var contactText by rememberSaveable { mutableStateOf("") }
    FigmaAppFrame(selectedDestination = HopesDestination.Settings, onNavigate = onNavigate) {
        FigmaBackHeader(
            title = stringResource(R.string.contact),
            subtitle = "서비스 오류나 개선 의견을 보내요.",
            onBackClick = onBackClick,
        )
        FigmaCard(modifier = Modifier.offset(x = 24.dp, y = 156.dp).height(420.dp)) {
            FigmaFieldLabel(text = stringResource(R.string.email), modifier = Modifier.offset(x = 24.dp, y = 42.dp))
            FigmaThinInput(value = emailText, onValueChange = { emailText = it }, hint = stringResource(R.string.email), modifier = Modifier.offset(x = 24.dp, y = 67.dp))
            FigmaFieldLabel(text = stringResource(R.string.contact_content), modifier = Modifier.offset(x = 24.dp, y = 136.dp))
            FigmaTextArea(value = contactText, onValueChange = { contactText = it }, hint = stringResource(R.string.contact_placeholder), height = 154, modifier = Modifier.offset(x = 24.dp, y = 160.dp))
            FigmaPrimaryButton(text = stringResource(R.string.send_contact), modifier = Modifier.offset(x = 24.dp, y = 344.dp).width(306.dp).height(48.dp)) {}
        }
        FigmaCard(modifier = Modifier.offset(x = 24.dp, y = 624.dp).height(70.dp)) {
            Text(text = stringResource(R.string.contact_email_info), modifier = Modifier.offset(x = 24.dp, y = 25.dp), style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
private fun FigmaBrandTitle(title: String, actionText: String) {
    FigmaBrandHeader(modifier = Modifier.offset(x = 24.dp, y = 68.dp))
    Text(text = title, modifier = Modifier.offset(x = 24.dp, y = 138.dp), style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold))
    Text(text = actionText, modifier = Modifier.offset(x = 325.dp, y = 79.dp), color = MaterialTheme.colorScheme.primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
}

@Composable
private fun FigmaBackHeader(title: String, subtitle: String, onBackClick: () -> Unit, actionText: String? = null) {
    FigmaBackButton(onBackClick = onBackClick)
    Text(text = title, modifier = Modifier.offset(x = 74.dp, y = 76.dp), style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp))
    Text(text = subtitle, modifier = Modifier.offset(x = 74.dp, y = 111.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp))
    actionText?.let { text ->
        Box(modifier = Modifier.offset(x = 324.dp, y = 76.dp).width(54.dp).height(36.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Text(text = text, color = MaterialTheme.colorScheme.primary, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
private fun FigmaBackButton(onBackClick: () -> Unit) {
    Box(modifier = Modifier.offset(x = 19.dp, y = 74.dp).width(38.dp).height(38.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(13.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(13.dp)).clickable(onClick = onBackClick), contentAlignment = Alignment.Center) {
        Text(text = "‹", color = MaterialTheme.colorScheme.primary, style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun FigmaBubble(text: String, modifier: Modifier, isUser: Boolean) {
    Box(modifier = modifier.width(if (isUser) 265.dp else 354.dp).height(if (isUser) 58.dp else 110.dp).background(if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).border(if (isUser) 0.dp else 1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))) {
        Text(text = text, modifier = Modifier.offset(x = if (isUser) 16.dp else 20.dp, y = if (isUser) 18.dp else 18.dp).width(if (isUser) 230.dp else 296.dp), color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface, style = TextStyle(fontSize = 15.sp, fontWeight = if (isUser) FontWeight.SemiBold else FontWeight.Normal, lineHeight = 22.sp))
    }
}

@Composable
private fun FigmaReplyBar(value: String, onValueChange: (String) -> Unit) {
    Box(modifier = Modifier.offset(y = 716.dp).width(402.dp).height(74.dp).background(MaterialTheme.colorScheme.surface).border(1.dp, MaterialTheme.colorScheme.outline)) {
        FigmaThinInput(value = value, onValueChange = onValueChange, hint = stringResource(R.string.chat_additional_question), modifier = Modifier.offset(x = 24.dp, y = 16.dp).width(282.dp).height(44.dp))
        FigmaPrimaryButton(text = stringResource(R.string.chat_send), modifier = Modifier.offset(x = 320.dp, y = 16.dp).width(58.dp).height(44.dp)) {}
    }
}

@Composable
private fun FigmaCard(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.width(354.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))) { content() }
}

@Composable
private fun FigmaFieldLabel(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold))
}

@Composable
private fun FigmaThinInput(value: String, onValueChange: (String) -> Unit, hint: String, modifier: Modifier) {
    Box(modifier = modifier.width(306.dp).height(40.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))) {
        if (value.isEmpty()) Text(text = hint, modifier = Modifier.offset(x = 16.dp, y = 10.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, style = TextStyle(fontSize = 15.sp))
        BasicTextField(value = value, onValueChange = onValueChange, modifier = Modifier.offset(x = 16.dp, y = 9.dp).width(274.dp).height(24.dp), singleLine = true, textStyle = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface))
    }
}

@Composable
private fun FigmaTextArea(value: String, onValueChange: (String) -> Unit, hint: String, height: Int, modifier: Modifier) {
    Box(modifier = modifier.width(306.dp).height(height.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))) {
        if (value.isEmpty()) Text(text = hint, modifier = Modifier.offset(x = 16.dp, y = 18.dp).width(260.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, style = TextStyle(fontSize = 15.sp, lineHeight = 22.sp))
        BasicTextField(value = value, onValueChange = onValueChange, modifier = Modifier.offset(x = 16.dp, y = 14.dp).width(274.dp).height((height - 28).dp), textStyle = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 22.sp))
    }
}

@Composable
private fun FigmaPrimaryButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp)).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold))
    }
}
