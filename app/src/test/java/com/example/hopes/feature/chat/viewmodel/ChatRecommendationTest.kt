package com.example.hopes.feature.chat.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class ChatRecommendationTest {
    private val defaultTopics =
        listOf(ChatQuestionTopic.Dormitory, ChatQuestionTopic.Admission, ChatQuestionTopic.Advice)

    @Test
    fun departmentAndMajorSelected_addsMajorStudyQuestionThird() {
        assertEquals(
            listOf(
                ChatQuestionTopic.Dormitory,
                ChatQuestionTopic.Admission,
                ChatQuestionTopic.MajorStudy,
                ChatQuestionTopic.Advice,
            ),
            recommendedQuestionTopics(ChatDepartment.Software, ChatMajor.Frontend),
        )
    }

    @Test
    fun missingDepartmentOrMajor_returnsDefaultQuestions() {
        assertEquals(defaultTopics, recommendedQuestionTopics(null, ChatMajor.Frontend))
        assertEquals(defaultTopics, recommendedQuestionTopics(ChatDepartment.Software, null))
    }

    @Test
    fun otherMajor_returnsDefaultQuestions() {
        assertEquals(defaultTopics, recommendedQuestionTopics(ChatDepartment.Software, ChatMajor.Other))
    }

    @Test
    fun recommendationLoadError_fallsBackToDefaultQuestions() {
        val state =
            ChatUiState(
                department = ChatDepartment.Software,
                major = ChatMajor.Frontend,
                recommendationState = ChatRecommendationState.LoadError,
            )

        assertEquals(defaultTopics, state.recommendedTopics)
    }
}
