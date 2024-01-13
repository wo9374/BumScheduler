package com.ljb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "holidays")
data class HolidayResponse(
    @PrimaryKey
    val year: String,
    val holidays: List<HolidayData>,
){
    @Serializable
    data class HolidayData(
        @SerialName("dateName") val dateName: String,
        @SerialName("isHoliday") val isHoliday: String,
        @SerialName("locdate") val locdate: Int
    ) {
        @kotlinx.serialization.Transient
        @SerialName("dateKind")
        val dateKind: String = ""

        @kotlinx.serialization.Transient
        @SerialName("seq")
        val seq: Int = 0
    }

    // TypeConverter를 사용하여 HolidayData를 문자열로 변환
    class Converters {
        @TypeConverter
        fun fromHolidayDataList(holidays: List<HolidayData>): String {
            return Json.encodeToString(holidays)
        }

        @TypeConverter
        fun toHolidayDataList(json: String): List<HolidayData> {
            return Json.decodeFromString(json)
        }
    }
}