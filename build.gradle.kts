// فایل Gradle سطح ریشه پروژه است.
// نسخه پلاگین‌های Android و Kotlin را یک‌بار برای همه ماژول‌ها تعریف می‌کند.
plugins {
    // Android Gradle Plugin مسئول تبدیل سورس اندروید به APK است.
    id("com.android.application") version "8.5.2" apply false

    // پلاگین Kotlin کامپایل فایل‌های .kt را فعال می‌کند.
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}
