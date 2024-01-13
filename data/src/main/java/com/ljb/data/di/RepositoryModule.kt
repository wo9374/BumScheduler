package com.ljb.data.di

import com.ljb.data.datasource.LocalHolidaySourceImpl
import com.ljb.data.datasource.RemoteHolidaySourceImpl
import com.ljb.data.repository.LocalHolidayRepositoryImpl
import com.ljb.data.repository.RemoteHolidayRepositoryImpl
import com.ljb.domain.repository.LocalHolidayRepository
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
    fun provideRemoteHolidayRepository(holidayDataSourceImpl: RemoteHolidaySourceImpl): RemoteHolidayRepository =
        RemoteHolidayRepositoryImpl(holidayDataSourceImpl)

    @Provides
    @Singleton
    fun provideLocalHolidayRepository(localHolidaySourceImpl: LocalHolidaySourceImpl): LocalHolidayRepository =
        LocalHolidayRepositoryImpl(localHolidaySourceImpl)
}