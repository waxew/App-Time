// این فایل لایه واسط بین رابط کاربری و دیتابیس داخلی است.
// تمام صفحه‌ها از این Controller داده می‌خوانند تا پس از هر تغییر UI بلافاصله تازه‌سازی شود.
package ir.asteam.apptime.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.asteam.apptime.notification.ReminderScheduler
import ir.asteam.apptime.util.nowStamp

/**
 * Controller سطح برنامه.
 * Stateهای Compose در این کلاس نگهداری می‌شوند و CRUD به AppDatabase واگذار می‌شود.
 */
class AppController(private val context: Context) {
    // دیتابیس فقط یک نمونه در طول عمر Controller دارد.
    private val database = AppDatabase(context)

    // SharedPreferences تنظیمات سبک و پروفایل را ذخیره می‌کند.
    private val preferences = context.getSharedPreferences("app_time_settings", Context.MODE_PRIVATE)

    // لیست‌های زیر State هستند؛ با تغییر آن‌ها صفحه‌های Compose خودکار بازترسیم می‌شوند.
    var customers by mutableStateOf<List<Customer>>(emptyList())
        private set
    var appointments by mutableStateOf<List<Appointment>>(emptyList())
        private set
    var staff by mutableStateOf<List<StaffMember>>(emptyList())
        private set
    var services by mutableStateOf<List<ServiceItem>>(emptyList())
        private set
    var transactions by mutableStateOf<List<MoneyTransaction>>(emptyList())
        private set
    var messages by mutableStateOf<List<MessageRecord>>(emptyList())
        private set
    var reminders by mutableStateOf<List<ReminderRecord>>(emptyList())
        private set
    var feedback by mutableStateOf<List<FeedbackRecord>>(emptyList())
        private set
    var campaigns by mutableStateOf<List<CampaignRecord>>(emptyList())
        private set

    // در لحظه ساخته‌شدن Controller داده‌های فعلی از SQLite خوانده می‌شوند.
    init {
        refreshAll()
    }

    // تازه‌سازی کامل State پس از Import یا تغییرات بزرگ.
    fun refreshAll() {
        customers = database.customers()
        appointments = database.appointments()
        staff = database.staff()
        services = database.services()
        transactions = database.transactions()
        messages = database.messages()
        reminders = database.reminders()
        feedback = database.feedback()
        campaigns = database.campaigns()
    }

    // ------------------------------ Customer actions ------------------------------

    // افزودن مشتری و تازه‌سازی فهرست.
    fun addCustomer(item: Customer) {
        database.addCustomer(item)
        customers = database.customers()
    }

    // ویرایش مشتری.
    fun updateCustomer(item: Customer) {
        database.updateCustomer(item)
        customers = database.customers()
    }

    // حذف مشتری.
    fun deleteCustomer(id: Long) {
        database.deleteCustomer(id)
        customers = database.customers()
    }

    // افزودن یا کسر امتیاز باشگاه مشتریان.
    fun changePoints(id: Long, delta: Int) {
        database.changeCustomerPoints(id, delta)
        customers = database.customers()
    }

    // ------------------------------ Appointment actions ------------------------------

    // افزودن نوبت و زمان‌بندی اعلان محلی در صورت فعال بودن تنظیمات.
    fun addAppointment(item: Appointment) {
        val id = database.addAppointment(item)
        appointments = database.appointments()
        if (notificationEnabled() && appointmentReminderEnabled()) {
            ReminderScheduler.scheduleAppointment(context, id, item)
        }
    }

    // ویرایش نوبت؛ اعلان قبلی لغو و زمان جدید ثبت می‌شود.
    fun updateAppointment(item: Appointment) {
        database.updateAppointment(item)
        appointments = database.appointments()
        ReminderScheduler.cancelAppointment(context, item.id)
        if (notificationEnabled() && appointmentReminderEnabled()) {
            ReminderScheduler.scheduleAppointment(context, item.id, item)
        }
    }

    // حذف نوبت و اعلان مربوط به آن.
    fun deleteAppointment(id: Long) {
        database.deleteAppointment(id)
        ReminderScheduler.cancelAppointment(context, id)
        appointments = database.appointments()
    }

