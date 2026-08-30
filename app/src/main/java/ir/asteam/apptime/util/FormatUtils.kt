// این فایل توابع کوچک و مشترک برای تاریخ، مبلغ و متن را در یک محل نگه می‌دارد.
package ir.asteam.apptime.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// فرمت هزارگان برای تمام مبالغ مالی برنامه؛ نمونه: 12000000 -> 12,000,000.
fun formatMoney(value: Long): String = DecimalFormat("#,###").format(value)

// تاریخ استاندارد ذخیره‌سازی دیتابیس؛ مستقل از زبان رابط و مناسب مرتب‌سازی است.
fun todayIso(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

// زمان استاندارد برای ثبت لاگ پیام‌ها و رویدادها.
fun nowStamp(): String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())

// تبدیل ارقام انگلیسی به فارسی برای نمایش دوستانه در رابط فارسی.
fun toPersianDigits(value: String): String {
    val english = "0123456789"
    val persian = "۰۱۲۳۴۵۶۷۸۹"
    return buildString {
        value.forEach { ch ->
            val index = english.indexOf(ch)
            append(if (index >= 0) persian[index] else ch)
        }
    }
}
