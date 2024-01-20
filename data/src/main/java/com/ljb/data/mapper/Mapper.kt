package com.ljb.data.mapper

import com.ljb.data.model.HolidayResponse
import com.ljb.data.model.HolidayRoomEntity
import com.ljb.domain.model.Holiday
import java.time.LocalDate

fun HolidayResponse.mapperToRoomEntity(): HolidayRoomEntity {
    val year = locdate / 10000
    val month = locdate % 10000 / 100
    val day = locdate % 100

    return HolidayRoomEntity(
        localDate = locdate,
        year = year,
        month = month,
        day = day,
        dateName = dateName,
        isHoliday = isHoliday == "Y"
    )
}

fun HolidayResponse.mapperToHoliday(): Holiday {
    val year = locdate / 10000
    val month = locdate % 10000 / 100
    val day = locdate % 100

    return Holiday(
        localDate = LocalDate.of(year, month, day),
        dateName = dateName,
        isHoliday = isHoliday == "Y"
    )
}

fun HolidayRoomEntity.mapperRoomEntityToHoliday(): Holiday {
    return Holiday(
        localDate = LocalDate.of(year, month, day),
        dateName = dateName,
        isHoliday = isHoliday
    )
}

fun Holiday.mapperToRoomEntity(): HolidayRoomEntity {
    val year = localDate.year * 10000
    val month = localDate.monthValue * 100
    val day = localDate.dayOfMonth

    return HolidayRoomEntity(
        localDate = year + month + day,
        year = year,
        month = month,
        day = day,
        dateName = dateName,
        isHoliday = isHoliday
    )
}