package com.ljb.data.di

import android.util.Log
import com.ljb.data.DlogUtil
import com.ljb.data.MyTag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

object HolidayApiInfo {
    const val HOST = "apis.data.go.kr"
    const val PATH = "/B090041/openapi/service/"
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true        //응답 유효성 검사 true/false

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HolidayApiInfo.HOST
                    path(HolidayApiInfo.PATH)
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true          // Json string을 읽기 편하게 만들어줌
                        isLenient = true            // "" 따옴표 잘못된 건 무시하고 처리
                        encodeDefaults = true       // null 인 값도 json에 포함 시킴
                        ignoreUnknownKeys = true    // 모델에 없고, json에 있는 경우 해당 key 무시
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v(MyTag, message)
                    }
                }
                level = LogLevel.ALL
            }

            /*install(ResponseObserver) {
                onResponse { response ->
                    DlogUtil.d(
                        MyTag,
                        "HTTP status: ${response.status.value} ${response.bodyAsText()}"
                    )
                }
            }*/
        }
    }

}