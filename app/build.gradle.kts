// فایل پیکربندی ماژول اصلی اندروید است.
// این فایل نسخه برنامه، SDKها، Compose، وابستگی‌ها و نحوه امضای Release را تعریف می‌کند.

// برای خواندن تنظیمات کلید امضا از فایل محلی keystore.properties از Properties جاوا استفاده می‌کنیم.
import java.util.Properties

// مسیر فایل تنظیمات امضای خصوصی را مشخص می‌کنیم.
// این فایل عمداً در GitHub قرار نمی‌گیرد و فقط داخل بسته خصوصی سورس تحویلی وجود خواهد داشت.
val keystorePropertiesFile = rootProject.file("signing/keystore.properties")

// شیء نگهدارنده اطلاعات امضا ساخته می‌شود.
val keystoreProperties = Properties()

// اگر فایل امضا روی سیستم توسعه‌دهنده موجود باشد، اطلاعات آن خوانده می‌شود.
if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { stream ->
        keystoreProperties.load(stream)
    }
}

// پلاگین برنامه اندروید و Kotlin برای این ماژول فعال می‌شوند.
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// تنظیمات اصلی ساخت برنامه اندروید از این بخش شروع می‌شود.
android {
    // namespace داخلی کدهای Kotlin و منابع برنامه است.
    namespace = "ir.asteam.apptime"

    // برنامه با Android SDK 35 کامپایل می‌شود.
    compileSdk = 35

    // تنظیمات ثابت هر خروجی APK در این بخش قرار دارد.
    defaultConfig {
        // شناسه ثابت برنامه است و برای اینکه نسخه‌های بعدی روی نسخه قبلی نصب شوند تغییر نمی‌کند.
        applicationId = "ir.asteam.apptime"

        // حداقل اندروید پشتیبانی‌شده Android 8 است.
        minSdk = 26

        // رفتار برنامه با قواعد Android 15 هدف‌گذاری می‌شود.
        targetSdk = 35

        // در هر انتشار این عدد باید افزایش پیدا کند تا سیستم نسخه جدید را تشخیص دهد.
        versionCode = 2

        // نام قابل نمایش نسخه نهایی فعلی برنامه است.
        versionName = "1.1.0"

        // Runner پیش‌فرض تست‌های Instrumentation اندروید را تعیین می‌کند.
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // پشتیبانی از Vector Drawable برای نسخه‌های مختلف اندروید فعال می‌شود.
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // پیکربندی کلید امضای نسخه انتشار در این بخش انجام می‌شود.
    signingConfigs {
        // یک SigningConfig با نام release می‌سازیم.
        create("release") {
            // فقط وقتی کلید خصوصی روی سیستم وجود داشته باشد، مقادیر امضا اعمال می‌شوند.
            if (keystorePropertiesFile.exists()) {
                // مسیر keystore از فایل خصوصی خوانده می‌شود.
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))

                // رمز خود فایل keystore خوانده می‌شود.
                storePassword = keystoreProperties.getProperty("storePassword")

                // نام کلید داخل keystore خوانده می‌شود.
                keyAlias = keystoreProperties.getProperty("keyAlias")

                // رمز کلید خصوصی داخل keystore خوانده می‌شود.
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    // انواع Build برنامه در این بخش تعریف می‌شوند.
    buildTypes {
        // نسخه Release برای خروجی نهایی قابل انتشار استفاده می‌شود.
        release {
            // در v1.1 فشرده‌سازی و Obfuscation خاموش است تا عیب‌یابی ساده‌تر بماند.
            isMinifyEnabled = false

            // اگر فایل امضای خصوصی وجود داشته باشد، Release با همان کلید پایدار امضا می‌شود.
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }

            // قواعد ProGuard پیش‌فرض به همراه فایل قواعد پروژه معرفی می‌شوند.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // سورس Java با Java 17 کامپایل می‌شود.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // bytecode تولیدی Kotlin نیز با JVM 17 سازگار می‌شود.
    kotlinOptions {
        jvmTarget = "17"
    }

    // قابلیت Compose و BuildConfig برای UI و خواندن نسخه برنامه فعال می‌شوند.
    buildFeatures {
        compose = true
        buildConfig = true
    }

    // نسخه Compiler مخصوص Jetpack Compose مشخص می‌شود.
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    // فایل‌های License تکراری بعضی کتابخانه‌ها از بسته نهایی حذف می‌شوند تا Conflict ایجاد نشود.
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

// کتابخانه‌های مورد نیاز برنامه در این بخش اضافه می‌شوند.
dependencies {
    // BOM باعث می‌شود نسخه کتابخانه‌های Compose با یکدیگر هماهنگ باشند.
    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")

    // BOM برای کد اصلی برنامه اعمال می‌شود.
    implementation(composeBom)

    // همان BOM برای تست‌های اندرویدی نیز اعمال می‌شود.
    androidTestImplementation(composeBom)

    // افزونه‌های کاربردی AndroidX Core را اضافه می‌کند.
    implementation("androidx.core:core-ktx:1.13.1")

    // Lifecycle Runtime برای هماهنگی Activity و چرخه عمر برنامه استفاده می‌شود.
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

    // اتصال ComponentActivity به Jetpack Compose و BackHandler از این وابستگی می‌آید.
    implementation("androidx.activity:activity-compose:1.9.0")

    // هسته UI در Compose را اضافه می‌کند.
    implementation("androidx.compose.ui:ui")

    // قابلیت Preview کامپوننت‌های Compose در Android Studio را اضافه می‌کند.
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material 3 شامل Scaffold، Drawer، Card و سایر اجزای رابط است.
    implementation("androidx.compose.material3:material3")

    // مجموعه کامل آیکون‌های Material برای کارت‌ها و منوها استفاده می‌شود.
    implementation("androidx.compose.material:material-icons-extended")

    // ابزارهای Visual Debugging فقط در Build نوع Debug اضافه می‌شوند.
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Manifest مخصوص تست UI فقط در Debug اضافه می‌شود و وارد Release نهایی نمی‌شود.
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
