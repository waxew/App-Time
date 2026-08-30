// این فایل مدل‌های داده اصلی App-Time را تعریف می‌کند.
// هر مدل دقیقاً نماینده یک رکورد ذخیره‌شده در دیتابیس داخلی برنامه است.
package ir.asteam.apptime.data

// مدل پرونده مشتری؛ اطلاعات تماس، تولد، امتیاز وفاداری و آخرین مراجعه را نگه می‌دارد.
data class Customer(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val birthday: String = "",
    val notes: String = "",
    val points: Int = 0,
    val lastVisit: String = ""
)

// مدل نوبت؛ برای تقویم، گزارش درآمد و یادآوری مراجعه استفاده می‌شود.
data class Appointment(
    val id: Long = 0,
    val customerName: String,
    val phone: String = "",
    val service: String,
    val staffName: String = "",
    val date: String,
    val time: String,
    val durationMinutes: Int = 45,
    val price: Long = 0,
    val status: String = "در انتظار"
)

// مدل پرسنل؛ نقش و وضعیت فعال/غیرفعال هر عضو تیم را نگه می‌دارد.
data class StaffMember(
    val id: Long = 0,
    val name: String,
    val role: String,
    val phone: String = "",
    val active: Boolean = true
)

// مدل خدمات؛ نام، مدت انجام و قیمت هر خدمت قابل رزرو را نگه می‌دارد.
data class ServiceItem(
    val id: Long = 0,
    val name: String,
    val durationMinutes: Int = 45,
    val price: Long = 0
)

// مدل تراکنش مالی؛ type فقط income یا expense است.
data class MoneyTransaction(
    val id: Long = 0,
    val type: String,
    val title: String,
    val amount: Long,
    val date: String
)

// مدل پیام داخلی/پیام ثبت‌شده؛ برای صندوق پیام و سابقه ارسال استفاده می‌شود.
data class MessageRecord(
    val id: Long = 0,
    val phone: String,
    val body: String,
    val direction: String,
    val createdAt: String
)

// مدل یادآوری؛ برای یادآوری ترمیم، پیگیری مشتری و کارهای تاریخ‌دار استفاده می‌شود.
data class ReminderRecord(
    val id: Long = 0,
    val customerName: String,
    val title: String,
    val dueDate: String,
    val done: Boolean = false
)

// مدل رضایت‌سنجی؛ امتیاز 1 تا 5 و یادداشت مشتری را ذخیره می‌کند.
data class FeedbackRecord(
    val id: Long = 0,
    val customerName: String,
    val rating: Int,
    val note: String,
    val date: String
)

// مدل کمپین؛ پیش‌نویس‌های پیامکی و کمپین‌های بازگشت مشتری را نگه می‌دارد.
data class CampaignRecord(
    val id: Long = 0,
    val type: String,
    val title: String,
    val detail: String,
    val date: String
)
