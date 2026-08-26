// این فایل ساختار Workspace گرادل و Repositoryهای دریافت وابستگی را تعیین می‌کند.
pluginManagement {
    // محل دریافت Pluginهای Gradle.
    repositories {
        // پلاگین‌های رسمی Android از Google دریافت می‌شوند.
        google()
        // کتابخانه‌های عمومی JVM/Kotlin از Maven Central قابل دریافت هستند.
        mavenCentral()
        // Gradle Plugin Portal برای Pluginهای Gradle استفاده می‌شود.
        gradlePluginPortal()
    }
}

// سیاست Repositoryهای Dependency پروژه.
dependencyResolutionManagement {
    // اجازه تعریف Repository پراکنده داخل ماژول‌ها داده نمی‌شود تا Build قابل پیش‌بینی بماند.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Repositoryهای مورد اعتماد پروژه.
    repositories {
        google()
        mavenCentral()
    }
}

// نام پروژه در Android Studio و Gradle.
rootProject.name = "AppTime"

// ماژول اصلی اپلیکیشن به Workspace اضافه می‌شود.
include(":app")
