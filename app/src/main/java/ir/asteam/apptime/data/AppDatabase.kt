// این فایل هسته دیتابیس داخلی App-Time است.
// برنامه برای اطلاعات اصلی به اینترنت وابسته نیست و همه رکوردها در SQLite خود دستگاه ذخیره می‌شوند.
package ir.asteam.apptime.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject

// نام فایل دیتابیس و نسخه Schema در یک نقطه ثابت نگهداری می‌شوند.
private const val DATABASE_NAME = "app_time.db"
private const val DATABASE_VERSION = 1

/**
 * SQLiteOpenHelper ساخت، ارتقا و عملیات CRUD دیتابیس را مدیریت می‌کند.
 * استفاده از API داخلی اندروید باعث می‌شود پروژه به پلاگین KSP/KAPT یا ORM خارجی وابسته نباشد.
 */
class AppDatabase(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    // در اولین اجرای برنامه تمام جدول‌های مورد نیاز ایجاد می‌شوند.
    override fun onCreate(db: SQLiteDatabase) {
        // جدول مشتریان.
        db.execSQL(
            """
            CREATE TABLE customers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                phone TEXT NOT NULL,
                birthday TEXT NOT NULL DEFAULT '',
                notes TEXT NOT NULL DEFAULT '',
                points INTEGER NOT NULL DEFAULT 0,
                last_visit TEXT NOT NULL DEFAULT ''
            )
            """.trimIndent()
        )

        // جدول نوبت‌ها.
        db.execSQL(
            """
            CREATE TABLE appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_name TEXT NOT NULL,
                phone TEXT NOT NULL DEFAULT '',
                service TEXT NOT NULL,
                staff_name TEXT NOT NULL DEFAULT '',
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                duration_minutes INTEGER NOT NULL DEFAULT 45,
                price INTEGER NOT NULL DEFAULT 0,
                status TEXT NOT NULL DEFAULT 'در انتظار'
            )
            """.trimIndent()
        )

        // جدول پرسنل.
        db.execSQL(
            """
            CREATE TABLE staff (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                role TEXT NOT NULL,
                phone TEXT NOT NULL DEFAULT '',
                active INTEGER NOT NULL DEFAULT 1
            )
            """.trimIndent()
        )

        // جدول خدمات قابل رزرو.
        db.execSQL(
            """
            CREATE TABLE services (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                duration_minutes INTEGER NOT NULL DEFAULT 45,
                price INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        // جدول تراکنش‌های مالی.
        db.execSQL(
            """
            CREATE TABLE money_transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                title TEXT NOT NULL,
                amount INTEGER NOT NULL,
                date TEXT NOT NULL
            )
            """.trimIndent()
        )

        // جدول سابقه پیام‌های داخلی و پیام‌های ارسال‌شده از برنامه.
        db.execSQL(
            """
            CREATE TABLE messages (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                phone TEXT NOT NULL DEFAULT '',
                body TEXT NOT NULL,
                direction TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """.trimIndent()
        )

        // جدول یادآوری‌ها.
        db.execSQL(
            """
            CREATE TABLE reminders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_name TEXT NOT NULL DEFAULT '',
                title TEXT NOT NULL,
                due_date TEXT NOT NULL,
                done INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        // جدول امتیازهای رضایت‌سنجی.
        db.execSQL(
            """
            CREATE TABLE feedback (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                customer_name TEXT NOT NULL DEFAULT '',
                rating INTEGER NOT NULL,
                note TEXT NOT NULL DEFAULT '',
                date TEXT NOT NULL
            )
            """.trimIndent()
        )

        // جدول کمپین‌ها و پیش‌نویس‌های بازاریابی.
        db.execSQL(
            """
            CREATE TABLE campaigns (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                title TEXT NOT NULL,
                detail TEXT NOT NULL DEFAULT '',
                date TEXT NOT NULL
            )
            """.trimIndent()
        )

        // یک خدمت عمومی فقط برای اینکه فرم نوبت از اولین اجرا گزینه پایه داشته باشد ثبت می‌شود.
        db.insert(
            "services",
            null,
            ContentValues().apply {
                put("name", "خدمات عمومی")
                put("duration_minutes", 45)
                put("price", 0)
            }
        )
    }

    // مسیر ارتقا برای نسخه‌های بعدی به‌صورت افزایشی نوشته خواهد شد تا اطلاعات کاربر پاک نشود.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // در نسخه فعلی Schema شماره 1 است؛ این بلوک عمداً هیچ جدول موجودی را حذف نمی‌کند.
        if (oldVersion < 1) {
            onCreate(db)
        }
    }

    // ------------------------------ Customers ------------------------------

    // خواندن تمام مشتریان با جدیدترین رکورد در ابتدای فهرست.
    fun customers(): List<Customer> = readableDatabase.rawQuery(
        "SELECT * FROM customers ORDER BY id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toCustomer())
            }
        }
    }

    // افزودن مشتری جدید و برگرداندن شناسه رکورد.
    fun addCustomer(item: Customer): Long = writableDatabase.insert(
        "customers",
        null,
        item.toValues(includeId = false)
    )

    // ویرایش مشتری موجود.
    fun updateCustomer(item: Customer): Boolean = writableDatabase.update(
        "customers",
        item.toValues(includeId = false),
        "id=?",
        arrayOf(item.id.toString())
    ) > 0

    // حذف مشتری بر اساس شناسه.
    fun deleteCustomer(id: Long): Boolean = writableDatabase.delete(
        "customers",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // تغییر امتیاز وفاداری بدون منفی شدن مقدار.
    fun changeCustomerPoints(id: Long, delta: Int): Boolean {
        val db = writableDatabase
        db.execSQL(
            "UPDATE customers SET points = MAX(0, points + ?) WHERE id = ?",
            arrayOf(delta, id)
        )
        return true
    }

    // ------------------------------ Appointments ------------------------------

    // خواندن نوبت‌ها بر اساس تاریخ و ساعت.
    fun appointments(): List<Appointment> = readableDatabase.rawQuery(
        "SELECT * FROM appointments ORDER BY date ASC, time ASC, id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toAppointment())
            }
        }
    }

    // افزودن نوبت.
    fun addAppointment(item: Appointment): Long = writableDatabase.insert(
        "appointments",
        null,
        item.toValues(includeId = false)
    )

    // ویرایش نوبت.
    fun updateAppointment(item: Appointment): Boolean = writableDatabase.update(
        "appointments",
        item.toValues(includeId = false),
        "id=?",
        arrayOf(item.id.toString())
    ) > 0

    // حذف نوبت.
    fun deleteAppointment(id: Long): Boolean = writableDatabase.delete(
        "appointments",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // تغییر وضعیت نوبت و ثبت آخرین مراجعه/امتیاز مشتری هنگام تکمیل شدن.
    fun setAppointmentStatus(item: Appointment, status: String): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        return try {
            val changed = db.update(
                "appointments",
                ContentValues().apply { put("status", status) },
                "id=?",
                arrayOf(item.id.toString())
            ) > 0

            if (changed && status == "انجام شد" && item.phone.isNotBlank()) {
                db.execSQL(
                    "UPDATE customers SET last_visit=?, points=points+10 WHERE phone=?",
                    arrayOf(item.date, item.phone)
                )
            }

            db.setTransactionSuccessful()
            changed
        } finally {
            db.endTransaction()
        }
    }

    // ------------------------------ Staff ------------------------------

    // خواندن اعضای تیم.
    fun staff(): List<StaffMember> = readableDatabase.rawQuery(
        "SELECT * FROM staff ORDER BY active DESC, id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toStaff())
            }
        }
    }

    // افزودن پرسنل.
    fun addStaff(item: StaffMember): Long = writableDatabase.insert(
        "staff",
        null,
        item.toValues(includeId = false)
    )

    // ویرایش پرسنل.
    fun updateStaff(item: StaffMember): Boolean = writableDatabase.update(
        "staff",
        item.toValues(includeId = false),
        "id=?",
        arrayOf(item.id.toString())
    ) > 0

    // حذف پرسنل.
    fun deleteStaff(id: Long): Boolean = writableDatabase.delete(
        "staff",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Services ------------------------------

    // خواندن خدمات.
    fun services(): List<ServiceItem> = readableDatabase.rawQuery(
        "SELECT * FROM services ORDER BY id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toService())
            }
        }
    }

    // افزودن خدمت.
    fun addService(item: ServiceItem): Long = writableDatabase.insert(
        "services",
        null,
        item.toValues(includeId = false)
    )

    // ویرایش خدمت.
    fun updateService(item: ServiceItem): Boolean = writableDatabase.update(
        "services",
        item.toValues(includeId = false),
        "id=?",
        arrayOf(item.id.toString())
    ) > 0

    // حذف خدمت.
    fun deleteService(id: Long): Boolean = writableDatabase.delete(
        "services",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Money ------------------------------

    // خواندن تراکنش‌های مالی.
    fun transactions(): List<MoneyTransaction> = readableDatabase.rawQuery(
        "SELECT * FROM money_transactions ORDER BY date DESC, id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toTransaction())
            }
        }
    }

    // افزودن تراکنش.
    fun addTransaction(item: MoneyTransaction): Long = writableDatabase.insert(
        "money_transactions",
        null,
        item.toValues(includeId = false)
    )

    // حذف تراکنش.
    fun deleteTransaction(id: Long): Boolean = writableDatabase.delete(
        "money_transactions",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Messages ------------------------------

    // خواندن سابقه پیام‌ها.
    fun messages(): List<MessageRecord> = readableDatabase.rawQuery(
        "SELECT * FROM messages ORDER BY id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toMessage())
            }
        }
    }

    // افزودن رکورد پیام.
    fun addMessage(item: MessageRecord): Long = writableDatabase.insert(
        "messages",
        null,
        item.toValues(includeId = false)
    )

    // حذف رکورد پیام.
    fun deleteMessage(id: Long): Boolean = writableDatabase.delete(
        "messages",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Reminders ------------------------------

    // خواندن یادآوری‌ها.
    fun reminders(): List<ReminderRecord> = readableDatabase.rawQuery(
        "SELECT * FROM reminders ORDER BY done ASC, due_date ASC, id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toReminder())
            }
        }
    }

    // افزودن یادآوری.
    fun addReminder(item: ReminderRecord): Long = writableDatabase.insert(
        "reminders",
        null,
        item.toValues(includeId = false)
    )

    // تغییر وضعیت انجام‌شدن یادآوری.
    fun setReminderDone(id: Long, done: Boolean): Boolean = writableDatabase.update(
        "reminders",
        ContentValues().apply { put("done", if (done) 1 else 0) },
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // حذف یادآوری.
    fun deleteReminder(id: Long): Boolean = writableDatabase.delete(
        "reminders",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Feedback ------------------------------

    // خواندن رضایت‌سنجی‌ها.
    fun feedback(): List<FeedbackRecord> = readableDatabase.rawQuery(
        "SELECT * FROM feedback ORDER BY date DESC, id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toFeedback())
            }
        }
    }

    // افزودن رضایت‌سنجی.
    fun addFeedback(item: FeedbackRecord): Long = writableDatabase.insert(
        "feedback",
        null,
        item.toValues(includeId = false)
    )

    // حذف رضایت‌سنجی.
    fun deleteFeedback(id: Long): Boolean = writableDatabase.delete(
        "feedback",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Campaigns ------------------------------

    // خواندن کمپین‌ها.
    fun campaigns(): List<CampaignRecord> = readableDatabase.rawQuery(
        "SELECT * FROM campaigns ORDER BY date DESC, id DESC",
        null
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(cursor.toCampaign())
            }
        }
    }

    // افزودن کمپین.
    fun addCampaign(item: CampaignRecord): Long = writableDatabase.insert(
        "campaigns",
        null,
        item.toValues(includeId = false)
    )

    // حذف کمپین.
    fun deleteCampaign(id: Long): Boolean = writableDatabase.delete(
        "campaigns",
        "id=?",
        arrayOf(id.toString())
    ) > 0

    // ------------------------------ Backup ------------------------------

    // ساخت نسخه پشتیبان JSON از تمام داده‌های مهم کاربر.
    fun exportJson(): String {
        val root = JSONObject()
        root.put("schemaVersion", DATABASE_VERSION)
        root.put("customers", JSONArray().apply { customers().forEach { put(it.toJson()) } })
        root.put("appointments", JSONArray().apply { appointments().forEach { put(it.toJson()) } })
        root.put("staff", JSONArray().apply { staff().forEach { put(it.toJson()) } })
        root.put("services", JSONArray().apply { services().forEach { put(it.toJson()) } })
        root.put("transactions", JSONArray().apply { transactions().forEach { put(it.toJson()) } })
        root.put("messages", JSONArray().apply { messages().forEach { put(it.toJson()) } })
        root.put("reminders", JSONArray().apply { reminders().forEach { put(it.toJson()) } })
        root.put("feedback", JSONArray().apply { feedback().forEach { put(it.toJson()) } })
        root.put("campaigns", JSONArray().apply { campaigns().forEach { put(it.toJson()) } })
        return root.toString(2)
    }

    // بازیابی نسخه پشتیبان؛ عملیات داخل Transaction انجام می‌شود تا Import نیمه‌کاره نماند.
    fun importJson(json: String) {
        val root = JSONObject(json)
        val db = writableDatabase
        db.beginTransaction()
        try {
            // ابتدا داده‌های فعلی پاک می‌شوند؛ خود ساختار جدول‌ها حفظ می‌شود.
            listOf(
                "customers",
                "appointments",
                "staff",
                "services",
                "money_transactions",
                "messages",
                "reminders",
                "feedback",
                "campaigns"
            ).forEach { table -> db.delete(table, null, null) }

            root.optJSONArray("customers")?.forEachObject { obj ->
                db.insert("customers", null, Customer(
                    id = obj.optLong("id"),
                    name = obj.optString("name"),
                    phone = obj.optString("phone"),
                    birthday = obj.optString("birthday"),
                    notes = obj.optString("notes"),
                    points = obj.optInt("points"),
                    lastVisit = obj.optString("lastVisit")
                ).toValues(includeId = true))
            }

            root.optJSONArray("appointments")?.forEachObject { obj ->
                db.insert("appointments", null, Appointment(
                    id = obj.optLong("id"),
                    customerName = obj.optString("customerName"),
                    phone = obj.optString("phone"),
                    service = obj.optString("service"),
                    staffName = obj.optString("staffName"),
                    date = obj.optString("date"),
                    time = obj.optString("time"),
                    durationMinutes = obj.optInt("durationMinutes", 45),
                    price = obj.optLong("price"),
                    status = obj.optString("status", "در انتظار")
                ).toValues(includeId = true))
            }

            root.optJSONArray("staff")?.forEachObject { obj ->
                db.insert("staff", null, StaffMember(
                    id = obj.optLong("id"),
                    name = obj.optString("name"),
                    role = obj.optString("role"),
                    phone = obj.optString("phone"),
                    active = obj.optBoolean("active", true)
                ).toValues(includeId = true))
            }

            root.optJSONArray("services")?.forEachObject { obj ->
                db.insert("services", null, ServiceItem(
                    id = obj.optLong("id"),
                    name = obj.optString("name"),
                    durationMinutes = obj.optInt("durationMinutes", 45),
                    price = obj.optLong("price")
                ).toValues(includeId = true))
            }

            root.optJSONArray("transactions")?.forEachObject { obj ->
                db.insert("money_transactions", null, MoneyTransaction(
                    id = obj.optLong("id"),
                    type = obj.optString("type"),
                    title = obj.optString("title"),
                    amount = obj.optLong("amount"),
                    date = obj.optString("date")
                ).toValues(includeId = true))
            }

            root.optJSONArray("messages")?.forEachObject { obj ->
                db.insert("messages", null, MessageRecord(
                    id = obj.optLong("id"),
                    phone = obj.optString("phone"),
                    body = obj.optString("body"),
                    direction = obj.optString("direction"),
                    createdAt = obj.optString("createdAt")
                ).toValues(includeId = true))
            }

            root.optJSONArray("reminders")?.forEachObject { obj ->
                db.insert("reminders", null, ReminderRecord(
                    id = obj.optLong("id"),
                    customerName = obj.optString("customerName"),
                    title = obj.optString("title"),
                    dueDate = obj.optString("dueDate"),
                    done = obj.optBoolean("done")
                ).toValues(includeId = true))
            }

            root.optJSONArray("feedback")?.forEachObject { obj ->
                db.insert("feedback", null, FeedbackRecord(
                    id = obj.optLong("id"),
                    customerName = obj.optString("customerName"),
                    rating = obj.optInt("rating", 5),
                    note = obj.optString("note"),
                    date = obj.optString("date")
                ).toValues(includeId = true))
            }

            root.optJSONArray("campaigns")?.forEachObject { obj ->
                db.insert("campaigns", null, CampaignRecord(
                    id = obj.optLong("id"),
                    type = obj.optString("type"),
                    title = obj.optString("title"),
                    detail = obj.optString("detail"),
                    date = obj.optString("date")
                ).toValues(includeId = true))
            }

            // اگر فایل پشتیبان هیچ خدمتی نداشت، خدمت عمومی دوباره اضافه می‌شود.
            val count = db.rawQuery("SELECT COUNT(*) FROM services", null).use { c ->
                c.moveToFirst()
                c.getInt(0)
            }
            if (count == 0) {
                db.insert("services", null, ContentValues().apply {
                    put("name", "خدمات عمومی")
                    put("duration_minutes", 45)
                    put("price", 0)
                })
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}

// ------------------------------ Cursor mapping ------------------------------

// توابع زیر Cursor را به مدل‌های Kotlin تبدیل می‌کنند تا کد Queryها خواناتر بماند.
private fun Cursor.toCustomer() = Customer(
    id = getLong(getColumnIndexOrThrow("id")),
    name = getString(getColumnIndexOrThrow("name")),
    phone = getString(getColumnIndexOrThrow("phone")),
    birthday = getString(getColumnIndexOrThrow("birthday")),
    notes = getString(getColumnIndexOrThrow("notes")),
    points = getInt(getColumnIndexOrThrow("points")),
    lastVisit = getString(getColumnIndexOrThrow("last_visit"))
)

private fun Cursor.toAppointment() = Appointment(
    id = getLong(getColumnIndexOrThrow("id")),
    customerName = getString(getColumnIndexOrThrow("customer_name")),
    phone = getString(getColumnIndexOrThrow("phone")),
    service = getString(getColumnIndexOrThrow("service")),
    staffName = getString(getColumnIndexOrThrow("staff_name")),
    date = getString(getColumnIndexOrThrow("date")),
    time = getString(getColumnIndexOrThrow("time")),
    durationMinutes = getInt(getColumnIndexOrThrow("duration_minutes")),
    price = getLong(getColumnIndexOrThrow("price")),
    status = getString(getColumnIndexOrThrow("status"))
)

private fun Cursor.toStaff() = StaffMember(
    id = getLong(getColumnIndexOrThrow("id")),
    name = getString(getColumnIndexOrThrow("name")),
    role = getString(getColumnIndexOrThrow("role")),
    phone = getString(getColumnIndexOrThrow("phone")),
    active = getInt(getColumnIndexOrThrow("active")) == 1
)

private fun Cursor.toService() = ServiceItem(
    id = getLong(getColumnIndexOrThrow("id")),
    name = getString(getColumnIndexOrThrow("name")),
    durationMinutes = getInt(getColumnIndexOrThrow("duration_minutes")),
    price = getLong(getColumnIndexOrThrow("price"))
)

private fun Cursor.toTransaction() = MoneyTransaction(
    id = getLong(getColumnIndexOrThrow("id")),
    type = getString(getColumnIndexOrThrow("type")),
    title = getString(getColumnIndexOrThrow("title")),
    amount = getLong(getColumnIndexOrThrow("amount")),
    date = getString(getColumnIndexOrThrow("date"))
)

private fun Cursor.toMessage() = MessageRecord(
    id = getLong(getColumnIndexOrThrow("id")),
    phone = getString(getColumnIndexOrThrow("phone")),
    body = getString(getColumnIndexOrThrow("body")),
    direction = getString(getColumnIndexOrThrow("direction")),
    createdAt = getString(getColumnIndexOrThrow("created_at"))
)

private fun Cursor.toReminder() = ReminderRecord(
    id = getLong(getColumnIndexOrThrow("id")),
    customerName = getString(getColumnIndexOrThrow("customer_name")),
    title = getString(getColumnIndexOrThrow("title")),
    dueDate = getString(getColumnIndexOrThrow("due_date")),
    done = getInt(getColumnIndexOrThrow("done")) == 1
)

private fun Cursor.toFeedback() = FeedbackRecord(
    id = getLong(getColumnIndexOrThrow("id")),
    customerName = getString(getColumnIndexOrThrow("customer_name")),
    rating = getInt(getColumnIndexOrThrow("rating")),
    note = getString(getColumnIndexOrThrow("note")),
    date = getString(getColumnIndexOrThrow("date"))
)

private fun Cursor.toCampaign() = CampaignRecord(
    id = getLong(getColumnIndexOrThrow("id")),
    type = getString(getColumnIndexOrThrow("type")),
    title = getString(getColumnIndexOrThrow("title")),
    detail = getString(getColumnIndexOrThrow("detail")),
    date = getString(getColumnIndexOrThrow("date"))
)

// ------------------------------ ContentValues mapping ------------------------------

// هر مدل به ContentValues تبدیل می‌شود تا Insert و Update یک پیاده‌سازی مشترک داشته باشند.
private fun Customer.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("name", name)
    put("phone", phone)
    put("birthday", birthday)
    put("notes", notes)
    put("points", points)
    put("last_visit", lastVisit)
}

private fun Appointment.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("customer_name", customerName)
    put("phone", phone)
    put("service", service)
    put("staff_name", staffName)
    put("date", date)
    put("time", time)
    put("duration_minutes", durationMinutes)
    put("price", price)
    put("status", status)
}

