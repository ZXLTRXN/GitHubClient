// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    id("org.jetbrains.kotlin.android") version Dependencies.kotlinVersion apply false
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Dependencies.hiltVersion}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Dependencies.navigationVersion}")
    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}