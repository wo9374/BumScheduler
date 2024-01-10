package com.ljb.domain.repository

import com.ljb.domain.model.status.ApiResult
import com.ljb.domain.model.Holiday
import kotlinx.coroutines.flow.Flow

interface RemoteHolidayRepository {
    fun getHoliday(solYear: String, solMonth: String): Flow<ApiResult<List<Holiday>>>
}