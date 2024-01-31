package com.ljb.data.di

import com.ljb.data.repository.DataStoreRepositoryImpl
import com.ljb.data.repository.HolidayRepositoryImpl
import com.ljb.domain.repository.DataStoreRepository
import com.ljb.domain.repository.HolidayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHolidayRepository(impl: HolidayRepositoryImpl): HolidayRepository

    @Binds
    abstract fun bindDataStoreRepository(impl: DataStoreRepositoryImpl): DataStoreRepository
}