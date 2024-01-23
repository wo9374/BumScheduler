package com.ljb.domain.usecase

import com.ljb.domain.model.Holiday
import com.ljb.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestHolidayUseCase @Inject constructor(private val repository: HolidayRepository) {
    suspend operator fun invoke(reqYear: Int) {
        return repository.requestHolidays(reqYear)
    }
}

@Singleton
class GetHolidayUseCase @Inject constructor(private val repository: HolidayRepository){
    operator fun invoke(): Flow<List<Holiday>> {
        return repository.getAllHolidays()
    }
}