package com.ljb.domain.usecase

import com.ljb.domain.model.Holiday
import com.ljb.domain.repository.LocalHolidayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLocalHolidayUseCase @Inject constructor(private val repository: LocalHolidayRepository) {
    operator fun invoke(): Flow<List<Holiday>> {
        return repository.getAllHolidays()
    }
}

@Singleton
class InsertHolidayUseCase @Inject constructor(private val repository: LocalHolidayRepository) {
    suspend operator fun invoke(holiday: Holiday) {
        repository.insertHoliday(holiday)
    }
}