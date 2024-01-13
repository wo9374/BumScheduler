package com.ljb.domain.usecase

import com.ljb.domain.repository.RemoteHolidayRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRemoteHolidayUseCase @Inject constructor(
    private val remoteHolidayRepository: RemoteHolidayRepository
) {
    operator fun invoke(solYear: String, solMonth: String) =
        remoteHolidayRepository.getHoliday(solYear, solMonth)
}