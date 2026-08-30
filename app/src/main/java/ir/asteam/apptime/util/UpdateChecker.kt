// این فایل بررسی نسخه جدید را با خواندن version.json از GitHub انجام می‌دهد.
// عملیات شبکه خارج از Thread رابط اجرا می‌شود تا UI مسدود نشود.
package ir.asteam.apptime.util

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// آدرس Manifest نسخه همیشه به شاخه main پروژه اشاره می‌کند.
private const val VERSION_MANIFEST_URL =
    "https://raw.githubusercontent.com/waxew/App-Time/main/version.json"

// نتیجه قابل نمایش بررسی بروزرسانی.
data class UpdateResult(
    val latestVersionCode: Int,
    val latestVersionName: String,
    val downloadUrl: String,
    val notes: String,
    val updateAvailable: Boolean
)

/** ابزار سبک بررسی نسخه بدون کتابخانه شبکه خارجی. */
object UpdateChecker {

    // درخواست HTTP روی Thread پس‌زمینه اجرا و نتیجه روی Main Thread برگردانده می‌شود.
    fun check(currentVersionCode: Int, callback: (Result<UpdateResult>) -> Unit) {
        Thread {
            val result = runCatching {
                val connection = URL(VERSION_MANIFEST_URL).openConnection() as HttpURLConnection
                connection.connectTimeout = 8000
                connection.readTimeout = 8000
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/json")

                try {
                    if (connection.responseCode !in 200..299) {
                        error("خطای دریافت نسخه: ${connection.responseCode}")
                    }

                    val text = connection.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(text)
                    val latestCode = json.getInt("versionCode")
                    UpdateResult(
                        latestVersionCode = latestCode,
                        latestVersionName = json.optString("versionName", latestCode.toString()),
                        downloadUrl = json.optString("downloadUrl"),
                        notes = json.optString("notes"),
                        updateAvailable = latestCode > currentVersionCode
                    )
                } finally {
                    connection.disconnect()
                }
            }

            // Callback روی Looper اصلی اجرا می‌شود تا State Compose امن تغییر کند.
            Handler(Looper.getMainLooper()).post {
                callback(result)
            }
        }.start()
    }
}
