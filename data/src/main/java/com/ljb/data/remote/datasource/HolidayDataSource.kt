package com.ljb.data.remote.datasource

import com.ljb.data.BuildConfig
import com.ljb.data.model.KtorResponse
import com.ljb.domain.model.status.ApiResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import javax.inject.Inject

object HolidayApiInfo{
    const val HOST = "apis.data.go.kr"
    const val PATH = "/B090041/openapi/service/"
}

interface HolidayDataSource {
    suspend fun getHoliday(solYear: String, solMonth: String): ApiResult<KtorResponse>
}


/**
 * Ktor 로 인한 인자 httpClient 바로 사용
 * Retrofit 은 여기서 Module 에서 Provides 로 받은 ApiInterface [Retrofit.create(Api::class.java)] 를 인자로 받음
 * */
class HolidayDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient
) : HolidayDataSource{
    override suspend fun getHoliday(solYear: String, solMonth: String): ApiResult<KtorResponse> {
        return try {
            httpClient.get("SpcdeInfoService/getHoliDeInfo") {
                parameter("ServiceKey", BuildConfig.DATA_API_KEY)
                parameter("_type", "json")
                parameter("numOfRows", "100")
                parameter("solYear", solYear)
                parameter("solMonth", solMonth)
            }.run {
                if (status.isSuccess()){
                    val response = body<KtorResponse>()
                    ApiResult.Success(response)
                }else{
                    ApiResult.ApiError(status.description, status.value)
                }
            }

        } catch (e: Exception) {
            ApiResult.NetworkError(e)
        }
    }
}