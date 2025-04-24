plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.prueba_beat_on_jeans"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.prueba_beat_on_jeans"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.module.jackson.module.kotlin)
    implementation(libs.squareup.picasso)
    implementation(libs.osmdroid.android)
    implementation(libs.play.services.location.v2101)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.jetbrains.kotlinx.coroutines.android)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.cardstackview)
    implementation(libs.kotlinx.coroutines.android.v160)
    implementation(libs.okhttp)
    implementation(libs.picasso.v28)
    implementation("io.coil-kt:coil:2.0.0")
    implementation(libs.androidx.security.crypto)
    implementation(libs.bcprov.jdk15to18)
}