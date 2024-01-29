plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.ljb.bumscheduler"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ljb.bumscheduler"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Compose.Implementation.ui)
    implementation(Compose.Implementation.graphics)
    implementation(Compose.Implementation.preview)
    implementation(Compose.Implementation.material3)
    debugImplementation(Compose.DebugImplementation.uiTooling)
    debugImplementation(Compose.DebugImplementation.uiTestManifest)
    androidTestImplementation(Compose.AndroidTestImplementation.junit4)

    implementation(LibCompose.Android.activity)

    implementation(LibCompose.Android.navigation)
    implementation(LibCompose.Android.hiltNavigation)

    implementation(Lib.Dagger.Hilt.core)
    ksp(Lib.Dagger.Hilt.compiler)

    //implementation(LibCompose.NaverMap.naverMap)
}