    // تغییر وضعیت نوبت و تازه‌سازی مشتری چون امتیاز و آخرین مراجعه ممکن است تغییر کنند.
    fun setAppointmentStatus(item: Appointment, status: String) {
        database.setAppointmentStatus(item, status)
        appointments = database.appointments()
        customers = database.customers()
    }

    // ------------------------------ Staff / Service actions ------------------------------

    // افزودن پرسنل.
    fun addStaff(item: StaffMember) {
        database.addStaff(item)
        staff = database.staff()
    }

    // ویرایش پرسنل.
    fun updateStaff(item: StaffMember) {
        database.updateStaff(item)
        staff = database.staff()
    }

    // حذف پرسنل.
    fun deleteStaff(id: Long) {
        database.deleteStaff(id)
        staff = database.staff()
    }

    // افزودن خدمت.
    fun addService(item: ServiceItem) {
        database.addService(item)
        services = database.services()
    }

    // ویرایش خدمت.
    fun updateService(item: ServiceItem) {
        database.updateService(item)
        services = database.services()
    }

    // حذف خدمت.
    fun deleteService(id: Long) {
        database.deleteService(id)
        services = database.services()
    }

    // ------------------------------ Finance actions ------------------------------

    // افزودن تراکنش مالی.
    fun addTransaction(item: MoneyTransaction) {
        database.addTransaction(item)
        transactions = database.transactions()
    }

    // حذف تراکنش.
    fun deleteTransaction(id: Long) {
        database.deleteTransaction(id)
        transactions = database.transactions()
    }

    // ------------------------------ Messaging actions ------------------------------

    // ثبت دستی پیام ورودی یا یادداشت مکالمه.
    fun addMessage(item: MessageRecord) {
        database.addMessage(item)
        messages = database.messages()
    }

    // ثبت پیام خروجی و باز کردن برنامه پیامک سیستم.
    fun sendSms(phone: String, body: String) {
        if (body.isBlank()) return
        database.addMessage(
            MessageRecord(
                phone = phone,
                body = body,
                direction = "out",
                createdAt = nowStamp()
            )
        )
        messages = database.messages()

        // ACTION_SENDTO نیاز به مجوز SEND_SMS ندارد و کاربر ارسال نهایی را در برنامه پیامک تأیید می‌کند.
        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${Uri.encode(phone)}")
            putExtra("sms_body", body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(smsIntent) }
    }

