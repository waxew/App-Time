// این فایل زمان‌بندی اعلان‌های محلی نوبت و یادآوری را مدیریت می‌کند.
package ir.asteam.apptime.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ir.asteam.apptime.MainActivity
import ir.asteam.apptime.data.AppDatabase
import ir.asteam.apptime.data.Appointment
import ir.asteam.apptime.data.ReminderRecord
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// شناسه کانال اعلان‌ها ثابت است تا تنظیمات سیستم برای نسخه‌های بعد حفظ شود.
private const val CHANNEL_ID = "app_time_reminders"

/** ابزار زمان‌بندی AlarmManager برای یادآوری‌های محلی. */
object ReminderScheduler {

    // ثبت یادآوری نوبت یک ساعت قبل از زمان رزرو.
    fun scheduleAppointment(context: Context, id: Long, item: Appointment) {
        val trigger = parseDateTime(item.date, item.time)?.let { it - 60 * 60 * 1000L } ?: return
        if (trigger <= System.currentTimeMillis()) return

        schedule(
            context = context,
            requestCode = appointmentRequestCode(id),
            triggerAt = trigger,
            title = "یادآوری نوبت",
            body = "${item.customerName} • ${item.service} • ${item.time}"
        )
    }

    // لغو اعلان نوبت هنگام حذف یا ویرایش.
    fun cancelAppointment(context: Context, id: Long) {
        cancel(context, appointmentRequestCode(id))
    }

    // ثبت یادآوری عمومی در ساعت 09:00 روز سررسید.
    fun scheduleReminder(context: Context, id: Long, item: ReminderRecord) {
        val trigger = parseDateTime(item.dueDate, "09:00") ?: return
        if (trigger <= System.currentTimeMillis()) return

        schedule(
            context = context,
            requestCode = reminderRequestCode(id),
            triggerAt = trigger,
            title = "یادآوری تایم",
            body = listOf(item.customerName, item.title).filter { it.isNotBlank() }.joinToString(" • ")
        )
    }

    // لغو یادآوری عمومی.
    fun cancelReminder(context: Context, id: Long) {
        cancel(context, reminderRequestCode(id))
    }

    // ساخت PendingIntent و ثبت Alarm بدون نیاز به مجوز Exact Alarm.
    private fun schedule(
        context: Context,
        requestCode: Int,
        triggerAt: Long,
        title: String,
        body: String
    ) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = notificationPendingIntent(context, requestCode, title, body)

        // setAndAllowWhileIdle برای یادآوری‌های روزمره کافی است و مجوز ویژه Exact Alarm نمی‌خواهد.
        manager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
    }

    // لغو PendingIntent قبلی با همان requestCode.
    private fun cancel(context: Context, requestCode: Int) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = notificationPendingIntent(context, requestCode, "", "")
        manager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    // Intent اعلان تمام اطلاعات لازم برای BroadcastReceiver را حمل می‌کند.
    private fun notificationPendingIntent(
        context: Context,
        requestCode: Int,
        title: String,
        body: String
    ): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("body", body)
            putExtra("notification_id", requestCode)
        }

        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // تبدیل تاریخ و ساعت دیتابیس به timestamp.
    private fun parseDateTime(date: String, time: String): Long? {
        return runCatching {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).apply {
                isLenient = false
            }.parse("$date $time")?.time
        }.getOrNull()
    }

    // بازه requestCode نوبت‌ها از 100000 شروع می‌شود تا با یادآوری‌ها تداخل نداشته باشد.
    private fun appointmentRequestCode(id: Long): Int = 100000 + (id % 500000).toInt()

    // بازه requestCode یادآوری‌ها از 700000 شروع می‌شود.
    private fun reminderRequestCode(id: Long): Int = 700000 + (id % 200000).toInt()
}

/** Receiver اعلان را هنگام فعال‌شدن Alarm نمایش می‌دهد. */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // از Android 8 به بالا کانال اعلان باید قبل از نمایش ساخته شود.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "یادآوری‌های تایم",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "یادآوری نوبت‌ها و پیگیری مشتریان"
                }
            )
        }

        // لمس اعلان برنامه را باز می‌کند.
        val openApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // NotificationCompat سازگاری نسخه‌های مختلف اندروید را فراهم می‌کند.
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(intent.getStringExtra("title") ?: "تایم")
            .setContentText(intent.getStringExtra("body") ?: "یادآوری جدید")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    intent.getStringExtra("body") ?: "یادآوری جدید"
                )
            )
            .setAutoCancel(true)
            .setContentIntent(openApp)
            .build()

        // شناسه یکتا جلوی جایگزینی اعلان‌های متفاوت را می‌گیرد.
        manager.notify(intent.getIntExtra("notification_id", 1), notification)
    }
}

/**
 * Receiver بوت دستگاه.
 * پس از خاموش/روشن‌شدن، AlarmManager پاک می‌شود؛ بنابراین نوبت‌ها و یادآوری‌های آینده دوباره ثبت می‌شوند.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val database = AppDatabase(context)
        try {
            // فقط نوبت‌های آینده/موجود دوباره زمان‌بندی می‌شوند؛ Scheduler تاریخ گذشته را نادیده می‌گیرد.
            database.appointments().forEach { appointment ->
                if (appointment.status != "لغو شد" && appointment.status != "انجام شد") {
                    ReminderScheduler.scheduleAppointment(context, appointment.id, appointment)
                }
            }

            // یادآوری‌های انجام‌نشده نیز بازیابی می‌شوند.
            database.reminders().filterNot { it.done }.forEach { reminder ->
                ReminderScheduler.scheduleReminder(context, reminder.id, reminder)
            }
        } finally {
            database.close()
        }
    }
}
