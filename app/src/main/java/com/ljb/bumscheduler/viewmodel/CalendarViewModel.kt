package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.data.DlogUtil
import com.ljb.data.MyTag
import com.ljb.domain.model.HolidayItem
import com.ljb.domain.model.status.ApiResult
import com.ljb.domain.usecase.GetHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getHolidayUseCase: GetHolidayUseCase,
): ViewModel() {

    private var _displayMonth = MutableStateFlow(LocalDate.now())
    val displayMonth get() = _displayMonth

    fun change(changeMonth: LocalDate){ _displayMonth.update { changeMonth } }


    private var _selectDate = MutableStateFlow(LocalDate.now())
    val selectDate get() = _selectDate

    fun select(selectDate: LocalDate){ _selectDate.update { selectDate } }


    private var _holiday: MutableStateFlow<List<HolidayItem.Holiday>> = MutableStateFlow(emptyList())
    val holiday get() = _holiday

    fun getHoliday(solYear: String, solMonth: String){
        viewModelScope.launch {
            getHolidayUseCase(solYear, solMonth).distinctUntilChanged().collect { result ->
                when(result){
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        _holiday.update {
                            DlogUtil.d(MyTag, "getHoliday Result ${result.data}")
                            result.data.holidays
                        }
                    }
                    is ApiResult.ApiError -> {}
                    is ApiResult.NetworkError -> {}
                }
            }
        }
    }
}