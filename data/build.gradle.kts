import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")

    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.ljb.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "DATA_API_KEY_ENCODE", gradleLocalProperties(rootDir).getProperty("data_api_key_encode"))
        buildConfigField("String", "DATA_API_KEY_DECODE", gradleLocalProperties(rootDir).getProperty("data_api_key_decode"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Lib.Dagger.Hilt.core)
    ksp(Lib.Dagger.Hilt.compiler)

    implementation(Lib.Ktor.core)
    implementation(Lib.Ktor.logging)
    implementation(Lib.Ktor.serializationJson)
    implementation(Lib.Ktor.contentNegotiation)

    implementation(Lib.Serialization.json)
}