package com.ljb.data.datasource

import com.ljb.data.database.HolidayDao
import com.ljb.data.model.HolidayResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalHolidaySource {
    fun getHolidays(year: String): Flow<HolidayResponse>
    suspend fun insertHoliday(holidayResponse: HolidayResponse)
}

class LocalHolidaySourceImpl @Inject constructor(private val dao: HolidayDao) : LocalHolidaySource {
    override fun getHolidays(year: String): Flow<HolidayResponse> {
        return dao.getHolidays(year)
    }

    override suspend fun insertHoliday(holidayResponse: HolidayResponse) {
        dao.insertHoliday(holidayResponse)
    }
}