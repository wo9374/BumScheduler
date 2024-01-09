package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class CalendarViewModel: ViewModel() {
    private var _displayMonth = MutableStateFlow(LocalDate.now())
    val displayMonth get() = _displayMonth

    private var _selectDate = MutableStateFlow(LocalDate.now())
    val selectDate get() = _selectDate


    fun change(changeMonth: LocalDate){
        _displayMonth.update {
            changeMonth
        }
    }

    fun select(selectDate: LocalDate){
        _selectDate.update {
            selectDate
        }
    }
}