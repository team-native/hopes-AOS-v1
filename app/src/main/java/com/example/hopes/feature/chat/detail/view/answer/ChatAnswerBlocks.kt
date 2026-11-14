package com.example.hopes.feature.chat.detail.view.answer

/** 선배 답변을 문단 단위로 나눈 한 덩어리다(피그마 08 답변 문단 정리). */
sealed interface ChatAnswerBlock {
    data class Heading(
        val text: String,
    ) : ChatAnswerBlock

    data class Paragraph(
        val text: String,
    ) : ChatAnswerBlock

    data class NumberedList(
        val items: List<String>,
    ) : ChatAnswerBlock

    data class BulletList(
        val items: List<String>,
    ) : ChatAnswerBlock

    /** @param language 코드 블록 위에 작게 보여줄 언어 이름이다. 없으면 빈 문자열이다. */
    data class Code(
        val language: String,
        val code: String,
    ) : ChatAnswerBlock

    data class Quote(
        val text: String,
    ) : ChatAnswerBlock
}

/**
 * 서버가 보낸 마크다운 꼴 답변을 [ChatAnswerBlock] 목록으로 나눈다.
 * 제목(#), 번호 목록(1.), 글머리 목록(-, *), 코드 블록(```), 인용(>)만 알아보고 나머지 줄은 문단으로 묶는다.
 * 빈 줄은 문단을 끊는다. 마크다운이 아닌 평문 답변은 문단 하나로 그대로 나온다.
 */
fun parseChatAnswerBlocks(answer: String): List<ChatAnswerBlock> {
    val blocks = mutableListOf<ChatAnswerBlock>()
    val paragraphLines = mutableListOf<String>()
    val listItems = mutableListOf<String>()
    var listKind: ListKind? = null
    val lines = answer.replace("\r\n", "\n").lines()
    var index = 0

    fun flushParagraph() {
        if (paragraphLines.isNotEmpty()) {
            blocks += ChatAnswerBlock.Paragraph(paragraphLines.joinToString(separator = "\n"))
            paragraphLines.clear()
        }
    }

    fun flushList() {
        when (listKind) {
            ListKind.Numbered -> blocks += ChatAnswerBlock.NumberedList(listItems.toList())
            ListKind.Bullet -> blocks += ChatAnswerBlock.BulletList(listItems.toList())
            null -> Unit
        }
        listItems.clear()
        listKind = null
    }

    while (index < lines.size) {
        val line = lines[index]
        val trimmed = line.trim()

        when {
            trimmed.startsWith(CODE_FENCE) -> {
                flushParagraph()
                flushList()
                val language = trimmed.removePrefix(CODE_FENCE).trim()
                val codeLines = mutableListOf<String>()
                index++
                while (index < lines.size && !lines[index].trim().startsWith(CODE_FENCE)) {
                    codeLines += lines[index]
                    index++
                }
                blocks += ChatAnswerBlock.Code(language = language, code = codeLines.joinToString(separator = "\n"))
            }

            trimmed.isEmpty() -> {
                flushParagraph()
                flushList()
            }

            headingPattern.matches(trimmed) -> {
                flushParagraph()
                flushList()
                blocks += ChatAnswerBlock.Heading(trimmed.trimStart('#').trim())
            }

            trimmed.startsWith(QUOTE_PREFIX) -> {
                flushParagraph()
                flushList()
                val quoteLines = mutableListOf(trimmed.removePrefix(QUOTE_PREFIX).trim())
                while (index + 1 < lines.size && lines[index + 1].trim().startsWith(QUOTE_PREFIX)) {
                    index++
                    quoteLines += lines[index].trim().removePrefix(QUOTE_PREFIX).trim()
                }
                blocks += ChatAnswerBlock.Quote(quoteLines.joinToString(separator = "\n"))
            }

            numberedItemPattern.matches(trimmed) -> {
                flushParagraph()
                if (listKind != ListKind.Numbered) flushList()
                listKind = ListKind.Numbered
                listItems += trimmed.replaceFirst(numberedPrefixPattern, "")
            }

            bulletItemPattern.matches(trimmed) -> {
                flushParagraph()
                if (listKind != ListKind.Bullet) flushList()
                listKind = ListKind.Bullet
                listItems += trimmed.drop(BULLET_PREFIX_LENGTH).trim()
            }

            else -> {
                flushList()
                paragraphLines += trimmed
            }
        }
        index++
    }

    flushParagraph()
    flushList()
    return blocks
}

private enum class ListKind {
    Numbered,
    Bullet,
}

private const val CODE_FENCE = "```"
private const val QUOTE_PREFIX = ">"
private const val BULLET_PREFIX_LENGTH = 2
private val headingPattern = Regex("^#{1,6}\\s+.+")
private val numberedItemPattern = Regex("^\\d+[.)]\\s+.+")
private val numberedPrefixPattern = Regex("^\\d+[.)]\\s+")
private val bulletItemPattern = Regex("^[-*]\\s+.+")
