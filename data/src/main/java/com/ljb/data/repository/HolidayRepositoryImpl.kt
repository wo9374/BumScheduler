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
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
    private val localHolidaySource: LocalHolidaySource,
    private val remoteHolidaySource: RemoteHolidaySource
) : HolidayRepository {

    override suspend fun getAllHolidays(reqYear: Int): Flow<List<Holiday>> {
        return channelFlow {
            IntRange(reqYear - 1, reqYear + 1).forEach { year ->
                val localData = localHolidaySource.checkLocalHolidays(year)

                DlogUtil.d(MyTag, "Check RoomDB $year localData: $localData")

                if (localData.isEmpty()){
                    when (val remoteResult = remoteHolidaySource.getHoliday(year)) {
                        is ApiResult.Success -> {
                            // 리모트 데이터를 로컬 데이터베이스에 저장
                            remoteResult.data.forEach { remoteItem ->
                                localHolidaySource.insertHoliday(
                                    remoteItem.mapperToRoomEntity()
                                )
                            }
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

            localHolidaySource.getLocalHolidays(reqYear).collectLatest {  localList ->
                val roomList = localList.map {
                    it.mapperRoomEntityToHoliday()
                }
                send(roomList)
            }
        }
    }
}