package com.ljb.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class HolidayData(
    val holidays: List<HolidayResponse>,
    val year: String,
)

@Serializable
data class HolidayResponse(
    @SerialName("dateName") val dateName: String,
    @SerialName("isHoliday") val isHoliday: String,
    @SerialName("locdate") val locdate: Int
) {
    @Transient
    @SerialName("dateKind")
    val dateKind: String = ""

    @Transient
    @SerialName("seq")
    val seq: Int = 0
}