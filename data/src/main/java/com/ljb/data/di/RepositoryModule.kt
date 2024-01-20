package com.ljb.data.di

import com.ljb.data.datasource.LocalHolidaySourceImpl
import com.ljb.data.datasource.RemoteHolidaySourceImpl
import com.ljb.data.repository.HolidayRepositoryImpl
import com.ljb.domain.repository.HolidayRepository
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
    fun provideLocalHolidayRepository(
        localHolidaySourceImpl: LocalHolidaySourceImpl,
        remoteHolidaySourceImpl: RemoteHolidaySourceImpl
    ): HolidayRepository = HolidayRepositoryImpl(localHolidaySourceImpl, remoteHolidaySourceImpl)
}