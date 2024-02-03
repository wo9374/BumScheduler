package com.ljb.data.datasource

import com.ljb.data.database.HolidayDao
import com.ljb.data.model.HolidayRoomEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalHolidaySource {
    fun getLocalHolidays(): Flow<List<HolidayRoomEntity>>
    fun checkLocalHolidays(reqYear: Int): List<HolidayRoomEntity>
    suspend fun insertHoliday(holidayRoomEntity: List<HolidayRoomEntity>)
    suspend fun clearHolidays()
}

class LocalHolidaySourceImpl @Inject constructor(private val dao: HolidayDao) : LocalHolidaySource {

    override fun getLocalHolidays(): Flow<List<HolidayRoomEntity>> {
        return dao.getLocalHolidays()
    }

    override fun checkLocalHolidays(reqYear: Int): List<HolidayRoomEntity> {
        return dao.checkLocalHolidays(reqYear)
    }

    override suspend fun insertHoliday(holidayRoomEntity: List<HolidayRoomEntity>) {
        dao.insertHoliday(*holidayRoomEntity.toTypedArray())
    }

    override suspend fun clearHolidays() {
        dao.clearHolidays()
    }
}