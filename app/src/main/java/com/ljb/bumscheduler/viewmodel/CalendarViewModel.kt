package com.ljb.bumscheduler.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljb.data.DlogUtil
import com.ljb.data.MyTag
import com.ljb.domain.model.Holiday
import com.ljb.domain.usecase.GetLocalHolidayUseCase
import com.ljb.domain.usecase.GetRemoteHolidayUseCase
import com.ljb.domain.usecase.InsertHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getHolidayUseCase: GetRemoteHolidayUseCase,
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


    private lateinit var _holiday: MutableSharedFlow<List<Holiday>>
    val holiday get() = _holiday


    fun getHoliday(solYear: String) {
        viewModelScope.launch {
            getLocalHolidayUseCase(solYear).distinctUntilChanged().collect {
                DlogUtil.d(MyTag, "getHoliday Result $it")
            }

            /*getHolidayUseCase(solYear, solMonth).distinctUntilChanged().collect { result ->
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
            }*/
        }
    }
}