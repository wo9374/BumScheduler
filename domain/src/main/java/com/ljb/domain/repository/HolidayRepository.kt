package com.ljb.domain.repository

import com.ljb.domain.model.Holiday
import kotlinx.coroutines.flow.Flow

interface HolidayRepository {
    suspend fun getAllHolidays(reqYear: Int): Flow<List<Holiday>>
}