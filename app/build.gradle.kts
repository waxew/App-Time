// فایل Build ماژول App-Time؛ SDK، نسخه، Signing و وابستگی‌های رابط را تعریف می‌کند.
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// اطلاعات امضای خصوصی فقط اگر در پوشه محلی signing وجود داشته باشد خوانده می‌شوند.
val keystorePropertiesFile = rootProject.file("signing/keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }
}

android {
    // namespace داخلی کدها؛ برای سازگاری نسخه‌های قبلی تغییر نمی‌کند.
    namespace = "ir.asteam.apptime"
    compileSdk = 35

    defaultConfig {
        // Application ID ثابت شرط اصلی نصب نسخه جدید روی نسخه قبلی است.
        applicationId = "ir.asteam.apptime"
        minSdk = 26
        targetSdk = 35

        // نسخه 2.0 داده‌محور و کامل‌تر پروژه.
        versionCode = 3
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    // امضای Release فقط در سیستم خصوصی توسعه‌دهنده فعال می‌شود.
    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            // Debug برای تست داخلی است و نام برنامه را تغییر نمی‌دهد.
            isMinifyEnabled = false
        }
        release {
            // در این نسخه Minify خاموش می‌ماند تا گزارش خطا و نگهداری سورس شفاف باشد.
            isMinifyEnabled = false
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
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

    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // BOM نسخه‌های Compose را هماهنگ نگه می‌دارد.
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core شامل NotificationCompat و APIهای سازگاری اندروید است.
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")

    // هسته Compose و Material 3 رابط کاربری.
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // ابزارهای Preview و تست فقط وارد Debug می‌شوند.
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
