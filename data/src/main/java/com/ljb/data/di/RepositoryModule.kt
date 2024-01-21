package com.ljb.data.di

import com.ljb.data.repository.HolidayRepositoryImpl
import com.ljb.domain.repository.HolidayRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHolidayRepository(impl: HolidayRepositoryImpl): HolidayRepository
}