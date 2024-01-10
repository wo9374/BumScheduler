// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("com.android.library") version "8.1.4" apply false

    id("org.jetbrains.kotlin.android") version Versions.kotlin apply false
    id("com.google.devtools.ksp") version Versions.ksp apply false
    
    id("com.google.dagger.hilt.android") version Versions.hilt apply false


    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
    kotlin("plugin.serialization") version "1.9.21" apply false
}