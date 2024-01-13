package com.ljb.domain.repository

import com.ljb.domain.model.HolidayItem
import kotlinx.coroutines.flow.Flow

interface LocalHolidayRepository {
    fun getHolidays(year: String): Flow<HolidayItem>
    suspend fun insertHoliday(holidayItem: HolidayItem)
}