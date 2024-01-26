package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.bumscheduler.base.UiEvent
import com.ljb.data.mapper.currentDate
import com.ljb.domain.model.Holiday
import com.ljb.domain.usecase.GetHolidayUseCase
import com.ljb.domain.usecase.RequestHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val requestHolidayUseCase: RequestHolidayUseCase,
    private val getHolidayUseCase: GetHolidayUseCase,
) : ViewModel() {

    val holidayList = getHolidayUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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

                //변경된 Month의 이전 달과 다음 달 까지의 로컬 data 확인 후 네트워크 data 호출
                viewModelScope.launch(Dispatchers.IO) {
                    requestHolidayUseCase(event.changeMonth.year)
                }
            }

            is CalendarEvent.SelectDate -> {
                _selectedDate.update {
                    event.selectDate
                }
            }
        }
    }
}

sealed class CalendarEvent : UiEvent {
    data class SwipeMonth(val changeMonth: LocalDate): CalendarEvent()
    data class SelectDate(val selectDate: LocalDate): CalendarEvent()
}