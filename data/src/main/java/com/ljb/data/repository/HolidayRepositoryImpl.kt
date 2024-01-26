package com.ljb.data.repository

import com.ljb.data.DlogUtil
import com.ljb.data.MyTag
import com.ljb.data.datasource.LocalHolidaySource
import com.ljb.data.datasource.RemoteHolidaySource
import com.ljb.data.mapper.mapperRoomEntityToHoliday
import com.ljb.data.mapper.mapperToRoomEntity
import com.ljb.domain.model.Holiday
import com.ljb.domain.model.status.ApiResult
import com.ljb.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
    private val localHolidaySource: LocalHolidaySource,
    private val remoteHolidaySource: RemoteHolidaySource
) : HolidayRepository {

    override fun getAllHolidays(): Flow<List<Holiday>> {
        return localHolidaySource.getLocalHolidays().map { localList ->
            localList.map {
                it.mapperRoomEntityToHoliday()
            }
        }
    }

    override suspend fun requestHolidays(reqYear: Int) {
        IntRange(reqYear - 1, reqYear + 1).forEach { year ->
            val localData = localHolidaySource.checkLocalHolidays(year)

            DlogUtil.d(MyTag, "Check RoomDB $year localData: $localData")

            if (localData.isEmpty()) {
                when (val remoteResult = remoteHolidaySource.getHoliday(year)) {
                    is ApiResult.Success -> {
                        // 리모트 데이터를 로컬 데이터베이스에 저장
                        localHolidaySource.insertHoliday(
                            remoteResult.data.map {
                                it.mapperToRoomEntity()
                            }
                        )
                    }

                    is ApiResult.ApiError -> {
                        // 에러 처리 로직 추가
                    }

                    is ApiResult.NetworkError -> {
                        // 네트워크 에러 처리 로직 추가
                    }
                }
            }
        }
    }
}