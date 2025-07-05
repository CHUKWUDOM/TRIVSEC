package com.strathmore.trivseccomplete


import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith




class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun startQuizButton_isDisabled_whenUsernameIsBlank() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())

        }
        composeTestRule.onNodeWithText("Start Quiz").assertIsNotEnabled()
    }

    @Test
    fun enteringUsername_enableStartQuizButton() {
        composeTestRule.setContent {
            Home(navController = rememberNavController())
        }
        composeTestRule.onNodeWithText("Your Name").performTextInput("Andrew")
        composeTestRule.onNodeWithText("Start Quiz").assertIsEnabled()

    }

    @Test
    fun loadingState_ShowsProgressBarAndText() {

        composeTestRule.setContent {
            Quiz(navController = rememberNavController(), username = "Andrew")

        }


        composeTestRule.onNodeWithText("Loading questions...").assertExists()

        composeTestRule.onAllNodes(hasTestTag("Progress Indicator")).assertCountEquals(1)
    }
}