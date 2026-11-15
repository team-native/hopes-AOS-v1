package com.example.hopes.feature.chat.detail.view.answer

import org.junit.Assert.assertEquals
import org.junit.Test

class ChatAnswerBlocksTest {
    @Test
    fun plainAnswer_staysSingleParagraph() {
        assertEquals(
            listOf(ChatAnswerBlock.Paragraph("평일은 저녁 식사 후 자습, 점호 순서로 흘러가요.")),
            parseChatAnswerBlocks("평일은 저녁 식사 후 자습, 점호 순서로 흘러가요."),
        )
    }

    @Test
    fun markdownAnswer_splitsIntoBlocks() {
        val answer =
            """
            # Frontend 시작 순서
            처음에는 화면을 직접 만들어 보세요.
            1. HTML·CSS 익히기
            2. JavaScript 기초
            ```html
            <button>질문하기</button>
            ```
            > 막히면 선배에게 물어보세요.
            - 메모 하나
            """.trimIndent()

        assertEquals(
            listOf(
                ChatAnswerBlock.Heading("Frontend 시작 순서"),
                ChatAnswerBlock.Paragraph("처음에는 화면을 직접 만들어 보세요."),
                ChatAnswerBlock.NumberedList(listOf("HTML·CSS 익히기", "JavaScript 기초")),
                ChatAnswerBlock.Code(language = "html", code = "<button>질문하기</button>"),
                ChatAnswerBlock.Quote("막히면 선배에게 물어보세요."),
                ChatAnswerBlock.BulletList(listOf("메모 하나")),
            ),
            parseChatAnswerBlocks(answer),
        )
    }

    @Test
    fun blankLine_splitsParagraphs() {
        assertEquals(
            listOf(ChatAnswerBlock.Paragraph("첫 문단"), ChatAnswerBlock.Paragraph("둘째 문단")),
            parseChatAnswerBlocks("첫 문단\n\n둘째 문단"),
        )
    }

    @Test
    fun unclosedCodeFence_keepsRemainingLinesAsCode() {
        assertEquals(
            listOf(ChatAnswerBlock.Code(language = "", code = "val a = 1")),
            parseChatAnswerBlocks("```\nval a = 1"),
        )
    }
}
