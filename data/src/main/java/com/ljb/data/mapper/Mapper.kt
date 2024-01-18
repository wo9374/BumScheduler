package com.ljb.data.mapper

import com.ljb.data.model.HolidayResponse
import com.ljb.data.model.HolidayRoomEntity
import com.ljb.domain.model.Holiday
import java.time.LocalDate

fun HolidayResponse.mapperResponseToHoliday(): Holiday {
    return Holiday(
        localDate = LocalDate.parse(locdate.toString(), formatString),
        dateName = dateName,
        isHoliday = isHoliday == "Y"
    )
}

fun HolidayRoomEntity.mapperRoomEntityToHoliday(): Holiday {
    return Holiday(
        localDate = localDate,
        dateName = dateName,
        isHoliday = isHoliday
    )
}

fun Holiday.mapperToHolidayResponse(): HolidayRoomEntity {
    return HolidayRoomEntity(
        localDate = localDate,
        isHoliday = isHoliday,
        dateName = dateName,
    )
}