plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.rigodev.tareasapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rigodev.tareasapp"
        minSdk = 23
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.13.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.airbnb.android:lottie:4.2.1")
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.opencsv:opencsv:5.6")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.camera:camera-camera2:1.3.4")
    implementation ("androidx.camera:camera-lifecycle:1.3.4")
    implementation ("androidx.camera:camera-view:1.3.4")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.zxing:core:3.4.1")

}