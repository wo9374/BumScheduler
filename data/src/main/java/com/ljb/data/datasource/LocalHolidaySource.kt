package com.ljb.data.datasource

import com.ljb.data.database.HolidayDao
import com.ljb.data.model.HolidayRoomEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalHolidaySource {
    fun getHolidays(year: String): Flow<List<HolidayRoomEntity>>
    suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity)
}

class LocalHolidaySourceImpl @Inject constructor(private val dao: HolidayDao) : LocalHolidaySource {
    override fun getHolidays(year: String): Flow<List<HolidayRoomEntity>> {
        return dao.getHolidays(year)
    }

    override suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity) {
        dao.insertHoliday(holidayRoomEntity)
    }
}