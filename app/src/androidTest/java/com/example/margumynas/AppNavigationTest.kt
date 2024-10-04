package com.example.margumynas

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput

class AppNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavController() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }

                AppNavigation(navController = navController)

        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun verifyCreateAppDestination() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.create_task_button)
        composeTestRule.onNodeWithTag(createTaskButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName("create_task_app")
    }

    @Test
    fun verifyStartDestination() {
        navController.assertCurrentRouteName("todo_app")
    }

    @Test
    fun navigateToCreateTaskScreen() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.create_task_button)
        composeTestRule.onNodeWithTag(createTaskButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName("create_task_app")
    }

    @Test
    fun navigateToDashboardScreen() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.create_task_button)
        val backArrowButton = composeTestRule.activity.getString(R.string.back_arrow_button)

        composeTestRule.onNodeWithTag(createTaskButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithTag(backArrowButton)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName("todo_app")
    }

    @Test
    fun navigateToDashboardOnTaskCreate() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.create_task_button)
        val taskTitleInput = composeTestRule.activity.getString(R.string.task_title_input)
        val createButtonText = composeTestRule.activity.getString(R.string.create_button)

        composeTestRule.onNodeWithTag(createTaskButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithTag(taskTitleInput)
            .assertIsDisplayed()
            .performTextInput("Naujas")

        composeTestRule.onNodeWithTag(createButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName("todo_app")
    }

    @Test
    fun stayOnDashboardOnTaskCreateIfTextFieldEmpty() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.create_task_button)
        val createButtonText = composeTestRule.activity.getString(R.string.create_button)

        composeTestRule.onNodeWithTag(createTaskButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithTag(createButtonText)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()
        navController.assertCurrentRouteName("create_task_app")
    }
}
