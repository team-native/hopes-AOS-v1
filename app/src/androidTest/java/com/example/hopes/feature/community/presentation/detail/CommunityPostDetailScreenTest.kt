package com.example.hopes.feature.community.presentation.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class CommunityPostDetailScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val now = LocalDateTime.now()
    private val events = mutableListOf<CommunityPostDetailEvent>()
    private val post =
        CommunityPost(
            id = 1,
            cohort = 10,
            authorCohort = 10,
            title = "기숙사 와이파이 다들 잘 돼?",
            content = "3층 끝방인데 저녁만 되면 느려져.",
            createdAt = now.minusMinutes(10),
            likeCount = 12,
        )
    private val comment = CommunityComment(7, 1, 9, "사감 선생님께 말씀드리면 점검해 주신대!", now.minusMinutes(3))

    private fun setContent(uiState: CommunityPostDetailUiState) {
        composeRule.setContent {
            HopesTheme {
                CommunityPostDetailScreen(uiState = uiState, myCohort = 10, onEvent = { events += it })
            }
        }
    }

    @Test
    fun detail_showsPostCommentsAndBoardSubtitle() {
        setContent(CommunityPostDetailUiState(isLoading = false, post = post, comments = listOf(comment)))

        composeRule.onNodeWithText("10기 게시판").assertIsDisplayed()
        composeRule.onNodeWithText("기숙사 와이파이 다들 잘 돼?").assertIsDisplayed()
        composeRule.onNodeWithText("댓글 1").assertIsDisplayed()
        composeRule.onNodeWithText("사감 선생님께 말씀드리면 점검해 주신대!").assertIsDisplayed()
    }

    @Test
    fun detail_emitsLikeEvent() {
        setContent(CommunityPostDetailUiState(isLoading = false, post = post))

        composeRule.onNodeWithContentDescription("좋아요 12개").performClick()

        composeRule.runOnIdle { assertTrue(events.contains(CommunityPostDetailEvent.LikeClicked)) }
    }

    @Test
    fun detail_otherPersonsPostMoreSheetShowsReportOnly() {
        setContent(
            CommunityPostDetailUiState(isLoading = false, post = post, sheet = CommunityDetailSheet.PostMore),
        )

        composeRule.onNodeWithText("신고하기").assertIsDisplayed()
        composeRule.onNodeWithText("수정하기").assertDoesNotExist()
    }

    @Test
    fun detail_myPostMoreSheetShowsEditAndDelete() {
        setContent(
            CommunityPostDetailUiState(
                isLoading = false,
                post = post.copy(isMine = true),
                sheet = CommunityDetailSheet.PostMore,
            ),
        )

        composeRule.onNodeWithText("수정하기").assertIsDisplayed()
        composeRule.onNodeWithText("삭제하기").performClick()

        composeRule.runOnIdle { assertTrue(events.contains(CommunityPostDetailEvent.DeletePostClicked)) }
    }

    @Test
    fun detail_failedCommentShowsRetry() {
        setContent(
            CommunityPostDetailUiState(
                isLoading = false,
                post = post,
                failedComments = listOf(CommunityFailedComment(3, "3층 공용 공유기 쪽은 괜찮더라", now)),
            ),
        )

        composeRule.onNodeWithText("다시 시도").performClick()

        composeRule.runOnIdle {
            assertTrue(events.contains(CommunityPostDetailEvent.FailedCommentRetryClicked(3)))
        }
    }

    @Test
    fun detail_deletedPostShowsBackToList() {
        setContent(CommunityPostDetailUiState(isLoading = false, post = null))

        composeRule.onNodeWithText("삭제된 게시글이에요").assertIsDisplayed()
        composeRule.onNodeWithText("목록으로").performClick()

        composeRule.runOnIdle { assertTrue(events.contains(CommunityPostDetailEvent.BackToListClicked)) }
    }
}
