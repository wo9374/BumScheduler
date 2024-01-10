package com.ljb.domain.model

import java.time.LocalDate

data class Holiday(
    val localDate: LocalDate,
    val dateName: String,
    val isHoliday: Boolean
)
