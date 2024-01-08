package com.ljb.bumscheduler

import com.ljb.bumscheduler.base.Reducer
import com.ljb.bumscheduler.base.UiEvent
import com.ljb.bumscheduler.base.UiState
import java.time.LocalDate

class CalendarReducer(state: CalendarState) : Reducer<CalendarState, CalendarUiEvent>(state) {
    override fun reduce(oldState: CalendarState, event: CalendarUiEvent): CalendarState {
        return when (event) {
            is CalendarUiEvent.SelectDate -> {
                oldState.copy(selectedDate = event.selectedDate)
            }

            is CalendarUiEvent.ChangeMonth -> {
                oldState.copy(
                    displayDate = event.changeMonth,
                    //schedulerDay = getMonthDay(event.changeMonth)
                )
            }

            is CalendarUiEvent.ScrollToToday -> {
                oldState
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