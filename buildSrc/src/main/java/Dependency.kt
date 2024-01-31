object Versions {
    const val kotlin = "1.9.20"

    /**
     * 사용시 코틀린 버전과 같거나 이하여야 함
     *
     * (:project)
     * id("com.google.devtools.ksp") version Versions.ksp apply false
     *
     * (:app) plugins
     * id("com.google.devtools.ksp")
     * */
    const val ksp = "1.9.20-1.0.14"

    const val hilt = "2.49"
    const val koin = "3.4.0"

    const val dataStore = "1.0.0"
    const val room = "2.6.1"

    const val livedata = "2.6.2"
    const val viewmodel = "2.6.2"
    const val paging = "3.2.1"
    const val navigation = "2.5.3"

    const val ktor = "2.3.6"
    const val retrofit = "2.9.0"
    const val okHttp = "4.11.0"
    const val orBit = "6.1.0"

    const val protoBufPlugin = "0.9.1"
    const val protoBuf = "3.25.1"
    const val glide = "4.14.2"
}

object Kotlin {
    const val coroutineVersion = "1.7.1"

    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
}

object Java {
    const val javax = "javax.inject:javax.inject:1"
}

object Lib {

    object Android {
        const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val activity = "androidx.activity:activity-ktx:1.8.1"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.livedata}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodel}"
        const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    }

    object DataStore {
        const val preferencesStore = "androidx.datastore:datastore-preferences:${Versions.dataStore}"
        const val protoStore = "androidx.datastore:datastore:${Versions.dataStore}"

        //Android 종속성 없이 사용 (Multi 모듈용)
        const val preferencesCore = "androidx.datastore:datastore-preferences-core:${Versions.dataStore}"
        const val protoCore = "androidx.datastore:datastore-core:1.0.0:${Versions.dataStore}"
    }

    object Room {
        const val core = "androidx.room:room-runtime:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-android:${Versions.ktor}"
        const val cio = "io.ktor:ktor-client-cio:${Versions.ktor}"                  //JVM, Android 및 Native 플랫폼에서 사용할 수 있는 완전 비동기식 코루틴 기반 엔진
        const val logging = "io.ktor:ktor-client-logging-jvm:${Versions.ktor}"      //HTTP Request을 로깅하기 위해 사용
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"  //직렬화/역직렬화를 위한 ContentNegotiation
        const val serializationJson = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"   //Json 직렬화
        const val serializationXml = "io.ktor:ktor-serialization-kotlinx-xml:${Versions.ktor}"     //XML 직렬화
    }

    object Serialization {
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2"      //Entity 직렬화
    }

    object Retrofit {
        const val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"  //Gson 처리
        const val jaxb = "com.squareup.retrofit2:converter-jaxb:${Versions.retrofit}"  //XML 처리
        const val scalars = "com.squareup.retrofit2:converter-scalars:${Versions.retrofit}" //String 처리
    }

    object OkHttp {
        const val core = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
        const val interceptor =
            "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}" //요청, 응답 정보 기록
    }

    object Orbit {
        const val core = "org.orbit-mvi:orbit-core:${Versions.orBit}"
        const val viewModel = "org.orbit-mvi:orbit-viewmodel:${Versions.orBit}"
    }

    object Dagger {
        object Hilt {
            const val core = "com.google.dagger:hilt-android:${Versions.hilt}"
            const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        }

        object Koin {
            const val core = "io.insert-koin:koin-android:${Versions.koin}"
        }
    }

    object Google {

        object ProtoBuf {
            const val kotlin = "com.google.protobuf:protobuf-kotlin-lite:${Versions.protoBuf}"
            const val java = "com.google.protobuf:protobuf-javalite:${Versions.protoBuf}"
            const val protoCompiler = "com.google.protobuf:protoc:${Versions.protoBuf}"  //protobuf protoc { artifacit = this }
        }

        object Glide {
            const val core = "com.github.bumptech.glide:glide:${Versions.glide}"
            const val compilerKapt = "com.github.bumptech.glide:compiler:${Versions.glide}"
            const val compilerKsp = "com.github.bumptech.glide:ksp:${Versions.glide}"
        }
    }
}


object Compose {
    const val composeVersion = "1.5.4"
    const val materialVersion = "1.1.2"

    object Implementation {
        const val ui = "androidx.compose.ui:ui:$composeVersion"
        const val graphics = "androidx.compose.ui:ui-graphics:$composeVersion"
        const val preview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
        const val material3 = "androidx.compose.material3:material3:$materialVersion"
    }

    object AndroidTestImplementation {
        const val junit4 = "androidx.compose.ui:ui-test-junit4:$composeVersion"
    }

    object DebugImplementation {
        const val uiTooling = "androidx.compose.ui:ui-tooling:$composeVersion"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$composeVersion"
    }
}

object LibCompose {
    const val hiltNavigationVersion = "1.0.0"

    /**
     * https://github.com/fornewid/naver-map-compose
     *
     * composeVersion에 맞는 NaverMap 버전 사용
     * */
    const val naverMapVersion = "1.4.1"

    object Android {
        const val navigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
        const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:$hiltNavigationVersion"
        const val paging = "androidx.paging:paging-compose:${Versions.paging}"
        const val activity = "androidx.activity:activity-compose:1.8.2"
    }

    object NaverMap {
        const val naverMap = "io.github.fornewid:naver-map-compose:$naverMapVersion"
    }
}