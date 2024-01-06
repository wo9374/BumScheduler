package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import com.ljb.bumscheduler.CalendarUiEvent
import com.ljb.bumscheduler.CalendarReducer
import com.ljb.bumscheduler.CalendarState
import java.time.LocalDate

class CalendarViewModel: ViewModel() {
    private val calendarReducer = CalendarReducer(CalendarState.init())

    val uiState get() = calendarReducer.uiState

    private fun sendEvent(event: CalendarUiEvent){
        calendarReducer.sendEvent(event)
    }

    fun changeMonth(changeMonth: LocalDate){
        sendEvent(CalendarUiEvent.ChangeMonth(changeMonth))
    }

    fun selectDate(selectDate: LocalDate){
        sendEvent(CalendarUiEvent.SelectDate(selectDate))
    }
}