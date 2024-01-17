package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.domain.model.status.ApiResult
import com.ljb.domain.usecase.GetLocalHolidayUseCase
import com.ljb.domain.usecase.GetRemoteHolidayUseCase
import com.ljb.domain.usecase.InsertHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getRemoteHolidayUseCase: GetRemoteHolidayUseCase,
    private val getLocalHolidayUseCase: GetLocalHolidayUseCase,
    private val insertHolidayUseCase: InsertHolidayUseCase,
) : ViewModel() {

    private var _displayMonth = MutableStateFlow(LocalDate.now())
    val displayMonth get() = _displayMonth
    fun change(changeMonth: LocalDate) {
        _displayMonth.update { changeMonth }
    }


    private var _selectDate = MutableStateFlow(LocalDate.now())
    val selectDate get() = _selectDate
    fun select(selectDate: LocalDate) {
        _selectDate.update { selectDate }
    }

    val holidayList = getLocalHolidayUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getRemoteHoliday(solYear: String) {
        viewModelScope.launch {
            getRemoteHolidayUseCase(solYear).distinctUntilChanged().collect { result ->
                when (result) {
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        result.data.forEach {
                            insertHolidayUseCase(solYear, it)
                        }
                    }

                    is ApiResult.ApiError -> {}
                    is ApiResult.NetworkError -> {}
                }
            }
        }
    }
}