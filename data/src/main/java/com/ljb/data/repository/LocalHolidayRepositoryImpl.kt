package com.ljb.data.repository

import com.ljb.data.datasource.LocalHolidaySource
import com.ljb.data.mapper.mapperToHolidayItem
import com.ljb.data.mapper.mapperToHolidayResponse
import com.ljb.domain.model.HolidayItem
import com.ljb.domain.repository.LocalHolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalHolidayRepositoryImpl @Inject constructor(private val dataSource: LocalHolidaySource) :
    LocalHolidayRepository {
    override fun getHolidays(year: String): Flow<HolidayItem> {
        return dataSource.getHolidays(year).map {
            it.mapperToHolidayItem()
        }
    }

    override suspend fun insertHoliday(holidayItem: HolidayItem) {
        dataSource.insertHoliday(holidayItem.mapperToHolidayResponse())
    }
}