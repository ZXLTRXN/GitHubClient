plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version Dependencies.kotlinVersion
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.zxltrxn.githubclient"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.core:core-splashscreen:${Dependencies.splashVersion}")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.fragment:fragment-ktx:${Dependencies.fragmentVersion}")
    implementation("androidx.annotation:annotation:1.3.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Dependencies.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Dependencies.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Dependencies.lifecycleVersion}")

    implementation("com.google.android.material:material:1.6.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:${Dependencies.truthVersion}")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Dependencies.coroutineVersion}")

    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofitVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Dependencies.serializationVersion}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Dependencies.serializationConverterVersion}")

    implementation("com.google.dagger:hilt-android:${Dependencies.hiltVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${Dependencies.hiltVersion}")

    implementation("androidx.navigation:navigation-fragment-ktx:${Dependencies.navigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${Dependencies.navigationVersion}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${Dependencies.navigationVersion}")

    implementation("io.noties.markwon:core:${Dependencies.markwonVersion}")

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Dependencies.leakCanaryVersion}")
}