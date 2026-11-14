plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "io.github.floydswoop.skyear"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "io.github.floydswoop.skyear"
        minSdk = 24
        targetSdk = 37
        versionCode = 3
        versionName = "0.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
}
