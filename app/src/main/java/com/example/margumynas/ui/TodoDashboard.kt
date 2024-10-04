package com.example.margumynas.ui

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.testing.TestNavHostController
import com.example.margumynas.R.*
import com.example.margumynas.ui.theme.Shapes
import com.example.margumynas.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.*
import com.example.margumynas.data.TodoRepository
import com.example.margumynas.data.TodoTask
import com.example.margumynas.ui.components.TopBar

@Composable
fun TodoApp(onCreateTaskClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        TopBar()
        GreetingText()
        ListWrapper(onCreateTaskClick)
    }
}

@Composable
fun GreetingText(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(dimensionResource(dimen.padding_large))) {
        Text(
            text = stringResource(string.greeting),
            style = Typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(dimensionResource(dimen.padding_small)))

        Text(
            text = stringResource(string.greeting_name),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun ListWrapper(onCreateTaskClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<TodoTask?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(dimen.padding_large))
    ) {
        CreateTaskButton(onCreateTaskClick)

        Spacer(modifier = Modifier.padding(vertical = dimensionResource(dimen.padding_medium)))

        LazyColumn {
            items(TodoRepository.itemsList) { task ->
                ListItem(
                    task = task,
                    onDelete = {
                        taskToDelete = task
                        showDialog = true
                    }
                )
            }
        }

        if (showDialog) {
            DeleteConfirmationDialog(
                onDismiss = { showDialog = false },
                onConfirm = {
                    taskToDelete?.let { task ->
                        TodoRepository.itemsList.remove(task)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun CreateTaskButton(onCreateTaskClick: () -> Unit) {
    Button(
        onClick = onCreateTaskClick,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
        shape = Shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(dimen.button_height))
            .testTag(stringResource(string.create_task_button))
    ) {
        Text(
            text = stringResource(string.primary_button_text),
            style = Typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun DeleteConfirmationDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(string.dialogText)) },
        text = { Text(stringResource(string.dialog_question)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(string.delete))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(string.cancel))
            }
        }
    )
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ListItem(task: TodoTask, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy, HH:mm")
    val formattedDate = dateFormat.format(task.date)

    val ongoing = stringResource(string.ongoing)
    val complete = stringResource(string.complete)
    val late = stringResource(string.late)

    task.status.value = UpdateStatusIfLate(task.date, task.status.value, late)

    Card(
        onClick = onDelete,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(dimen.padding_small))
            .clip(Shapes.small)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = task.name,
                        style = Typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = modifier.padding(
                            start = dimensionResource(dimen.padding_extra_small),
                            end = dimensionResource(dimen.padding_extra_small),
                            top = dimensionResource(dimen.padding_extra_small),
                            bottom = dimensionResource(dimen.padding_extra_extra_small)
                        )
                    )
                    Text(text = formattedDate, modifier = modifier.padding(dimensionResource(dimen.padding_extra_small)), color = MaterialTheme.colorScheme.inverseSurface)
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        task.status.value = ChangeStatus(task.status.value, ongoing, complete, late)
                    },
                    colors = GetBgColorByStatus(task.status.value),
                    modifier = Modifier
                        .width(dimensionResource(dimen.status_button_width))
                        .height(dimensionResource(dimen.status_button_height))
                ) {
                    Text(text = task.status.value, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@VisibleForTesting
@Composable
internal fun GetBgColorByStatus(status: String): ButtonColors {
    return when (status) {
        stringResource(string.late) -> ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
        stringResource(string.ongoing) -> ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceVariant)
        else -> ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceBright)
    }
}

@VisibleForTesting
internal fun ChangeStatus(status: String, ongoing: String, complete: String, late: String): String {
    return when (status) {
        late -> status
        ongoing -> complete
        complete -> ongoing
        else -> ongoing
    }
}

@VisibleForTesting
internal fun UpdateStatusIfLate(date: Date, status: String, late: String): String {
    if (date.before(Date())) {
        return late
    }
    return status
}