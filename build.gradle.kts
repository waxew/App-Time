// فایل Gradle سطح ریشه؛ نسخه پلاگین‌های اصلی را یک‌بار برای کل پروژه تعریف می‌کند.
plugins {
    // AGP 8.7.3 به‌صورت رسمی با API 35 سازگار است.
    id("com.android.application") version "8.7.3" apply false

    // Kotlin 1.9.24 با Compose Compiler انتخاب‌شده پروژه هماهنگ است.
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}
