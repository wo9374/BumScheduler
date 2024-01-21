package com.ljb.data.di

import android.content.Context
import androidx.room.Room
import com.ljb.data.database.HolidayDao
import com.ljb.data.database.HolidayDatabase
import com.ljb.data.datasource.LocalHolidaySource
import com.ljb.data.datasource.LocalHolidaySourceImpl
import com.ljb.data.datasource.RemoteHolidaySource
import com.ljb.data.datasource.RemoteHolidaySourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalHolidaySource(impl: LocalHolidaySourceImpl): LocalHolidaySource

    @Binds
    @Singleton
    abstract fun bindRemoteHolidaySource(impl: RemoteHolidaySourceImpl): RemoteHolidaySource

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideHolidayDatabase(@ApplicationContext appContext: Context): HolidayDatabase =
        Room.databaseBuilder(appContext, HolidayDatabase::class.java, "holiday_db").build()

    @Provides
    @Singleton
    fun provideHolidayDao(database: HolidayDatabase): HolidayDao = database.dao()
}