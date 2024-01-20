package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.bumscheduler.base.UiEvent
import com.ljb.data.mapper.currentDate
import com.ljb.domain.model.Holiday
import com.ljb.domain.usecase.GetHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getHolidayUseCase: GetHolidayUseCase
) : ViewModel() {

    private val _holidayList = MutableStateFlow<List<Holiday>>(emptyList())
    val holidayList get() = _holidayList

    private val _calendarMonth = MutableStateFlow(currentDate)
    val calendarMonth get() = _calendarMonth

    private val _selectedDate = MutableStateFlow(currentDate)
    val selectDate get() = _selectedDate

    fun processEvent(event: CalendarEvent){
        when(event){

            is CalendarEvent.SwipeMonth -> {
                _calendarMonth.update {
                    event.changeMonth
                }
                _selectedDate.update {
                    val lastDay = event.changeMonth.lengthOfMonth()   // 변경될 월의 마지막 Day
                    val selectDay = selectDate.value.dayOfMonth       // 선택된 Day

                    // 선택 Day 가 변경된 월에 속하지 않을때
                    if (lastDay < selectDay){
                        event.changeMonth.withDayOfMonth(lastDay)    // 변경될 월의 마지막 Day 지정
                    } else {
                        event.changeMonth.withDayOfMonth(selectDay)  // 이전 선택 Day 를 변경될 월의 Day 로 지정
                    }
                }
            }

            is CalendarEvent.SelectDate -> {
                _selectedDate.update {
                    event.selectDate
                }
            }
        }
    }

    fun getData(reqYear: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getHolidayUseCase(reqYear).distinctUntilChanged().collectLatest { holidays ->
                _holidayList.update {
                    holidays
                }
            }
        }
    }

}

sealed class CalendarEvent : UiEvent {
    data class SwipeMonth(val changeMonth: LocalDate): CalendarEvent()
    data class SelectDate(val selectDate: LocalDate): CalendarEvent()
}