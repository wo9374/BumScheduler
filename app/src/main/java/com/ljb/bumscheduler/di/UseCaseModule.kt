package com.ljb.bumscheduler.di

import com.ljb.domain.repository.LocalHolidayRepository
import com.ljb.domain.repository.RemoteHolidayRepository
import com.ljb.domain.usecase.GetLocalHolidayUseCase
import com.ljb.domain.usecase.GetRemoteHolidayUseCase
import com.ljb.domain.usecase.InsertHolidayUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    fun provideGetRemoteHolidayUseCase(repository: RemoteHolidayRepository) = GetRemoteHolidayUseCase(repository)

    @Provides
    fun provideGetLocalHolidayUseCase(repository: LocalHolidayRepository) = GetLocalHolidayUseCase(repository)

    @Provides
    fun provideInsertHolidayUseCase(repository: LocalHolidayRepository) = InsertHolidayUseCase(repository)
}