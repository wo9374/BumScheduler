package com.ljb.bumscheduler.di

import com.ljb.domain.repository.HolidayRepository
import com.ljb.domain.usecase.GetHolidayUseCase
import com.ljb.domain.usecase.RequestHolidayUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideRequestHolidayUseCase(repository: HolidayRepository) = RequestHolidayUseCase(repository)

    @Provides
    fun provideGetHolidayUseCase(repository: HolidayRepository) = GetHolidayUseCase(repository)
}