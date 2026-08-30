// این فایل تبدیل تاریخ میلادی به جلالی را بدون وابستگی خارجی انجام می‌دهد.
// ذخیره‌سازی دیتابیس همچنان ISO میلادی است تا مرتب‌سازی و سازگاری ساده بماند.
package ir.asteam.apptime.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// مدل ساده تاریخ جلالی برای ساخت متن رابط.
data class JalaliDate(val year: Int, val month: Int, val day: Int)

// نام ماه‌های شمسی مورد استفاده در عنوان تقویم و نوبت‌ها.
private val persianMonths = listOf(
    "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
    "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
)

// تبدیل تاریخ میلادی به جلالی با الگوریتم عدد روز جولیانی.
fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
    val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    var gy2 = gy - 1600
    var gm2 = gm - 1
    var gd2 = gd - 1

    var gDayNo = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400
    for (i in 0 until gm2) {
        gDayNo += gDaysInMonth[i]
    }
    if (gm2 > 1 && ((gy2 + 1600) % 4 == 0 && ((gy2 + 1600) % 100 != 0 || (gy2 + 1600) % 400 == 0))) {
        gDayNo++
    }
    gDayNo += gd2

    var jDayNo = gDayNo - 79
    val jNp = jDayNo / 12053
    jDayNo %= 12053

    var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
    jDayNo %= 1461

    if (jDayNo >= 366) {
        jy += (jDayNo - 1) / 365
        jDayNo = (jDayNo - 1) % 365
    }

    var jm = 0
    while (jm < 11 && jDayNo >= jDaysInMonth[jm]) {
        jDayNo -= jDaysInMonth[jm]
        jm++
    }

    return JalaliDate(jy, jm + 1, jDayNo + 1)
}

// تبدیل تاریخ ISO دیتابیس به متن شمسی خوانا.
fun isoToPersian(value: String): String {
    val parts = value.split("-")
    if (parts.size != 3) return value
    val gy = parts[0].toIntOrNull() ?: return value
    val gm = parts[1].toIntOrNull() ?: return value
    val gd = parts[2].toIntOrNull() ?: return value
    val j = gregorianToJalali(gy, gm, gd)
    return "${toPersianDigits(j.day.toString())} ${persianMonths[j.month - 1]} ${toPersianDigits(j.year.toString())}"
}

// متن شمسی امروز را از Calendar سیستم تولید می‌کند.
fun todayPersian(): String {
    val c = Calendar.getInstance()
    val j = gregorianToJalali(
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH) + 1,
        c.get(Calendar.DAY_OF_MONTH)
    )
    return "${toPersianDigits(j.day.toString())} ${persianMonths[j.month - 1]} ${toPersianDigits(j.year.toString())}"
}

// اعتبارسنجی ساده ورودی تاریخ yyyy-MM-dd برای فرم‌ها.
fun isValidIsoDate(value: String): Boolean {
    return runCatching {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }
        parser.parse(value)
    }.getOrNull() != null
}
