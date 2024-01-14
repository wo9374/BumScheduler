package com.ljb.domain.repository

import com.ljb.domain.model.Holiday
import com.ljb.domain.model.status.ApiResult
import kotlinx.coroutines.flow.Flow

interface RemoteHolidayRepository {
    fun getHoliday(solYear: String): Flow<ApiResult<List<Holiday>>>
}