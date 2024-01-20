package com.ljb.bumscheduler.di

import com.ljb.domain.repository.HolidayRepository
import com.ljb.domain.usecase.GetHolidayUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {

    @Provides
    fun provideGetLocalHolidayUseCase(repository: HolidayRepository) = GetHolidayUseCase(repository)
}