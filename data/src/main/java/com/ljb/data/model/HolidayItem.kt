package com.ljb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class HolidayResponse(
    @SerialName("dateKind") val dateKind: String,
    @SerialName("dateName") val dateName: String,
    @SerialName("isHoliday") val isHoliday: String,
    @SerialName("locdate") val locdate: Int,
    @SerialName("seq") val seq: Int
)

@Entity(tableName = "holidays")
data class HolidayRoomEntity(
    val year: String,
    @PrimaryKey val localDate: LocalDate,
    val dateName: String,
    val isHoliday: Boolean,
){
    class LocalDateConverters {
        @TypeConverter
        fun fromLocalDate(localDate: LocalDate?): Long? {
            return localDate?.toEpochDay()
        }

        @TypeConverter
        fun toLocalDate(epochDay: Long?): LocalDate? {
            return epochDay?.let { LocalDate.ofEpochDay(it) }
        }
    }
}