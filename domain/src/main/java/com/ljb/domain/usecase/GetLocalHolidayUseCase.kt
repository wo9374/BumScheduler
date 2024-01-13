package com.ljb.domain.usecase

import com.ljb.domain.model.HolidayItem
import com.ljb.domain.repository.LocalHolidayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetLocalHolidayUseCase @Inject constructor (private val repository: LocalHolidayRepository) {
    operator fun invoke(year: String): Flow<HolidayItem> {
        return repository.getHolidays(year)
    }
}

@Singleton
class InsertHolidayUseCase @Inject constructor (private val repository: LocalHolidayRepository) {
    suspend operator fun invoke(holidayItem: HolidayItem) {
        repository.insertHoliday(holidayItem)
    }
}