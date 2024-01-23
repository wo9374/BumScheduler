package com.ljb.domain.repository

import com.ljb.domain.model.Holiday
import kotlinx.coroutines.flow.Flow

interface HolidayRepository {
    fun getAllHolidays(): Flow<List<Holiday>>
    suspend fun requestHolidays(reqYear: Int)
}