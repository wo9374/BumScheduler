package com.ljb.data.mapper

import com.ljb.data.model.HolidayData
import com.ljb.domain.model.HolidayItem
import com.ljb.domain.model.status.ApiResult
import java.time.LocalDate

object ResponseMapper {
    fun responseToHoliday(response: ApiResult<HolidayData>): ApiResult<HolidayItem> {
        return when(response) {
            is ApiResult.Loading -> ApiResult.Loading
            is ApiResult.Success -> {

                val list = response.data.holidays.map {
                    HolidayItem.Holiday(
                        localDate = LocalDate.parse(it.locdate.toString(), formatString),
                        dateName = it.dateName,
                        isHoliday = it.isHoliday == "Y"
                    )
                }

                val item = HolidayItem(
                    holidays = list,
                    year = response.data.year
                )
                ApiResult.Success(item)
            }
            is ApiResult.ApiError -> ApiResult.ApiError(response.message, response.code)
            is ApiResult.NetworkError -> ApiResult.NetworkError(response.throwable)
        }
    }
}