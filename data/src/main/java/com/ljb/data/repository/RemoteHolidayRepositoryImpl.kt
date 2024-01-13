package com.ljb.data.repository

import com.ljb.data.datasource.RemoteHolidaySource
import com.ljb.data.mapper.mapperToHolidayItem
import com.ljb.domain.model.HolidayItem
import com.ljb.domain.model.status.ApiResult
import com.ljb.domain.repository.RemoteHolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteHolidayRepositoryImpl @Inject constructor(
    private val remoteHolidaySource: RemoteHolidaySource
) : RemoteHolidayRepository {

    override fun getHoliday(solYear: String, solMonth: String): Flow<ApiResult<HolidayItem>> =
        flow {
            remoteHolidaySource.getHoliday(solYear = solYear, solMonth = solMonth).run {
                val resultData = when(this) {
                    is ApiResult.Loading -> ApiResult.Loading
                    is ApiResult.Success -> ApiResult.Success(data.mapperToHolidayItem())
                    is ApiResult.ApiError -> ApiResult.ApiError(message, code)
                    is ApiResult.NetworkError -> ApiResult.NetworkError(throwable)
                }
                emit(resultData)
            }
        }
}