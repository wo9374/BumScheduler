package com.ljb.domain.repository

import com.ljb.domain.model.HolidayItem
import com.ljb.domain.model.status.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteHolidayRepository {
    fun getHoliday(solYear: String, solMonth: String): Flow<ApiResult<HolidayItem>>
}