private fun StaffMember.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("name", name)
    put("role", role)
    put("phone", phone)
    put("active", if (active) 1 else 0)
}

private fun ServiceItem.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("name", name)
    put("duration_minutes", durationMinutes)
    put("price", price)
}

private fun MoneyTransaction.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("type", type)
    put("title", title)
    put("amount", amount)
    put("date", date)
}

private fun MessageRecord.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("phone", phone)
    put("body", body)
    put("direction", direction)
    put("created_at", createdAt)
}

private fun ReminderRecord.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("customer_name", customerName)
    put("title", title)
    put("due_date", dueDate)
    put("done", if (done) 1 else 0)
}

private fun FeedbackRecord.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("customer_name", customerName)
    put("rating", rating.coerceIn(1, 5))
    put("note", note)
    put("date", date)
}

private fun CampaignRecord.toValues(includeId: Boolean) = ContentValues().apply {
    if (includeId && id > 0) put("id", id)
    put("type", type)
    put("title", title)
    put("detail", detail)
    put("date", date)
}

// ------------------------------ JSON mapping ------------------------------

// تبدیل مدل‌ها به JSON برای فایل Backup.
private fun Customer.toJson() = JSONObject().apply {
    put("id", id); put("name", name); put("phone", phone); put("birthday", birthday)
    put("notes", notes); put("points", points); put("lastVisit", lastVisit)
}

