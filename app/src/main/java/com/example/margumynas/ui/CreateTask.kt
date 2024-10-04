package com.example.margumynas.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.margumynas.R
import com.example.margumynas.data.TodoRepository
import com.example.margumynas.data.TodoTask
import com.example.margumynas.ui.components.DatePickerModalInput
import com.example.margumynas.ui.components.TimePickerModalInput
import com.example.margumynas.ui.components.TopBar
import com.example.margumynas.ui.theme.Shapes
import com.example.margumynas.ui.theme.Typography
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskApp(onCreateTaskClick: () -> Unit, onNavigateBack: () -> Unit) {
    val onGoingStatus = stringResource(R.string.ongoing)
    val addTask: (String, Date) -> Unit = { title, date ->
        TodoRepository.itemsList.add(
            TodoTask(
                name = title,
                date = date,
                status = mutableStateOf(onGoingStatus)
            )
        )
        onCreateTaskClick()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        TopBar()
        NavBar(onNavigateBack)
        FormWrapper(addTask)
    }
}

@Composable
fun NavBar(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier
        .padding(dimensionResource(R.dimen.padding_medium_md))
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = navigateBack, modifier = modifier.testTag(stringResource(R.string.back_arrow_button))) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_arrow),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Text(
            text = stringResource(R.string.create_task),
            color = MaterialTheme.colorScheme.onPrimary,
            style = Typography.displaySmall,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun FormWrapper(onSubmitTaskClick: (String, Date) -> Unit, modifier: Modifier = Modifier) {
    var titleValue by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        TextFieldEdit(titleValue = titleValue, onTitleChange = { titleValue = it })
        DatePickerModalInput({ selectedDate = it })
        TimePickerModalInput({ selectedTime = it })
        Spacer(Modifier.padding(top = dimensionResource(R.dimen.padding_medium)))

        val taskDate = createTaskDate(selectedDate, selectedTime)

        TaskButton(onCreate = {onSubmitTaskClick(titleValue, taskDate)}, titleValue = titleValue)
    }
}

@Composable
fun TextFieldEdit(titleValue: String, onTitleChange: (String) -> Unit, modifier: Modifier = Modifier) {

    val keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Text,
    )

    TextField(
        value = titleValue,
        onValueChange = onTitleChange,
        label = { Text(stringResource(R.string.title_label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.extraSmall)
            .testTag(stringResource(R.string.task_title_input))
    )
}

@Composable
fun TaskButton(onCreate: () -> Unit, titleValue: String, modifier: Modifier = Modifier) {
    var isSubmitClicked by remember { mutableStateOf(false) }
    val isTitleEmpty = titleValue.isBlank()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                isSubmitClicked = true
                if (!isTitleEmpty) onCreate()
            },

            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceBright),
            modifier = modifier.width(dimensionResource(R.dimen.status_button_width)).testTag("createButton")
        ) {
            Text(stringResource(R.string.create))
        }
    }
    if (isSubmitClicked && isTitleEmpty) {
        ErrorMessage()
    }
}

@Composable
fun ErrorMessage(modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), Arrangement.Center) {
        Text(stringResource(R.string.error_message), color = MaterialTheme.colorScheme.error)
    }
}

@VisibleForTesting
internal fun createTaskDate(selectedDate: Long?, selectedTime: String?): Date {
    return if (selectedDate != null && selectedTime != null) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate

        val timeParts = selectedTime.split(":")
        val hours = timeParts[0].toIntOrNull() ?: 0
        val minutes = timeParts[1].toIntOrNull() ?: 0

        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        calendar.time
    } else {
        Date()
    }
}