package com.ljb.data.di

import com.ljb.data.remote.datasource.HolidayDataSourceImpl
import com.ljb.data.remote.repository.RemoteHolidayRepositoryImpl
import com.ljb.domain.repository.RemoteHolidayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun bindRemoteHolidayRepository(holidayDataSourceImpl: HolidayDataSourceImpl): RemoteHolidayRepository =
        RemoteHolidayRepositoryImpl(holidayDataSourceImpl)
}