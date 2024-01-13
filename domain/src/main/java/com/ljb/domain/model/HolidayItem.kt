package com.ljb.domain.model

import java.time.LocalDate

data class HolidayItem(
    val holidays: List<Holiday>,
    val year: String,
) {
    data class Holiday(
        val localDate: LocalDate,
        val dateName: String,
        val isHoliday: Boolean
    )
}