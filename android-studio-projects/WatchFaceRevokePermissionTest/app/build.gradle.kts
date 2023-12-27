plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.crazyj36.watchfacerevokepermissiontest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.crazyj36.watchfacerevokepermissiontest"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    implementation("androidx.wear:wear:1.3.0")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    compileOnly("com.google.android.wearable:wearable:2.9.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // updating these to 1.2.0 caused crash when revoking complication permission.
    implementation("androidx.wear.watchface:watchface:1.2.0")
    implementation("androidx.wear.watchface:watchface-complications:1.2.0")
    implementation("androidx.wear.watchface:watchface-complications-data-source:1.2.0")
    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.2.0")
    implementation("androidx.wear.watchface:watchface-editor:1.2.0")
    implementation("androidx.wear.watchface:watchface-complications-rendering:1.2.0")
}