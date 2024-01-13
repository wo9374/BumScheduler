package com.ljb.data.remote.datasource

import com.ljb.data.BuildConfig
import com.ljb.data.model.HolidayData
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
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

object HolidayApiInfo{
    const val HOST = "apis.data.go.kr"
    const val PATH = "/B090041/openapi/service/"
}

interface HolidayDataSource {
    suspend fun getHoliday(solYear: String, solMonth: String): ApiResult<HolidayData>
}


/**
 * Ktor 로 인한 인자 httpClient 바로 사용
 * Retrofit 은 여기서 Module 에서 Provides 로 받은 ApiInterface [Retrofit.create(Api::class.java)] 를 인자로 받음
 * */
class HolidayDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : HolidayDataSource{
    override suspend fun getHoliday(solYear: String, solMonth: String): ApiResult<HolidayData> {
        return try {
            httpClient.get("SpcdeInfoService/getHoliDeInfo") {
                parameter("ServiceKey", BuildConfig.DATA_API_KEY_DECODE)
                parameter("_type", "json")
                parameter("numOfRows", "100")
                parameter("solYear", solYear)
                parameter("solMonth", solMonth)
            }.run {
                if (status.isSuccess()){
                    val holidayItem = deserializationJsonString(
                        solYear = solYear,
                        jsonString = bodyAsText()
                    )
                    ApiResult.Success(holidayItem)
                }else{
                    ApiResult.ApiError(status.description, status.value)
                }
            }

        } catch (e: Exception) {
            ApiResult.NetworkError(e)
        }
    }

    private fun deserializationJsonString(solYear: String, jsonString: String) : HolidayData {
        // JSON 문자열을 JsonElement로 변환
        val jsonElement: JsonElement = Json.parseToJsonElement(jsonString)

        // 필요한 body 데이터에 접근
        val bodyJsonObject =
            jsonElement.jsonObject["response"]?.jsonObject?.get("body")?.jsonObject

        // HolidayItem 클래스로 역직렬화
        val holidayItem = bodyJsonObject?.let {
            val itemElement = it["items"]?.jsonObject?.get("item")  //items 안 [] jsonObject 감싸져 있음
            val holidayList = itemElement?.let { element ->
                Json.decodeFromJsonElement(element)
            } ?: listOf<HolidayResponse>()

            HolidayData(holidayList, solYear)
        } ?: HolidayData(emptyList(), solYear)

        return holidayItem
    }
}