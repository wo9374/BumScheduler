package com.ljb.data.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ljb.data.model.HolidayResponse
import com.ljb.data.model.HolidayRoomEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [HolidayRoomEntity::class], version = 1, exportSchema = false)
@TypeConverters(HolidayRoomEntity.LocalDateConverters::class)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun dao(): HolidayDao
}

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holidays WHERE year = :year")
    fun getHolidays(year: String): Flow<List<HolidayRoomEntity>>

    @Insert
    suspend fun insertHoliday(holidayRoomEntity: HolidayRoomEntity)
}