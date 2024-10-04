package com.example.margumynas.ui.components

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.example.margumynas.R
import java.util.Calendar

@SuppressLint("DefaultLocale")
@Composable
fun TimePickerModalInput(onTimeSelected: (String?) -> Unit, modifier: Modifier = Modifier) {
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    TextButton(onClick = { showTimePicker = true }, modifier = modifier
        .background(MaterialTheme.colorScheme.secondary)
        .fillMaxWidth()) {
        Text(stringResource(R.string.pick_time), color = MaterialTheme.colorScheme.onSecondary)
    }

    if (showTimePicker) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                onTimeSelected(selectedTime)
                showTimePicker = false
            },
            hour,
            minute,
            true,
        ).show()
    }

    selectedTime?.let { time ->
        SelectedDateAndTimeBox(
            text = stringResource(R.string.selected_time),
            value = time,
            modifier = modifier.testTag("SelectedTimeBox")
        )
    }
}