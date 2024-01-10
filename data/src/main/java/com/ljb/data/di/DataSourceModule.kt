package com.ljb.data.di

import com.ljb.data.remote.datasource.HolidayDataSource
import com.ljb.data.remote.datasource.HolidayDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideHolidayDataSource(httpClient: HttpClient): HolidayDataSource = HolidayDataSourceImpl(httpClient)
}