package com.example.margumynas.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class TodoTask(
    val name: String,
    val date: Date,
    var status: MutableState<String>
)

object TodoRepository {
    private const val ONGOING_TEXT = "Daroma"

    @RequiresApi(Build.VERSION_CODES.O)
    val itemsList: MutableList<TodoTask> = mutableStateListOf(
        TodoTask(
            "Padaryti ND",
            Date.from(LocalDate.of(2024, 10, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            mutableStateOf(ONGOING_TEXT)
        ),
        TodoTask(
            "Apsipirkti",
            Date.from(LocalDate.of(2024, 10, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            mutableStateOf(ONGOING_TEXT)
        ),
        TodoTask(
            "Pasiruošti darbui",
            Date.from(LocalDate.of(2024, 9, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            mutableStateOf(ONGOING_TEXT)
        ),
        TodoTask(
            "Sutvarkyti namus",
            Date.from(LocalDate.of(2024, 10, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            mutableStateOf(ONGOING_TEXT)
        )
    )
}
