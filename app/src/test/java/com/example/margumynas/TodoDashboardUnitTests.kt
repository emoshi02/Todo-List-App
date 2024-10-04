package com.example.margumynas

import com.example.margumynas.ui.ChangeStatus
import com.example.margumynas.ui.UpdateStatusIfLate
import org.junit.Test

import org.junit.Assert.*
import java.util.Calendar

class TodoDashboardUnitTests {
    private val ongoing = "Daroma"
    private val complete = "Atlikta"
    private val late = "Vėluojama"
    private val unknown = "150njb"

    @Test
    fun changeStatusFromOngoingToComplete() {
        val actualStatus  = ChangeStatus(ongoing, ongoing, complete, late)
        assertEquals(complete, actualStatus)
    }

    @Test
    fun changeStatusFromCompleteToOngoing() {
        val actualStatus  = ChangeStatus(complete, ongoing, complete, late)
        assertEquals(ongoing, actualStatus)
    }

    @Test
    fun keepStatusIfLate() {
        val actualStatus = ChangeStatus(late, ongoing, complete, late)
        assertEquals(late, actualStatus)
    }

    @Test
    fun updateStatusIfDateLate() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2023)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val fixedDate = calendar.time

        val actualStatus = UpdateStatusIfLate(fixedDate, ongoing, late)
        assertEquals(late, actualStatus)
    }

    @Test
    fun keepStatusIfDateNotLate() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2038)
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val fixedDate = calendar.time

        val actualStatus = UpdateStatusIfLate(fixedDate, ongoing, late)
        assertEquals(ongoing, actualStatus)
    }

    @Test
    fun changeStatusFromUnknownToOngoing() {
        val actualStatus  = ChangeStatus(unknown, ongoing, complete, late)
        assertEquals(ongoing, actualStatus)
    }
}