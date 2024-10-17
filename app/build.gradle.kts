plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.tfg.loginsignupfirebasecompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tfg.loginsignupfirebasecompose"
        minSdk = 31
        targetSdk = 35
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    // Dependencia de Google Maps
    implementation (libs.gms.play.services.maps)

    // Dependencia para Jetpack Compose Maps
    implementation (
        libs.maps.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.pagingCompose)
    implementation(libs.dagger.hilt)
    implementation(libs.dagger.hilt.navigation)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.firebase.auth.ktx)
    implementation (libs.androidx.navigation.compose)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.play.services.location)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.coil.compose)
    implementation(libs.okhttp)


    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}