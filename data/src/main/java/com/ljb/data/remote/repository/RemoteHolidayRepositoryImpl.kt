package com.ljb.data.remote.repository

import com.ljb.data.mapper.ResponseMapper
import com.ljb.data.remote.datasource.HolidayDataSource
import com.ljb.domain.model.HolidayItem
import com.ljb.domain.model.status.ApiResult
import com.ljb.domain.repository.RemoteHolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteHolidayRepositoryImpl @Inject constructor(
    private val holidayDataSource: HolidayDataSource
) : RemoteHolidayRepository {

    override fun getHoliday(solYear: String, solMonth: String): Flow<ApiResult<HolidayItem>> =
        flow {
            holidayDataSource.getHoliday(solYear = solYear, solMonth = solMonth).run {
                emit(
                    ResponseMapper.responseToHoliday(this)
                )
            }
        }
}