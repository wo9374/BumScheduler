package com.ljb.bumscheduler

import com.ljb.bumscheduler.base.Reducer
import com.ljb.bumscheduler.base.UiEvent
import com.ljb.bumscheduler.base.UiState
import java.time.LocalDate

class CalendarReducer(state: CalendarState) : Reducer<CalendarState, CalendarUiEvent>(state) {
    override fun reduce(oldState: CalendarState, event: CalendarUiEvent) {
        when (event) {
            is CalendarUiEvent.SelectDate -> {
                val newState = oldState.copy(selectedDate = event.selectedDate)
                setState(newState)
            }

            is CalendarUiEvent.ChangeMonth -> {
                val newState = oldState.copy(
                    displayDate = event.changeMonth,
                    //schedulerDay = getMonthDay(event.changeMonth)
                )
                setState(newState)
            }

            is CalendarUiEvent.ScrollToToday -> {

            }
        }
    }
}

sealed class CalendarUiEvent : UiEvent {
    data class SelectDate(val selectedDate: LocalDate) : CalendarUiEvent()
    data class ChangeMonth(val changeMonth: LocalDate) : CalendarUiEvent()
    object ScrollToToday : CalendarUiEvent()
}

data class CalendarState(
    val displayDate: LocalDate,
    //val schedulerDay: List<LocalDate>,
    val selectedDate: LocalDate
) : UiState {
    companion object {
        fun init() = CalendarState(
            displayDate = LocalDate.now(),
            //schedulerDay = getMonthDay(LocalDate.now()),
            selectedDate = LocalDate.now()
        )
    }
}