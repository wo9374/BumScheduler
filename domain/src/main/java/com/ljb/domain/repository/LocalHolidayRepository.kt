package com.ljb.domain.repository

import com.ljb.domain.model.Holiday
import kotlinx.coroutines.flow.Flow

interface LocalHolidayRepository {
    fun getAllHolidays(): Flow<List<Holiday>>
    suspend fun insertHoliday(holiday: Holiday)
}