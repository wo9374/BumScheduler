package com.ljb.data.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ljb.data.model.HolidayResponse
import kotlinx.coroutines.flow.Flow

@Database(entities = [HolidayResponse::class], version = 1, exportSchema = false)
@TypeConverters(HolidayResponse.Converters::class)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun dao(): HolidayDao
}

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holidays WHERE year = :year")
    fun getHolidays(year: String): Flow<HolidayResponse>

    @Insert
    suspend fun insertHoliday(holidayResponse: HolidayResponse)
    /*
        @Query("DELETE FROM holidays")
        suspend fun clearNumbers()*/
}