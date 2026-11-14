package com.example.hopes.feature.chat.detail.view.answer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * 문단으로 나눈 선배 답변을 피그마 08 모양대로 그린다(제목, 번호 목록, 코드 블록, 인용, 링크).
 *
 * @param onCopyCode 코드 블록 오른쪽 위 복사 버튼을 눌렀을 때 그 코드를 넘긴다.
 */
@Composable
fun ChatAnswerContent(
    blocks: List<ChatAnswerBlock>,
    onCopyCode: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        blocks.forEach { block ->
            when (block) {
                is ChatAnswerBlock.Heading -> {
                    Text(
                        text = block.text.toInlineAnnotatedString(),
                        modifier = Modifier.semantics { heading() },
                        color = textColor,
                        style = HopesTextStyles.SectionTitle,
                    )
                }

                is ChatAnswerBlock.Paragraph -> {
                    Text(
                        text = block.text.toInlineAnnotatedString(),
                        color = textColor,
                        style = ANSWER_BODY_STYLE,
                    )
                }

                is ChatAnswerBlock.NumberedList -> {
                    AnswerList(
                        items = block.items,
                        marker = { index -> "${index + 1}." },
                    )
                }

                is ChatAnswerBlock.BulletList -> {
                    AnswerList(
                        items = block.items,
                        marker = { BULLET_MARKER },
                    )
                }

                is ChatAnswerBlock.Code -> {
                    AnswerCodeBlock(
                        language = block.language,
                        code = block.code,
                        onCopyClick = { onCopyCode(block.code) },
                    )
                }

                is ChatAnswerBlock.Quote -> {
                    AnswerQuote(text = block.text)
                }
            }
        }
    }
}

@Composable
private fun AnswerList(
    items: List<String>,
    marker: (Int) -> String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small + AppSpacing.XSmall)) {
        items.forEachIndexed { index, item ->
            Row {
                Text(
                    text = marker(index),
                    modifier = Modifier.widthIn(min = LIST_MARKER_MIN_WIDTH),
                    color = MaterialTheme.colorScheme.primary,
                    style = ANSWER_BODY_STYLE.copy(fontWeight = FontWeight.SemiBold),
                )

                Text(
                    text = item.toInlineAnnotatedString(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = ANSWER_BODY_STYLE,
                )
            }
        }
    }
}

@Composable
private fun AnswerCodeBlock(
    language: String,
    code: String,
    onCopyClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(AppRadius.SmallButton),
                ).padding(start = AppSpacing.Item, bottom = AppSpacing.Item),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = language,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )

            Box(
                modifier =
                    Modifier
                        .size(AppSize.IconButton)
                        .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onCopyClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = stringResource(R.string.chat_copy_code),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AppIconSize.Inline),
                )
            }
        }

        Text(
            text = code,
            modifier = Modifier.padding(end = AppSpacing.Item),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Caption.copy(fontFamily = FontFamily.Monospace),
        )
    }
}

@Composable
private fun AnswerQuote(text: String) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Box(
            modifier =
                Modifier
                    .width(QUOTE_BAR_WIDTH)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = QUOTE_BAR_ALPHA)),
        )

        Text(
            text = text.toInlineAnnotatedString(),
            modifier = Modifier.padding(start = AppSpacing.Item - AppSpacing.XSmall),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Subtitle,
        )
    }
}

/** **굵게**, `코드`, [글자](주소) 링크만 알아보고 나머지는 그대로 둔다. */
@Composable
private fun String.toInlineAnnotatedString(): AnnotatedString {
    val linkColor = MaterialTheme.colorScheme.primary
    val codeBackground = MaterialTheme.colorScheme.background

    return buildInlineAnnotatedString(this, linkColor, codeBackground)
}

internal fun buildInlineAnnotatedString(
    text: String,
    linkColor: Color,
    codeBackground: Color,
): AnnotatedString =
    buildAnnotatedString {
        var cursor = 0
        inlinePattern.findAll(text).forEach { match ->
            append(text.substring(cursor, match.range.first))
            val (bold, code, linkLabel, linkUrl) = match.destructured
            when {
                bold.isNotEmpty() -> {
                    withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(bold) }
                }

                code.isNotEmpty() -> {
                    withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = codeBackground)) {
                        append(code)
                    }
                }

                else -> {
                    withLink(
                        LinkAnnotation.Url(
                            url = linkUrl,
                            styles =
                                TextLinkStyles(
                                    style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
                                ),
                        ),
                    ) {
                        append(linkLabel)
                    }
                }
            }
            cursor = match.range.last + 1
        }
        append(text.substring(cursor))
    }

private val inlinePattern = Regex("""\*\*(.+?)\*\*|`([^`]+)`|\[([^\]]+)]\((https?://[^)\s]+)\)""")
private val ANSWER_BODY_STYLE = HopesTextStyles.Body.copy(lineHeight = HopesTextStyles.Description.lineHeight)
private const val BULLET_MARKER = "•"
private val LIST_MARKER_MIN_WIDTH = 20.dp
private val QUOTE_BAR_WIDTH = 3.dp
private const val QUOTE_BAR_ALPHA = 0.3f
