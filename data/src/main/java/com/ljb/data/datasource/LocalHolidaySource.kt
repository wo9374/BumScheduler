package com.ljb.data.datasource

import com.ljb.data.database.HolidayDao
import com.ljb.data.model.HolidayRoomEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalHolidaySource {
    fun checkLocalHolidays(reqYear: Int): List<HolidayRoomEntity>
    fun getLocalHolidays(reqYear: Int): Flow<List<HolidayRoomEntity>>
    suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity)
}

class LocalHolidaySourceImpl @Inject constructor(private val dao: HolidayDao) : LocalHolidaySource {
    override fun checkLocalHolidays(reqYear: Int): List<HolidayRoomEntity> {
        return dao.checkLocalHolidays(reqYear)
    }

    override fun getLocalHolidays(reqYear: Int): Flow<List<HolidayRoomEntity>> {
        return dao.getLocalHolidays(reqYear)
    }

    override suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity) {
        dao.insertHoliday(holidayRoomEntity)
    }
}