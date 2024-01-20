package com.ljb.data.di

import android.content.Context
import androidx.room.Room
import com.ljb.data.database.HolidayDao
import com.ljb.data.database.HolidayDatabase
import com.ljb.data.datasource.LocalHolidaySource
import com.ljb.data.datasource.LocalHolidaySourceImpl
import com.ljb.data.datasource.RemoteHolidaySource
import com.ljb.data.datasource.RemoteHolidaySourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalHolidaySource(dao: HolidayDao): LocalHolidaySource = LocalHolidaySourceImpl(dao)

    @Provides
    @Singleton
    fun provideHolidayDataSource(httpClient: HttpClient): RemoteHolidaySource = RemoteHolidaySourceImpl(httpClient)
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