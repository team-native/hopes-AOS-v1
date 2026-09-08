import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

// release 서명 정보는 local.properties(git 제외)에서만 읽는다. 비밀번호는 절대
// BuildConfig로 앱 코드에 노출하지 않는다 — 이 Properties는 Gradle 스크립트
// 범위에서만 signingConfigs 연결에 쓰인다.
val releaseSigningProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}
val releaseStoreFile = releaseSigningProperties.getProperty("RELEASE_STORE_FILE")
val releaseStorePassword = releaseSigningProperties.getProperty("RELEASE_STORE_PASSWORD")
val releaseKeyAlias = releaseSigningProperties.getProperty("RELEASE_KEY_ALIAS")
val releaseKeyPassword = releaseSigningProperties.getProperty("RELEASE_KEY_PASSWORD")
val hasReleaseSigningConfig = !releaseStoreFile.isNullOrBlank() &&
    !releaseStorePassword.isNullOrBlank() &&
    !releaseKeyAlias.isNullOrBlank() &&
    !releaseKeyPassword.isNullOrBlank()

android {
    namespace = "com.example.hopes"
    // Compose dependencies require API 37 at compile time; runtime target remains unchanged.
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.hopes"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    signingConfigs {
        if (hasReleaseSigningConfig) {
            create("release") {
                storeFile = file(releaseStoreFile!!)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://service.gsmsv.site:22116/\"")
            buildConfigField("boolean", "ENABLE_LOG", "true")
            buildConfigField("int", "CONNECT_TIMEOUT_SECONDS", "20")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("String", "BASE_URL", "\"http://service.gsmsv.site:22116/\"")
            buildConfigField("boolean", "ENABLE_LOG", "false")
            buildConfigField("int", "CONNECT_TIMEOUT_SECONDS", "20")
            if (hasReleaseSigningConfig) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                logger.warn("RELEASE 서명 키 정보(local.properties)가 없어 release 빌드가 서명되지 않습니다.")
            }
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