private fun Appointment.toJson() = JSONObject().apply {
    put("id", id); put("customerName", customerName); put("phone", phone); put("service", service)
    put("staffName", staffName); put("date", date); put("time", time); put("durationMinutes", durationMinutes)
    put("price", price); put("status", status)
}

private fun StaffMember.toJson() = JSONObject().apply {
    put("id", id); put("name", name); put("role", role); put("phone", phone); put("active", active)
}

private fun ServiceItem.toJson() = JSONObject().apply {
    put("id", id); put("name", name); put("durationMinutes", durationMinutes); put("price", price)
}

private fun MoneyTransaction.toJson() = JSONObject().apply {
    put("id", id); put("type", type); put("title", title); put("amount", amount); put("date", date)
}

private fun MessageRecord.toJson() = JSONObject().apply {
    put("id", id); put("phone", phone); put("body", body); put("direction", direction); put("createdAt", createdAt)
}

private fun ReminderRecord.toJson() = JSONObject().apply {
    put("id", id); put("customerName", customerName); put("title", title); put("dueDate", dueDate); put("done", done)
}

private fun FeedbackRecord.toJson() = JSONObject().apply {
    put("id", id); put("customerName", customerName); put("rating", rating); put("note", note); put("date", date)
}

private fun CampaignRecord.toJson() = JSONObject().apply {
    put("id", id); put("type", type); put("title", title); put("detail", detail); put("date", date)
}

// Helper کوچک برای پیمایش امن JSONArray بدون تکرار حلقه‌ها.
private inline fun JSONArray.forEachObject(block: (JSONObject) -> Unit) {
    for (index in 0 until length()) {
        optJSONObject(index)?.let(block)
    }
}
