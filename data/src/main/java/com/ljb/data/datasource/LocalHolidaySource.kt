package com.ljb.data.datasource

import com.ljb.data.database.HolidayDao
import com.ljb.data.model.HolidayRoomEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalHolidaySource {
    fun getAllHolidays(): Flow<List<HolidayRoomEntity>>
    suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity)
}

class LocalHolidaySourceImpl @Inject constructor(private val dao: HolidayDao) : LocalHolidaySource {
    override fun getAllHolidays(): Flow<List<HolidayRoomEntity>> {
        return dao.getAllHolidays()
    }

    override suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity) {
        dao.insertHoliday(holidayRoomEntity)
    }
}