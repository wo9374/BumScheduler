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
    @SerialName("seq") val seq: Int,

    @Transient
    @SerialName("remarks") val remarks: String = ""
)

@Entity(tableName = "holidays")
data class HolidayRoomEntity(
    @PrimaryKey val localDate: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val dateName: String,
    val isHoliday: Boolean,
) {
    /*class LocalDateConverters {
        @TypeConverter
        fun fromLocalDate(localDate: LocalDate?): Long? {
            return localDate?.toEpochDay()
        }

        @TypeConverter
        fun toLocalDate(epochDay: Long?): LocalDate? {
            return epochDay?.let { LocalDate.ofEpochDay(it) }
        }
    }*/
}