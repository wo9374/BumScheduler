package com.ljb.data.mapper

import com.ljb.data.model.KtorResponse
import com.ljb.domain.model.Holiday
import com.ljb.domain.model.status.ApiResult
import java.time.LocalDate

object ResponseMapper {
    fun responseToHoliday(response: ApiResult<KtorResponse>): ApiResult<List<Holiday>> {
        return when(response) {
            is ApiResult.Loading -> ApiResult.Loading
            is ApiResult.Success -> {
                val holiday = response.data.response.body.items.item.map {
                    Holiday(
                        localDate = LocalDate.parse(it.locdate),
                        dateName = it.dateName,
                        isHoliday = it.isHoliday == "Y"
                    )
                }
                ApiResult.Success(holiday)
            }
            is ApiResult.ApiError -> ApiResult.ApiError(response.message, response.code)
            is ApiResult.NetworkError -> ApiResult.NetworkError(response.throwable)
        }
    }
}