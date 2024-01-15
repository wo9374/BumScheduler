package com.ljb.data.repository

import com.ljb.data.datasource.LocalHolidaySource
import com.ljb.data.mapper.mapperToHoliday
import com.ljb.data.mapper.mapperToHolidayResponse
import com.ljb.domain.model.Holiday
import com.ljb.domain.repository.LocalHolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalHolidayRepositoryImpl @Inject constructor(private val dataSource: LocalHolidaySource) :
    LocalHolidayRepository {
    override fun getHolidays(year: String): Flow<List<Holiday>> {
        return dataSource.getHolidays(year).map { roomHolidayList ->
            roomHolidayList.map { it.mapperToHoliday() }
        }
    }

    override suspend fun insertHoliday(holiday: Holiday) {
        dataSource.insertHoliday(holiday.mapperToHolidayResponse())
    }
}