    // ارسال گروهی از طریق برنامه پیامک سیستم؛ رفتار دقیق چند گیرنده به برنامه پیامک دستگاه وابسته است.
    fun sendBulkSms(phones: List<String>, body: String) {
        val cleaned = phones.map { it.trim() }.filter { it.isNotBlank() }.distinct()
        if (cleaned.isEmpty() || body.isBlank()) return

        database.addMessage(
            MessageRecord(
                phone = cleaned.joinToString(","),
                body = body,
                direction = "out",
                createdAt = nowStamp()
            )
        )
        messages = database.messages()

        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${cleaned.joinToString(";") { Uri.encode(it) }}")
            putExtra("sms_body", body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        runCatching { context.startActivity(smsIntent) }
    }

    // حذف رکورد پیام.
    fun deleteMessage(id: Long) {
        database.deleteMessage(id)
        messages = database.messages()
    }

    // ------------------------------ Reminder actions ------------------------------

    // افزودن یادآوری و زمان‌بندی اعلان محلی ساعت 09:00 روز سررسید.
    fun addReminder(item: ReminderRecord) {
        val id = database.addReminder(item)
        reminders = database.reminders()
        if (notificationEnabled()) {
            ReminderScheduler.scheduleReminder(context, id, item)
        }
    }

    // علامت‌گذاری انجام‌شدن یادآوری و لغو Alarm آن.
    fun setReminderDone(id: Long, done: Boolean) {
        database.setReminderDone(id, done)
        if (done) {
            ReminderScheduler.cancelReminder(context, id)
        }
        reminders = database.reminders()
    }

    // حذف یادآوری.
    fun deleteReminder(id: Long) {
        database.deleteReminder(id)
        ReminderScheduler.cancelReminder(context, id)
        reminders = database.reminders()
    }

    // ------------------------------ Feedback / Campaign actions ------------------------------

    // افزودن امتیاز رضایت مشتری.
    fun addFeedback(item: FeedbackRecord) {
        database.addFeedback(item)
        feedback = database.feedback()
    }

    // حذف امتیاز ثبت‌شده.
    fun deleteFeedback(id: Long) {
        database.deleteFeedback(id)
        feedback = database.feedback()
    }

    // ذخیره پیش‌نویس کمپین.
    fun addCampaign(item: CampaignRecord) {
        database.addCampaign(item)
        campaigns = database.campaigns()
    }

    // حذف کمپین.
    fun deleteCampaign(id: Long) {
        database.deleteCampaign(id)
        campaigns = database.campaigns()
    }

    // ------------------------------ Settings ------------------------------

    // نام صاحب/مدیر مجموعه در Drawer نمایش داده می‌شود.
    fun profileName(): String = preferences.getString("profile_name", "مدیر مجموعه") ?: "مدیر مجموعه"

    // ذخیره نام پروفایل.
    fun setProfileName(value: String) {
        preferences.edit().putString("profile_name", value.trim()).apply()
    }

    // URI تصویر انتخاب‌شده از Storage Access Framework.
    fun profileImageUri(): String = preferences.getString("profile_image_uri", "") ?: ""

    // ذخیره URI پایدار تصویر پروفایل.
    fun setProfileImageUri(value: String) {
        preferences.edit().putString("profile_image_uri", value).apply()
    }

    // تنظیم اعلان‌ها.
    fun notificationEnabled(): Boolean = preferences.getBoolean("notifications", true)

    // تغییر تنظیم اعلان‌ها.
    fun setNotificationEnabled(value: Boolean) {
        preferences.edit().putBoolean("notifications", value).apply()
    }

    // تنظیم یادآوری نوبت.
    fun appointmentReminderEnabled(): Boolean = preferences.getBoolean("appointment_reminder", true)

    // تغییر یادآوری نوبت.
    fun setAppointmentReminderEnabled(value: Boolean) {
        preferences.edit().putBoolean("appointment_reminder", value).apply()
    }

    // شماره اختصاصی پنل پیامکی، در صورت تهیه سرویس خارجی.
    fun senderNumber(): String = preferences.getString("sender_number", "") ?: ""

    // ذخیره شماره اختصاصی.
    fun setSenderNumber(value: String) {
        preferences.edit().putString("sender_number", value.trim()).apply()
    }

    // نام سرویس‌دهنده پیامک برای مستندسازی تنظیمات کاربر.
    fun smsProvider(): String = preferences.getString("sms_provider", "") ?: ""

    // ذخیره نام سرویس‌دهنده پیامک.
    fun setSmsProvider(value: String) {
        preferences.edit().putString("sms_provider", value.trim()).apply()
    }

    // لینک رزرو آنلاین که کاربر از سرویس وب خود وارد می‌کند.
    fun bookingUrl(): String = preferences.getString("booking_url", "") ?: ""

    // ذخیره لینک رزرو آنلاین.
    fun setBookingUrl(value: String) {
        preferences.edit().putString("booking_url", value.trim()).apply()
    }

    // لینک سایت مجموعه.
    fun websiteUrl(): String = preferences.getString("website_url", "") ?: ""

    // ذخیره لینک سایت.
    fun setWebsiteUrl(value: String) {
        preferences.edit().putString("website_url", value.trim()).apply()
    }

    // ------------------------------ Backup ------------------------------

    // دریافت متن JSON نسخه پشتیبان.
    fun exportBackup(): String = database.exportJson()

    // بازیابی JSON و تازه‌سازی تمام Stateها.
    fun importBackup(json: String) {
        database.importJson(json)
        refreshAll()
    }

    // بستن دیتابیس در زمان تخریب Activity در صورت نیاز.
    fun close() {
        database.close()
    }
}
