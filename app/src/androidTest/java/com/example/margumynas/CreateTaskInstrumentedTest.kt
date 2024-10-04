package com.example.margumynas

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.margumynas.ui.CreateTaskApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateTaskAppTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            CreateTaskApp(
                onCreateTaskClick = {},
                onNavigateBack = {}
            )
        }
    }

    @Test
    fun displaysBackArrowButton() {
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.back_arrow_button))
            .assertIsDisplayed()
    }

    @Test
    fun displaysCreateButton() {
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.create_button))
            .assertIsDisplayed()
    }

    @Test
    fun displaysErrorWhenNoTextEntered() {
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.create_button))
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.error_message))
            .assertIsDisplayed()
    }

    @Test
    fun createTaskWhenTitleIsFilled() {
        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.task_title_input))
            .performTextInput("Naujas")

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.create_button))
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.error_message))
            .assertDoesNotExist()
    }

    @Test
    fun datePickerModalOpens() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.pick_date))
            .performClick()

        composeTestRule.onNodeWithTag(composeTestRule.activity.getString(R.string.datepicker))
            .assertIsDisplayed()
    }

    @Test
    fun datePickerModalDisplaysTheDate() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.pick_date))
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNode(hasTestTag(composeTestRule.activity.getString(R.string.calendar_date_picker)))
            .assertIsDisplayed()

        val locale = Locale.getDefault()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val dateFormat = SimpleDateFormat("yyyy 'm.' MMMM d 'd.,' EEEE", locale)
        val currentDate = calendar.time


        val formattedCurrentDate = dateFormat.format(currentDate)

        composeTestRule.onNodeWithText(formattedCurrentDate)
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.Submit))
            .assertIsDisplayed()
            .performClick()

        val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", locale)
        val formattedDisplayDate = displayDateFormat.format(currentDate)

        composeTestRule.onNodeWithText("Pasirinkta Data: $formattedDisplayDate")
            .assertIsDisplayed()
    }
}
