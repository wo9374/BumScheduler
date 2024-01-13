package com.ljb.data.mapper

import com.ljb.data.model.HolidayResponse
import com.ljb.domain.model.HolidayItem
import java.time.LocalDate

fun HolidayResponse.mapperToHolidayItem(): HolidayItem {
    return HolidayItem(
        holidays = holidays.map {
            HolidayItem.Holiday(
                localDate = LocalDate.parse(it.locdate.toString(), formatString),
                dateName = it.dateName,
                isHoliday = it.isHoliday == "Y"
            )
        },
        year = year
    )
}

fun HolidayItem.mapperToHolidayResponse(): HolidayResponse {
    return HolidayResponse(
        holidays = holidays.map {
            HolidayResponse.HolidayData(
                locdate = it.localDate.format(formatString).toInt(),
                dateName = it.dateName,
                isHoliday = if (it.isHoliday) "Y" else "N"
            )
        },
        year = year
    )
}