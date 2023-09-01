plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.crazyj36.watchfacetest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.crazyj36.watchfacetest"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("androidx.wear.compose:compose-material:1.2.0")
    implementation("androidx.wear.compose:compose-foundation:1.2.0")
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.wear.watchface:watchface:1.2.0-beta01")
    implementation("androidx.wear.watchface:watchface-complications:1.2.0-beta01")
    implementation("androidx.wear.watchface:watchface-editor:1.2.0-beta01")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.2.0-beta01")
    implementation("androidx.wear.watchface:watchface-complications-rendering:1.2.0-beta01")
}