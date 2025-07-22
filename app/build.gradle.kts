plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

    id("com.google.devtools.ksp")
}

android {
    namespace = "com.saubh.solveitai"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.saubh.solveitai"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.ui:ui-text-google-fonts:1.8.3")

    implementation("com.airbnb.android:lottie-compose:6.6.7")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("com.github.jeziellago:compose-markdown:0.5.7")

    implementation("io.coil-kt.coil3:coil-compose:3.2.0")

    val roomVersion = "2.7.2"

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:${roomVersion}")
    ksp("androidx.room:room-compiler:$roomVersion")

    implementation("com.google.code.gson:gson:2.13.1")
}

kotlin {
    jvmToolchain(11)
}
