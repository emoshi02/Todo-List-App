package com.example.margumynas

import com.example.margumynas.ui.createTaskDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date
import java.util.Calendar

class CreateTaskUnitTests {
    @Test
    fun returnCurrentDateIfDateIsNull() {
        val selectedDate = null
        val selectedTime = "10:30"

        val result = createTaskDate(selectedDate, selectedTime)

        assertEquals(Date(), result)
    }

    @Test
    fun returnCurrentDateAndCurrentTimeIfTimeIsNull() {
        val selectedDate = Calendar.getInstance().timeInMillis
        val selectedTime = null
        val result = createTaskDate(selectedDate, selectedTime)

        assertEquals(Date(), result)
    }

    @Test
    fun combineTaskDateAndDay() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val selectedDate = calendar.timeInMillis
        val selectedTime = "10:30"

        val result = createTaskDate(selectedDate, selectedTime)

        val expectedCalendar = Calendar.getInstance().apply {
            timeInMillis = selectedDate
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        assertEquals(expectedCalendar.time, result)
    }
}