package com.ljb.data.datasource

import com.ljb.data.BuildConfig
import com.ljb.data.DlogUtil
import com.ljb.data.MyTag
import com.ljb.data.model.HolidayResponse
import com.ljb.domain.model.status.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

interface RemoteHolidaySource {
    suspend fun getHoliday(solYear: Int): ApiResult<List<HolidayResponse>>
}


/**
 * Ktor 로 인한 인자 httpClient 바로 사용
 * Retrofit 은 여기서 Module 에서 Provides 로 받은 ApiInterface [Retrofit.create(Api::class.java)] 를 인자로 받음
 * */
class RemoteHolidaySourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : RemoteHolidaySource {

    override suspend fun getHoliday(solYear: Int): ApiResult<List<HolidayResponse>> {
        return try {
            httpClient.get("SpcdeInfoService/getHoliDeInfo") {
                parameter("ServiceKey", BuildConfig.DATA_API_KEY_DECODE)
                parameter("_type", "json")
                parameter("numOfRows", "100")
                parameter("solYear", solYear.toString())
                //parameter("solMonth", solMonth)
            }.run {
                if (status.isSuccess()) {
                    val holidayItem = deserializationJsonString(jsonString = bodyAsText())
                    ApiResult.Success(holidayItem)
                } else {
                    ApiResult.ApiError(status.description, status.value)
                }
            }

        } catch (e: Exception) {
            ApiResult.NetworkError(e)
        }
    }

    private fun deserializationJsonString(jsonString: String): List<HolidayResponse> {
        return try {
            // JSON 문자열을 JsonElement로 변환
            val jsonElement: JsonElement = Json.parseToJsonElement(jsonString)

            // 필요한 body 데이터에 접근
            val bodyJsonObject = jsonElement.jsonObject["response"]?.jsonObject?.get("body")?.jsonObject

            // HolidayResponse 클래스로 역직렬화
            val list = bodyJsonObject?.let {
                val itemsObject = it["items"]?.jsonObject
                val itemArray = itemsObject?.get("item")?.jsonArray

                itemArray?.map { element ->
                    Json.decodeFromJsonElement(element)
                }
            } ?: emptyList<HolidayResponse>()

            return list
        } catch (e: Exception) {
            // 예외 처리 - 역직렬화 실패 시 빈 리스트 반환
            e.printStackTrace()
            emptyList()
        }
    }
}