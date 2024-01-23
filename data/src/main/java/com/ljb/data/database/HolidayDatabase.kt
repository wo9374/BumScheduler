package com.ljb.data.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ljb.data.model.HolidayRoomEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [HolidayRoomEntity::class], version = 1, exportSchema = false)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun dao(): HolidayDao

    companion object {
        @Volatile
        private var instance: HolidayDatabase? = null

        fun getInstance(context: Context): HolidayDatabase {
            return instance ?: kotlin.run {
                synchronized(this){
                    val tempInstance = Room.databaseBuilder(
                        context,
                        HolidayDatabase::class.java,
                        "holiday_db"
                    ).build()

                    instance = tempInstance
                    tempInstance
                }
            }
        }
    }
}

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holidays WHERE year = :reqYear")
    fun checkLocalHolidays(reqYear: Int): List<HolidayRoomEntity>

    @Query("SELECT * FROM holidays") // WHERE year IN (:reqYear - 1, :reqYear, :reqYear + 1)
    fun getLocalHolidays(): Flow<List<HolidayRoomEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) // ABORT - 트랙잭션 중 에러가 발생하면 중지하고 전부를 롤백, IGNORE - 에러를 만나도 무시하고 계속 진행
    suspend fun insertHoliday(vararg holidayRoomEntity: HolidayRoomEntity)
}