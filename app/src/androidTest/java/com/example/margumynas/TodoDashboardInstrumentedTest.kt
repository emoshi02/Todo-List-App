package com.example.margumynas

import androidx.activity.ComponentActivity
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.margumynas.data.TodoRepository
import com.example.margumynas.data.TodoTask
import com.example.margumynas.ui.GetBgColorByStatus
import com.example.margumynas.ui.TodoApp
import org.junit.Rule
import org.junit.Test
import java.util.Date

class TodoDashboardInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun displaysGreeting() {
        val greetingText = composeTestRule.activity.getString(R.string.greeting)
        val greetingPerson = composeTestRule.activity.getString(R.string.greeting_name)

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = {})
        }

        composeTestRule
            .onNodeWithText(greetingText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(greetingPerson)
            .assertIsDisplayed()
    }

    @Test
    fun displayCreateTaskButton() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.primary_button_text)

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = {})
        }

        composeTestRule
            .onNodeWithText(createTaskButtonText)
            .assertIsDisplayed()
    }

    @Test
    fun createTaskButton_triggersAction() {
        val createTaskButtonText = composeTestRule.activity.getString(R.string.primary_button_text)
        var clicked = false

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = { clicked = true })
        }

        composeTestRule
            .onNodeWithText(createTaskButtonText)
            .performClick()

        assert(clicked)
    }

    @Test
    fun displayTask() {
        val testTask = TodoTask("1 Užduotis", Date(), mutableStateOf("Ongoing"))

        TodoRepository.itemsList.add(testTask)

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = {})
        }

        composeTestRule
            .onNodeWithText("1 Užduotis")
            .assertIsDisplayed()
    }

    @Test
    fun deleteFirstTask() {
        val dialogText = composeTestRule.activity.getString(R.string.dialogText)
        val dialogConfirm = composeTestRule.activity.getString(R.string.delete)

        val testTask = TodoTask("1 Užduotis", Date(), mutableStateOf("Ongoing"))

        TodoRepository.itemsList.add(testTask)

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = {})
        }

        composeTestRule
            .onNodeWithText("1 Užduotis")
            .performClick()

        composeTestRule
            .onNodeWithText(dialogText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(dialogConfirm)
            .performClick()

        composeTestRule
            .onNodeWithText("1 Užduotis")
            .assertDoesNotExist()

        TodoRepository.itemsList.clear()
    }


    @Test
    fun deleteMiddleTask() {
        val dialogText = composeTestRule.activity.getString(R.string.dialogText)
        val dialogConfirm = composeTestRule.activity.getString(R.string.delete)

        val testTasks = listOf(TodoTask("1 Užduotis", Date(), mutableStateOf("Ongoing")),
        TodoTask("2 Užduotis", Date(), mutableStateOf("Ongoing")),
        TodoTask("3 Užduotis", Date(), mutableStateOf("Ongoing")))

        TodoRepository.itemsList.addAll(testTasks)

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = {})
        }

        composeTestRule
            .onNodeWithText("2 Užduotis")
            .performClick()

        composeTestRule
            .onNodeWithText(dialogText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(dialogConfirm)
            .performClick()

        composeTestRule
            .onNodeWithText("2 Užduotis")
            .assertDoesNotExist()

        TodoRepository.itemsList.clear()
    }

    @Test
    fun deleteLastTask() {
        val dialogText = composeTestRule.activity.getString(R.string.dialogText)
        val dialogConfirm = composeTestRule.activity.getString(R.string.delete)

        val testTasks = listOf(TodoTask("1 Užduotis", Date(), mutableStateOf("Ongoing")),
            TodoTask("2 Užduotis", Date(), mutableStateOf("Ongoing")),
            TodoTask("3 Užduotis", Date(), mutableStateOf("Ongoing")))

        TodoRepository.itemsList.addAll(testTasks)

        composeTestRule.setContent {
            TodoApp(onCreateTaskClick = {})
        }

        composeTestRule
            .onNodeWithText("3 Užduotis")
            .performClick()

        composeTestRule
            .onNodeWithText(dialogText)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(dialogConfirm)
            .performClick()

        composeTestRule
            .onNodeWithText("3 Užduotis")
            .assertDoesNotExist()

        TodoRepository.itemsList.clear()
    }

    @Test
    fun changeButtonColorByStatus() {

        @Composable
        fun TestButtonColors(status: String): ButtonColors {
            return GetBgColorByStatus(status)
        }

        composeTestRule.setContent {
            val buttonColors = TestButtonColors("Completed")
            assert(buttonColors == ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceBright))
        }
    }
}