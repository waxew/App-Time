// این فایل نقطه ورود و رابط کاربری کامل App-Time است.
// UI با Jetpack Compose ساخته شده و تمام داده‌های اصلی از دیتابیس داخلی SQLite خوانده می‌شوند.
package ir.asteam.apptime

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Poll
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Sms
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ir.asteam.apptime.data.AppController
import ir.asteam.apptime.data.Appointment
import ir.asteam.apptime.data.CampaignRecord
import ir.asteam.apptime.data.Customer
import ir.asteam.apptime.data.FeedbackRecord
import ir.asteam.apptime.data.MessageRecord
import ir.asteam.apptime.data.MoneyTransaction
import ir.asteam.apptime.data.ReminderRecord
import ir.asteam.apptime.data.ServiceItem
import ir.asteam.apptime.data.StaffMember
import ir.asteam.apptime.util.UpdateChecker
import ir.asteam.apptime.util.formatMoney
import ir.asteam.apptime.util.isoToPersian
import ir.asteam.apptime.util.isValidIsoDate
import ir.asteam.apptime.util.nowStamp
import ir.asteam.apptime.util.todayIso
import ir.asteam.apptime.util.todayPersian
import ir.asteam.apptime.util.toPersianDigits
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

/** Activity اصلی برنامه؛ یک Activity و چند مقصد Compose برای حفظ Back Stack ساده و پایدار استفاده می‌شود. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTimeTheme {
                AppTimeRoot()
            }
        }
    }
}

// پالت اصلی رابط تیره مشابه طراحی مرجع پروژه.
private val AppBackground = Color(0xFF101217)
private val AppSurface = Color(0xFF1A1D23)
private val AppSurface2 = Color(0xFF242832)
private val AppText = Color(0xFFF7F8FA)
private val AppMuted = Color(0xFFA9B0BC)
private val Green = Color(0xFF2DD4A4)
private val Blue = Color(0xFF4C9CFF)
private val Pink = Color(0xFFFF5A8A)
private val Yellow = Color(0xFFFFC857)
private val Purple = Color(0xFFA978FF)
private val Cyan = Color(0xFF34D5E6)
private val Red = Color(0xFFFF6B6B)

// طرح رنگ Material 3 برنامه.
private val appColors = darkColorScheme(
    primary = Green,
    secondary = Blue,
    background = AppBackground,
    surface = AppSurface,
    onPrimary = Color(0xFF07120F),
    onBackground = AppText,
    onSurface = AppText
)

/** Theme واحد برای تمام صفحات. */
@Composable
private fun AppTimeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = appColors, content = content)
}

/** تمام مقصدهای اصلی برنامه. */
private sealed class Screen(val title: String) {
    data object Dashboard : Screen("داشبورد")
    data object Customers : Screen("پرونده مشتری")
    data object Appointments : Screen("نوبت‌دهی")
    data object Messages : Screen("دریافت پیام")
    data object SmsCenter : Screen("سامانه پیامک")
    data object RegionalSms : Screen("پیامک منطقه‌ای")
    data object DedicatedNumber : Screen("شماره اختصاصی")
    data object Birthday : Screen("تبریک تولد")
    data object Feedback : Screen("رضایت‌سنجی")
    data object Reminders : Screen("یادآوری ترمیم")
    data object ReturnCustomer : Screen("بازگشت مشتری")
    data object OnlineBooking : Screen("رزرو نوبت آنلاین")
    data object Website : Screen("سایت اختصاصی")
    data object Loyalty : Screen("باشگاه مشتریان")
    data object Lottery : Screen("قرعه‌کشی")
    data object Accounting : Screen("مالی و حسابداری")
    data object Reports : Screen("گزارش سیستم")
    data object Staff : Screen("پرسنل و خدمات")
    data object Settings : Screen("تنظیمات")
    data object Software : Screen("درباره نرم‌افزار")
    data object Update : Screen("بروزرسانی")
}

/** مدل کارت قابلیت داشبورد. */
private data class Feature(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color,
    val target: Screen,
    val badge: String? = null
)

// همه کارت‌های ویدیوی مرجع به مقصد واقعی متصل شده‌اند و دیگر Generic/بی‌عمل نیستند.
private val features = listOf(
    Feature("پرونده مشتری", "مدیریت اطلاعات و سوابق", Icons.Rounded.Person, Blue, Screen.Customers),
    Feature("نوبت‌دهی", "تقویم و مدیریت نوبت‌ها", Icons.Rounded.CalendarMonth, Green, Screen.Appointments),
    Feature("دریافت پیام", "صندوق پیام و یادداشت مکالمات", Icons.Rounded.Chat, Purple, Screen.Messages),
    Feature("سامانه پیامک", "ارسال از برنامه پیامک دستگاه", Icons.Rounded.Sms, Pink, Screen.SmsCenter),
    Feature("پیامک منطقه‌ای", "ساخت و نگهداری کمپین منطقه", Icons.Rounded.LocationOn, Cyan, Screen.RegionalSms, "NEW"),
    Feature("شماره اختصاصی", "تنظیم اطلاعات پنل پیامک", Icons.Rounded.Phone, Yellow, Screen.DedicatedNumber),
    Feature("تبریک تولد", "پیام آماده برای مشتریان", Icons.Rounded.Cake, Pink, Screen.Birthday),
    Feature("رضایت‌سنجی", "ثبت امتیاز و نظر", Icons.Rounded.Poll, Purple, Screen.Feedback),
    Feature("یادآوری ترمیم", "یادآوری تاریخ‌دار و اعلان محلی", Icons.Rounded.NotificationsActive, Yellow, Screen.Reminders, "NEW"),
    Feature("بازگشت مشتری", "شناسایی مشتریان غیرفعال", Icons.Rounded.Refresh, Green, Screen.ReturnCustomer),
    Feature("رزرو نوبت آنلاین", "مدیریت و اشتراک لینک رزرو", Icons.Rounded.EventAvailable, Blue, Screen.OnlineBooking, "NEW"),
    Feature("سایت اختصاصی", "ثبت و اشتراک سایت مجموعه", Icons.Rounded.Language, Cyan, Screen.Website),
    Feature("باشگاه مشتریان", "امتیاز و وفاداری", Icons.Rounded.Groups, Pink, Screen.Loyalty),
    Feature("قرعه‌کشی", "انتخاب تصادفی از مشتریان", Icons.Rounded.Celebration, Yellow, Screen.Lottery),
    Feature("مالی حسابداری", "درآمد، هزینه و صندوق", Icons.Rounded.AccountBalanceWallet, Green, Screen.Accounting),
    Feature("گزارش سیستم", "آمار و تحلیل عملکرد", Icons.Rounded.Assessment, Blue, Screen.Reports),
    Feature("دسترسی پرسنل", "پرسنل، نقش‌ها و وضعیت", Icons.Rounded.Badge, Purple, Screen.Staff),
    Feature("پرسنل و خدمات", "تعریف نیرو، خدمت و قیمت", Icons.Rounded.Build, Cyan, Screen.Staff)
)

/** ریشه برنامه؛ Controller، Drawer و Back Stack در این سطح نگهداری می‌شوند. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTimeRoot() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        val context = LocalContext.current
        val controller = remember { AppController(context.applicationContext) }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val backStack = remember { mutableStateListOf<Screen>(Screen.Dashboard) }
        val screen = backStack.last()

        // State پروفایل در سطح ریشه نگهداری می‌شود تا تغییر نام/تصویر فوراً در Drawer دیده شود.
        var profileName by remember { mutableStateOf(controller.profileName()) }
        var profileImageUri by remember { mutableStateOf(controller.profileImageUri()) }
        var editProfile by remember { mutableStateOf(false) }

        // انتخاب تصویر با Storage Access Framework؛ مجوز URI برای اجراهای بعدی پایدار می‌شود.
        val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                runCatching {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                controller.setProfileImageUri(uri.toString())
                profileImageUri = uri.toString()
            }
        }

        // Launcher درخواست مجوز اعلان در Android 13+.
        val notificationPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            controller.setNotificationEnabled(granted)
        }

        // Controller هنگام خروج کامل Activity دیتابیس را می‌بندد.
        DisposableEffect(Unit) {
            onDispose { controller.close() }
        }

        // ناوبری جدید فقط مقصد متفاوت را به Back Stack اضافه می‌کند.
        fun navigateTo(target: Screen) {
            if (backStack.last() != target) backStack.add(target)
        }

        // بازگشت یک سطح بدون خروج ناگهانی از برنامه.
        fun navigateBack() {
            if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
        }

        // خانه ریشه جدید Back Stack می‌شود.
        fun navigateHome() {
            backStack.clear()
            backStack.add(Screen.Dashboard)
        }

        // Back سیستم ابتدا Drawer را می‌بندد و سپس صفحه قبلی را باز می‌کند.
        BackHandler(enabled = drawerState.isOpen || backStack.size > 1) {
            if (drawerState.isOpen) {
                scope.launch { drawerState.close() }
            } else {
                navigateBack()
            }
        }

        // منوی همبرگری از سمت راست رابط RTL باز می‌شود.
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(320.dp),
                    drawerContainerColor = Color(0xFF171A20)
                ) {
                    Spacer(Modifier.height(24.dp))

                    // بلوک پروفایل بالای Drawer طبق استاندارد مشترک پروژه.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { imagePicker.launch(arrayOf("image/*")) }
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileAvatar(profileImageUri)
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Person, null, tint = Green, modifier = Modifier.size(17.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(profileName, color = AppText, fontWeight = FontWeight.Bold)
                        }
                        Text("برای تغییر تصویر لمس کنید", color = AppMuted, fontSize = 10.sp)
                    }

                    TextButton(
                        onClick = { editProfile = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(Icons.Rounded.Edit, null, tint = Green, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(5.dp))
                        Text("ویرایش نام", color = Green, fontSize = 11.sp)
                    }

                    Divider(color = Color.White.copy(alpha = 0.07f))

                    // تنظیمات و اشتراک‌گذاری عمداً دو گزینه اول منوی عملیاتی هستند.
                    DrawerItem("تنظیمات", Icons.Rounded.Settings, screen is Screen.Settings) {
                        navigateTo(Screen.Settings)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("اشتراک با دوستان", Icons.Rounded.Share, false) {
                        shareText(context, "اپلیکیشن تایم - مدیریت مشتری، نوبت، پرسنل و امور مالی")
                    }

                    Divider(color = Color.White.copy(alpha = 0.07f), modifier = Modifier.padding(vertical = 4.dp))

                    // گزینه‌های اصلی اختصاصی برنامه.
                    DrawerItem("خانه", Icons.Rounded.Home, screen is Screen.Dashboard) {
                        navigateHome()
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("مشتریان", Icons.Rounded.Person, screen is Screen.Customers) {
                        navigateTo(Screen.Customers)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("نوبت‌ها", Icons.Rounded.CalendarMonth, screen is Screen.Appointments) {
                        navigateTo(Screen.Appointments)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("مالی و حسابداری", Icons.Rounded.AccountBalanceWallet, screen is Screen.Accounting) {
                        navigateTo(Screen.Accounting)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("گزارش‌ها", Icons.Rounded.Assessment, screen is Screen.Reports) {
                        navigateTo(Screen.Reports)
                        scope.launch { drawerState.close() }
                    }

                    Divider(color = Color.White.copy(alpha = 0.07f), modifier = Modifier.padding(vertical = 4.dp))

                    DrawerItem("درباره نرم‌افزار", Icons.Rounded.Info, screen is Screen.Software) {
                        navigateTo(Screen.Software)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("بررسی بروزرسانی", Icons.Rounded.Update, screen is Screen.Update) {
                        navigateTo(Screen.Update)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("خروج", Icons.Rounded.ExitToApp, false) {
                        (context as? Activity)?.finish()
                    }

                    Spacer(Modifier.weight(1f))
                    Text(
                        "TIME • v${BuildConfig.VERSION_NAME}",
                        color = AppMuted,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp
                    )
                }
            }
        ) {
            Scaffold(
                containerColor = AppBackground,
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text(screen.title, color = AppText, fontSize = 18.sp)
                                if (screen is Screen.Dashboard) {
                                    Text("${todayPersian()} • خوش آمدید", color = AppMuted, fontSize = 10.sp)
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Rounded.Menu, contentDescription = "منوی اصلی", tint = AppText)
                            }
                        },
                        actions = {
                            if (backStack.size > 1) {
                                IconButton(onClick = ::navigateBack) {
                                    Icon(Icons.Rounded.ArrowBack, contentDescription = "بازگشت", tint = AppText)
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
                    )
                }
            ) { padding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    color = AppBackground
                ) {
                    when (screen) {
                        Screen.Dashboard -> DashboardScreen(controller, ::navigateTo)
                        Screen.Customers -> CustomersScreen(controller)
                        Screen.Appointments -> AppointmentsScreen(controller)
                        Screen.Messages -> MessagesScreen(controller)
                        Screen.SmsCenter -> SmsScreen(controller)
                        Screen.RegionalSms -> RegionalSmsScreen(controller)
                        Screen.DedicatedNumber -> DedicatedNumberScreen(controller)
                        Screen.Birthday -> BirthdayScreen(controller)
                        Screen.Feedback -> FeedbackScreen(controller)
                        Screen.Reminders -> RemindersScreen(controller)
                        Screen.ReturnCustomer -> ReturnCustomerScreen(controller)
                        Screen.OnlineBooking -> OnlineBookingScreen(controller)
                        Screen.Website -> WebsiteScreen(controller)
                        Screen.Loyalty -> LoyaltyScreen(controller)
                        Screen.Lottery -> LotteryScreen(controller)
                        Screen.Accounting -> AccountingScreen(controller)
                        Screen.Reports -> ReportsScreen(controller)
                        Screen.Staff -> StaffAndServicesScreen(controller)
                        Screen.Settings -> SettingsScreen(
                            controller = controller,
                            profileName = profileName,
                            onProfileNameChanged = {
                                profileName = it
                                controller.setProfileName(it)
                            },
                            onRequestNotificationPermission = {
                                if (Build.VERSION.SDK_INT >= 33) {
                                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }
                        )
                        Screen.Software -> SoftwareInfoScreen()
                        Screen.Update -> UpdateScreen()
                    }
                }
            }
        }

        // Dialog ویرایش نام پروفایل روی همه صفحات قابل نمایش است.
        if (editProfile) {
            var value by remember(profileName) { mutableStateOf(profileName) }
            SimpleTextDialog(
                title = "نام پروفایل",
                value = value,
                onValueChange = { value = it },
                placeholder = "نام مدیر یا مجموعه",
                onDismiss = { editProfile = false },
                onSave = {
                    if (value.isNotBlank()) {
                        profileName = value.trim()
                        controller.setProfileName(profileName)
                    }
                    editProfile = false
                }
            )
        }
    }
}

/** تصویر پروفایل با fallback آیکون کاربر. */
@Composable
private fun ProfileAvatar(uriString: String) {
    val context = LocalContext.current
    val bitmap = remember(uriString) {
        if (uriString.isBlank()) null else runCatching {
            context.contentResolver.openInputStream(Uri.parse(uriString))?.use { BitmapFactory.decodeStream(it) }
        }.getOrNull()
    }

    Box(
        modifier = Modifier
            .size(78.dp)
            .clip(CircleShape)
            .background(AppSurface2),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "تصویر پروفایل",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(Icons.Rounded.Person, null, tint = Green, modifier = Modifier.size(38.dp))
        }
    }
}

/** گزینه استاندارد Drawer. */
@Composable
private fun DrawerItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(icon, contentDescription = null) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Green.copy(alpha = 0.13f),
            unselectedContainerColor = Color.Transparent,
            selectedTextColor = Green,
            selectedIconColor = Green,
            unselectedTextColor = AppText,
            unselectedIconColor = AppMuted
        )
    )
}

/** داشبورد زنده؛ اعداد از دیتابیس محاسبه می‌شوند. */
@Composable
private fun DashboardScreen(controller: AppController, onOpen: (Screen) -> Unit) {
    val today = todayIso()
    val todayAppointments = controller.appointments.count { it.date == today && it.status != "لغو شد" }
    val completedToday = controller.appointments.count { it.date == today && it.status == "انجام شد" }
    val monthPrefix = today.take(7)
    val monthIncome = controller.transactions
        .filter { it.type == "income" && it.date.startsWith(monthPrefix) }
        .sumOf { it.amount }
    val pendingReminders = controller.reminders.count { !it.done }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("نوبت امروز", toPersianDigits(todayAppointments.toString()), "${toPersianDigits(completedToday.toString())} انجام شده", Green, Modifier.weight(1f))
                MetricCard("مشتریان", toPersianDigits(controller.customers.size.toString()), "+ داده داخلی", Blue, Modifier.weight(1f))
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("وضعیت این ماه", color = AppText, fontSize = 15.sp)
                            Text("روند نوبت‌ها بر اساس داده ثبت‌شده", color = AppMuted, fontSize = 10.sp)
                        }
                        Text("${formatMoney(monthIncome)} تومان", color = Green, fontSize = 12.sp)
                    }
                    Spacer(Modifier.height(14.dp))
                    MiniBarChart(dashboardBars(controller.appointments))
                }
            }
        }

        item {
            Text("امکانات", color = AppText, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            FeatureGrid(onOpen)
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SmallStat("پیام‌ها", toPersianDigits(controller.messages.size.toString()), Purple, Modifier.weight(1f))
                SmallStat("یادآوری باز", toPersianDigits(pendingReminders.toString()), Yellow, Modifier.weight(1f))
            }
        }

        item {
            Text(
                "Develop by AS Team Group • ${BuildConfig.VERSION_NAME}",
                color = Color.White.copy(alpha = 0.28f),
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

/** ساخت داده نمودار بر اساس تعداد نوبت‌های ثبت‌شده در 12 تاریخ اخیر. */
private fun dashboardBars(appointments: List<Appointment>): List<Int> {
    val grouped = appointments.groupingBy { it.date }.eachCount().toSortedMap().values.takeLast(12)
    if (grouped.isEmpty()) return List(12) { 18 }
    val maxValue = max(1, grouped.maxOrNull() ?: 1)
    val scaled = grouped.map { 18 + (it * 64 / maxValue) }
    return List(max(0, 12 - scaled.size)) { 18 } + scaled
}

/** کارت آماری. */
@Composable
private fun MetricCard(title: String, value: String, sub: String, accent: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).background(accent, CircleShape))
                Spacer(Modifier.width(7.dp))
                Text(title, color = AppMuted, fontSize = 12.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text(value, color = AppText, fontSize = 24.sp)
            Text(sub, color = accent, fontSize = 10.sp)
        }
    }
}

/** نمودار میله‌ای سبک بدون کتابخانه Chart خارجی. */
@Composable
private fun MiniBarChart(values: List<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            Box(
                Modifier
                    .weight(1f)
                    .height(value.coerceIn(10, 88).dp)
                    .background(
                        if (index >= values.lastIndex - 2) Green else Blue.copy(alpha = 0.55f),
                        RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                    )
            )
        }
    }
}

/** شبکه دو ستونه امکانات. */
@Composable
private fun FeatureGrid(onOpen: (Screen) -> Unit) {
    val rows = (features.size + 1) / 2
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height((rows * 112).dp),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(features) { feature -> FeatureCard(feature) { onOpen(feature.target) } }
    }
}

/** کارت یک قابلیت داشبورد. */
@Composable
private fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(17.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.padding(13.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(feature.accent.copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(feature.icon, null, tint = feature.accent, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(9.dp))
                    Text(feature.title, color = AppText, fontSize = 13.sp)
                }
                Spacer(Modifier.height(9.dp))
                Text(feature.subtitle, color = AppMuted, fontSize = 10.sp)
            }
            feature.badge?.let { badge ->
                Box(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(feature.accent.copy(alpha = 0.16f), RoundedCornerShape(50))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(badge, color = feature.accent, fontSize = 9.sp)
                }
            }
        }
    }
}

/** کارت آماری کوچک. */
@Composable
private fun SmallStat(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(accent.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                Text(value.take(1).ifBlank { "۰" }, color = accent)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, color = AppMuted, fontSize = 10.sp)
                Text(value, color = AppText, fontSize = 17.sp)
            }
        }
    }
}

/** پرونده مشتری با جستجو و CRUD کامل. */
@Composable
private fun CustomersScreen(controller: AppController) {
    var query by remember { mutableStateOf("") }
    var editing by remember { mutableStateOf<Customer?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<Customer?>(null) }

    val filtered = controller.customers.filter {
        query.isBlank() || it.name.contains(query, true) || it.phone.contains(query)
    }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("جستجوی نام یا شماره", color = AppMuted) },
                leadingIcon = { Icon(Icons.Rounded.Search, null, tint = AppMuted) },
                singleLine = true
            )
        }
        item {
            Button(
                onClick = { editing = null; showEditor = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Icon(Icons.Rounded.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("افزودن پرونده مشتری")
            }
        }

        if (filtered.isEmpty()) {
            item { EmptyCard("هنوز مشتری ثبت نشده است.") }
        }

        items(filtered, key = { it.id }) { customer ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(14.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(44.dp).background(Blue.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Person, null, tint = Blue)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(customer.name, color = AppText, fontWeight = FontWeight.Bold)
                            Text(customer.phone.ifBlank { "بدون شماره" }, color = AppMuted, fontSize = 11.sp)
                            Text("امتیاز: ${toPersianDigits(customer.points.toString())}", color = Pink, fontSize = 10.sp)
                        }
                        IconButton(onClick = { editing = customer; showEditor = true }) {
                            Icon(Icons.Rounded.Edit, "ویرایش", tint = Green)
                        }
                        IconButton(onClick = { deleting = customer }) {
                            Icon(Icons.Rounded.Delete, "حذف", tint = Red)
                        }
                    }
                    if (customer.notes.isNotBlank()) {
                        Text(customer.notes, color = AppMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 6.dp))
                    }
                }
            }
        }
    }

    if (showEditor) {
        CustomerDialog(
            initial = editing,
            onDismiss = { showEditor = false },
            onSave = { item ->
                if (item.id == 0L) controller.addCustomer(item) else controller.updateCustomer(item)
                showEditor = false
            }
        )
    }

    deleting?.let { customer ->
        ConfirmDeleteDialog(
            title = "حذف ${customer.name}؟",
            onDismiss = { deleting = null },
            onConfirm = { controller.deleteCustomer(customer.id); deleting = null }
        )
    }
}

/** فرم افزودن/ویرایش مشتری. */
@Composable
private fun CustomerDialog(initial: Customer?, onDismiss: () -> Unit, onSave: (Customer) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial?.name ?: "") }
    var phone by remember(initial) { mutableStateOf(initial?.phone ?: "") }
    var birthday by remember(initial) { mutableStateOf(initial?.birthday ?: "") }
    var notes by remember(initial) { mutableStateOf(initial?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "مشتری جدید" else "ویرایش مشتری") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("نام و نام خانوادگی") }, singleLine = true)
                OutlinedTextField(
                    phone,
                    { phone = it },
                    label = { Text("شماره موبایل") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                OutlinedTextField(birthday, { birthday = it }, label = { Text("تولد (مثال 05-18 یا 1995-05-18)") }, singleLine = true)
                OutlinedTextField(notes, { notes = it }, label = { Text("یادداشت") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isNotBlank()) {
                    onSave(
                        Customer(
                            id = initial?.id ?: 0,
                            name = name.trim(),
                            phone = phone.trim(),
                            birthday = birthday.trim(),
                            notes = notes.trim(),
                            points = initial?.points ?: 0,
                            lastVisit = initial?.lastVisit ?: ""
                        )
                    )
                }
            }) { Text("ذخیره") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** صفحه نوبت‌دهی با فیلتر تاریخ، افزودن، ویرایش، حذف و وضعیت. */
@Composable
private fun AppointmentsScreen(controller: AppController) {
    var selectedDate by remember { mutableStateOf(todayIso()) }
    var showEditor by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Appointment?>(null) }
    var deleting by remember { mutableStateOf<Appointment?>(null) }

    val dayItems = controller.appointments.filter { it.date == selectedDate }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text(isoToPersian(selectedDate), color = AppText, fontSize = 18.sp)
                    Text("برای تاریخ دیگر مقدار yyyy-MM-dd را وارد کنید", color = AppMuted, fontSize = 10.sp)
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { selectedDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("تاریخ") },
                        singleLine = true
                    )
                }
            }
        }
        item {
            Button(
                onClick = { editing = null; showEditor = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Icon(Icons.Rounded.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("ثبت نوبت جدید")
            }
        }

        if (dayItems.isEmpty()) item { EmptyCard("برای این تاریخ نوبتی ثبت نشده است.") }

        items(dayItems, key = { it.id }) { appointment ->
            AppointmentCard(
                item = appointment,
                onStatus = { status -> controller.setAppointmentStatus(appointment, status) },
                onEdit = { editing = appointment; showEditor = true },
                onDelete = { deleting = appointment }
            )
        }
    }

    if (showEditor) {
        AppointmentDialog(
            initial = editing,
            controller = controller,
            defaultDate = selectedDate,
            onDismiss = { showEditor = false },
            onSave = { item ->
                if (item.id == 0L) controller.addAppointment(item) else controller.updateAppointment(item)
                selectedDate = item.date
                showEditor = false
            }
        )
    }

    deleting?.let { appointment ->
        ConfirmDeleteDialog(
            title = "حذف نوبت ${appointment.customerName}؟",
            onDismiss = { deleting = null },
            onConfirm = { controller.deleteAppointment(appointment.id); deleting = null }
        )
    }
}

/** کارت یک نوبت. */
@Composable
private fun AppointmentCard(
    item: Appointment,
    onStatus: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val nextStatus = when (item.status) {
        "در انتظار" -> "تأیید شد"
        "تأیید شد" -> "انجام شد"
        "انجام شد" -> "در انتظار"
        else -> "در انتظار"
    }
    val statusColor = when (item.status) {
        "انجام شد" -> Green
        "تأیید شد" -> Blue
        "لغو شد" -> Red
        else -> Yellow
    }

    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(item.time, color = Green, fontSize = 18.sp, modifier = Modifier.width(62.dp))
                Column(Modifier.weight(1f)) {
                    Text(item.customerName, color = AppText, fontWeight = FontWeight.Bold)
                    Text("${item.service} • ${toPersianDigits(item.durationMinutes.toString())} دقیقه", color = AppMuted, fontSize = 10.sp)
                    if (item.price > 0) Text("${formatMoney(item.price)} تومان", color = Green, fontSize = 10.sp)
                }
                IconButton(onClick = onEdit) { Icon(Icons.Rounded.Edit, "ویرایش", tint = Blue) }
                IconButton(onClick = onDelete) { Icon(Icons.Rounded.Delete, "حذف", tint = Red) }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { onStatus(nextStatus) }) { Text(item.status, color = statusColor) }
                if (item.status != "لغو شد") {
                    TextButton(onClick = { onStatus("لغو شد") }) { Text("لغو", color = Red, fontSize = 11.sp) }
                }
            }
        }
    }
}

/** فرم نوبت با پیشنهاد اولین مشتری/خدمت/پرسنل برای سریع‌تر شدن ثبت. */
@Composable
private fun AppointmentDialog(
    initial: Appointment?,
    controller: AppController,
    defaultDate: String,
    onDismiss: () -> Unit,
    onSave: (Appointment) -> Unit
) {
    val firstCustomer = controller.customers.firstOrNull()
    val firstService = controller.services.firstOrNull()
    val firstStaff = controller.staff.firstOrNull { it.active }

    var customerName by remember(initial) { mutableStateOf(initial?.customerName ?: firstCustomer?.name.orEmpty()) }
    var phone by remember(initial) { mutableStateOf(initial?.phone ?: firstCustomer?.phone.orEmpty()) }
    var service by remember(initial) { mutableStateOf(initial?.service ?: firstService?.name.orEmpty()) }
    var staffName by remember(initial) { mutableStateOf(initial?.staffName ?: firstStaff?.name.orEmpty()) }
    var date by remember(initial) { mutableStateOf(initial?.date ?: defaultDate) }
    var time by remember(initial) { mutableStateOf(initial?.time ?: "10:00") }
    var duration by remember(initial) { mutableStateOf((initial?.durationMinutes ?: firstService?.durationMinutes ?: 45).toString()) }
    var price by remember(initial) { mutableStateOf((initial?.price ?: firstService?.price ?: 0).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "ثبت نوبت" else "ویرایش نوبت") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(7.dp), modifier = Modifier.height(460.dp)) {
                item { OutlinedTextField(customerName, { customerName = it }, label = { Text("نام مشتری") }, singleLine = true) }
                item { OutlinedTextField(phone, { phone = it }, label = { Text("شماره") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)) }
                item { OutlinedTextField(service, { service = it }, label = { Text("خدمت") }, singleLine = true) }
                item { OutlinedTextField(staffName, { staffName = it }, label = { Text("پرسنل") }, singleLine = true) }
                item { OutlinedTextField(date, { date = it }, label = { Text("تاریخ yyyy-MM-dd") }, singleLine = true) }
                item { OutlinedTextField(time, { time = it }, label = { Text("ساعت HH:mm") }, singleLine = true) }
                item { OutlinedTextField(duration, { duration = it }, label = { Text("مدت (دقیقه)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) }
                item { OutlinedTextField(price, { price = it }, label = { Text("مبلغ") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) }
                if (controller.customers.isNotEmpty()) {
                    item { Text("پیشنهاد مشتری: ${controller.customers.take(3).joinToString("، ") { it.name }}", color = AppMuted, fontSize = 9.sp) }
                }
                if (controller.services.isNotEmpty()) {
                    item { Text("خدمات: ${controller.services.take(4).joinToString("، ") { it.name }}", color = AppMuted, fontSize = 9.sp) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (customerName.isNotBlank() && service.isNotBlank() && isValidIsoDate(date)) {
                    onSave(
                        Appointment(
                            id = initial?.id ?: 0,
                            customerName = customerName.trim(),
                            phone = phone.trim(),
                            service = service.trim(),
                            staffName = staffName.trim(),
                            date = date.trim(),
                            time = time.trim(),
                            durationMinutes = duration.toIntOrNull()?.coerceAtLeast(1) ?: 45,
                            price = price.toLongOrNull()?.coerceAtLeast(0) ?: 0,
                            status = initial?.status ?: "در انتظار"
                        )
                    )
                }
            }) { Text("ذخیره") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** صندوق پیام داخلی؛ دسترسی مستقیم READ_SMS عمداً درخواست نمی‌شود تا با سیاست‌های اندروید سازگار بماند. */
@Composable
private fun MessagesScreen(controller: AppController) {
    var showAdd by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<MessageRecord?>(null) }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            InfoCard("صندوق پیام", "برای حفظ حریم خصوصی، برنامه پیامک‌های شخصی گوشی را بدون مجوز ویژه نمی‌خواند. پیام‌های مرتبط با مشتری یا یادداشت مکالمه را اینجا ثبت کنید.", Purple)
        }
        item {
            Button(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("ثبت پیام ورودی")
            }
        }
        if (controller.messages.isEmpty()) item { EmptyCard("هنوز پیامی ثبت نشده است.") }
        items(controller.messages, key = { it.id }) { message ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (message.direction == "out") Icons.Rounded.Send else Icons.Rounded.Chat, null, tint = if (message.direction == "out") Green else Purple)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(if (message.direction == "out") "ارسالی • ${message.phone}" else "دریافتی • ${message.phone}", color = AppText, fontSize = 11.sp)
                        Text(message.body, color = AppMuted, fontSize = 11.sp)
                        Text(message.createdAt, color = AppMuted.copy(alpha = 0.7f), fontSize = 9.sp)
                    }
                    IconButton(onClick = { deleting = message }) { Icon(Icons.Rounded.Delete, "حذف", tint = Red) }
                }
            }
        }
    }

    if (showAdd) {
        MessageDialog(onDismiss = { showAdd = false }) { phone, body ->
            controller.addMessage(MessageRecord(phone = phone, body = body, direction = "in", createdAt = nowStamp()))
            showAdd = false
        }
    }
    deleting?.let { item -> ConfirmDeleteDialog("حذف پیام؟", { deleting = null }) { controller.deleteMessage(item.id); deleting = null } }
}

/** فرم ثبت دستی پیام ورودی. */
@Composable
private fun MessageDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var phone by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("پیام ورودی") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(phone, { phone = it }, label = { Text("شماره/نام فرستنده") }, singleLine = true)
                OutlinedTextField(body, { body = it }, label = { Text("متن پیام") }, modifier = Modifier.height(120.dp))
            }
        },
        confirmButton = { TextButton(onClick = { if (body.isNotBlank()) onSave(phone.trim(), body.trim()) }) { Text("ثبت") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** مرکز ارسال پیامک با Intent امن سیستم و بدون مجوز SEND_SMS. */
@Composable
private fun SmsScreen(controller: AppController) {
    var phone by remember { mutableStateOf(controller.customers.firstOrNull()?.phone.orEmpty()) }
    var body by remember { mutableStateOf("") }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            InfoCard("ارسال امن پیامک", "دکمه ارسال، برنامه پیامک گوشی را با متن آماده باز می‌کند؛ ارسال نهایی در اختیار کاربر است و هزینه طبق اپراتور محاسبه می‌شود.", Pink)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    OutlinedTextField(phone, { phone = it }, label = { Text("شماره گیرنده") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(body, { body = it }, label = { Text("متن پیام") }, modifier = Modifier.fillMaxWidth().height(130.dp))
                    Button(
                        onClick = { controller.sendSms(phone, body) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = phone.isNotBlank() && body.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    ) {
                        Icon(Icons.Rounded.Send, null); Spacer(Modifier.width(6.dp)); Text("باز کردن برنامه پیامک")
                    }
                    OutlinedButton(
                        onClick = { controller.sendBulkSms(controller.customers.map { it.phone }, body) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = controller.customers.any { it.phone.isNotBlank() } && body.isNotBlank()
                    ) {
                        Icon(Icons.Rounded.Groups, null); Spacer(Modifier.width(6.dp)); Text("ارسال به همه مشتریان")
                    }
                }
            }
        }
        item { Text("انتخاب سریع مشتری", color = AppText) }
        items(controller.customers.take(10), key = { it.id }) { customer ->
            ActionRow(customer.name, customer.phone, Icons.Rounded.Person, Blue) { phone = customer.phone }
        }
    }
}

/** سازنده کمپین منطقه‌ای؛ ارسال واقعی منطقه‌ای نیازمند قرارداد با سرویس‌دهنده پیامک است. */
@Composable
private fun RegionalSmsScreen(controller: AppController) {
    var region by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var deleting by remember { mutableStateOf<CampaignRecord?>(null) }
    val campaigns = controller.campaigns.filter { it.type == "regional" }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            InfoCard("کمپین منطقه‌ای", "بانک شماره منطقه‌ای باید از ارائه‌دهنده مجاز پیامک دریافت شود. App-Time کمپین، منطقه و متن را ذخیره می‌کند تا پس از اتصال API همان ساختار استفاده شود.", Cyan)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(region, { region = it }, label = { Text("منطقه / شهر / محدوده") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(body, { body = it }, label = { Text("متن کمپین") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                    Button(
                        onClick = {
                            if (region.isNotBlank() && body.isNotBlank()) {
                                controller.addCampaign(CampaignRecord(type = "regional", title = region.trim(), detail = body.trim(), date = todayIso()))
                                region = ""; body = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    ) { Icon(Icons.Rounded.Save, null); Spacer(Modifier.width(6.dp)); Text("ذخیره کمپین") }
                }
            }
        }
        items(campaigns, key = { it.id }) { campaign ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Campaign, null, tint = Cyan)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(campaign.title, color = AppText)
                        Text(campaign.detail, color = AppMuted, fontSize = 10.sp)
                    }
                    IconButton(onClick = { deleting = campaign }) { Icon(Icons.Rounded.Delete, null, tint = Red) }
                }
            }
        }
    }
    deleting?.let { item -> ConfirmDeleteDialog("حذف کمپین؟", { deleting = null }) { controller.deleteCampaign(item.id); deleting = null } }
}

/** تنظیمات شماره اختصاصی و ارائه‌دهنده پیامک. */
@Composable
private fun DedicatedNumberScreen(controller: AppController) {
    var provider by remember { mutableStateOf(controller.smsProvider()) }
    var sender by remember { mutableStateOf(controller.senderNumber()) }
    var saved by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InfoCard("شماره اختصاصی", "نام سرویس‌دهنده و شماره ارسال را ثبت کنید. کلید API عمداً در نسخه عمومی برنامه ذخیره نمی‌شود تا اطلاعات حساس داخل APK قرار نگیرد.", Yellow)
        OutlinedTextField(provider, { provider = it; saved = false }, label = { Text("سرویس‌دهنده پیامک") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(sender, { sender = it; saved = false }, label = { Text("شماره/خط ارسال") }, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = { controller.setSmsProvider(provider); controller.setSenderNumber(sender); saved = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) { Icon(Icons.Rounded.Save, null); Spacer(Modifier.width(6.dp)); Text("ذخیره") }
        if (saved) Text("تنظیمات ذخیره شد.", color = Green)
    }
}

/** لیست تولدها و ارسال پیام آماده. */
@Composable
private fun BirthdayScreen(controller: AppController) {
    val customers = controller.customers.filter { it.birthday.isNotBlank() }
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { InfoCard("تبریک تولد", "تاریخ تولد در پرونده مشتری ذخیره می‌شود. برای ارسال، برنامه پیامک سیستم با متن آماده باز می‌شود.", Pink) }
        if (customers.isEmpty()) item { EmptyCard("در پرونده مشتریان هنوز تاریخ تولد ثبت نشده است.") }
        items(customers, key = { it.id }) { customer ->
            ActionRow(customer.name, "تولد: ${customer.birthday}", Icons.Rounded.Cake, Pink) {
                controller.sendSms(customer.phone, "${customer.name} عزیز، تولدت مبارک 🌷 با آرزوی بهترین‌ها برای شما.")
            }
        }
    }
}

/** رضایت‌سنجی مشتریان. */
@Composable
private fun FeedbackScreen(controller: AppController) {
    var showAdd by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<FeedbackRecord?>(null) }
    val average = if (controller.feedback.isEmpty()) 0.0 else controller.feedback.map { it.rating }.average()

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { MetricCard("میانگین رضایت", String.format("%.1f / 5", average), "${controller.feedback.size} پاسخ", Purple, Modifier.fillMaxWidth()) }
        item { Button(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("ثبت رضایت") } }
        if (controller.feedback.isEmpty()) item { EmptyCard("هنوز امتیازی ثبت نشده است.") }
        items(controller.feedback, key = { it.id }) { item ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("★".repeat(item.rating), color = Yellow, fontSize = 16.sp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.customerName.ifBlank { "مشتری" }, color = AppText)
                        if (item.note.isNotBlank()) Text(item.note, color = AppMuted, fontSize = 10.sp)
                    }
                    IconButton(onClick = { deleting = item }) { Icon(Icons.Rounded.Delete, null, tint = Red) }
                }
            }
        }
    }
    if (showAdd) FeedbackDialog(controller.customers.map { it.name }, { showAdd = false }) { item -> controller.addFeedback(item); showAdd = false }
    deleting?.let { item -> ConfirmDeleteDialog("حذف نظر؟", { deleting = null }) { controller.deleteFeedback(item.id); deleting = null } }
}

/** فرم رضایت با امتیاز عددی 1 تا 5. */
@Composable
private fun FeedbackDialog(customerNames: List<String>, onDismiss: () -> Unit, onSave: (FeedbackRecord) -> Unit) {
    var customer by remember { mutableStateOf(customerNames.firstOrNull().orEmpty()) }
    var rating by remember { mutableStateOf("5") }
    var note by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("ثبت رضایت") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(customer, { customer = it }, label = { Text("نام مشتری") })
                OutlinedTextField(rating, { rating = it }, label = { Text("امتیاز 1 تا 5") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(note, { note = it }, label = { Text("نظر") })
            }
        },
        confirmButton = { TextButton(onClick = { onSave(FeedbackRecord(customerName = customer.trim(), rating = rating.toIntOrNull()?.coerceIn(1, 5) ?: 5, note = note.trim(), date = todayIso())) }) { Text("ثبت") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** یادآوری ترمیم/پیگیری با اعلان محلی. */
@Composable
private fun RemindersScreen(controller: AppController) {
    var showAdd by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<ReminderRecord?>(null) }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Button(onClick = { showAdd = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("یادآوری جدید") } }
        if (controller.reminders.isEmpty()) item { EmptyCard("یادآوری ثبت نشده است.") }
        items(controller.reminders, key = { it.id }) { item ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (item.done) Icons.Rounded.CheckCircle else Icons.Rounded.NotificationsActive, null, tint = if (item.done) Green else Yellow)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.title, color = AppText)
                        Text("${item.customerName} • ${isoToPersian(item.dueDate)}", color = AppMuted, fontSize = 10.sp)
                    }
                    TextButton(onClick = { controller.setReminderDone(item.id, !item.done) }) { Text(if (item.done) "بازگشت" else "انجام شد", color = Green, fontSize = 10.sp) }
                    IconButton(onClick = { deleting = item }) { Icon(Icons.Rounded.Delete, null, tint = Red) }
                }
            }
        }
    }
    if (showAdd) ReminderDialog(controller.customers.map { it.name }, { showAdd = false }) { item -> controller.addReminder(item); showAdd = false }
    deleting?.let { item -> ConfirmDeleteDialog("حذف یادآوری؟", { deleting = null }) { controller.deleteReminder(item.id); deleting = null } }
}

/** فرم یادآوری. */
@Composable
private fun ReminderDialog(customers: List<String>, onDismiss: () -> Unit, onSave: (ReminderRecord) -> Unit) {
    var customer by remember { mutableStateOf(customers.firstOrNull().orEmpty()) }
    var title by remember { mutableStateOf("یادآوری مراجعه") }
    var date by remember { mutableStateOf(todayIso()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("یادآوری جدید") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(customer, { customer = it }, label = { Text("مشتری") })
                OutlinedTextField(title, { title = it }, label = { Text("عنوان") })
                OutlinedTextField(date, { date = it }, label = { Text("تاریخ yyyy-MM-dd") })
            }
        },
        confirmButton = { TextButton(onClick = { if (title.isNotBlank() && isValidIsoDate(date)) onSave(ReminderRecord(customerName = customer.trim(), title = title.trim(), dueDate = date.trim())) }) { Text("ذخیره") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** شناسایی مشتریان غیرفعال و ارسال پیام بازگشت. */
@Composable
private fun ReturnCustomerScreen(controller: AppController) {
    val today = LocalDate.now()
    val inactive = controller.customers.filter { customer ->
        if (customer.lastVisit.isBlank()) true else runCatching {
            ChronoUnit.DAYS.between(LocalDate.parse(customer.lastVisit), today) >= 30
        }.getOrDefault(true)
    }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { InfoCard("بازگشت مشتری", "مشتریانی که حداقل 30 روز مراجعه ثبت‌شده نداشته‌اند نمایش داده می‌شوند.", Green) }
        if (inactive.isEmpty()) item { EmptyCard("مشتری غیرفعال پیدا نشد.") }
        items(inactive, key = { it.id }) { customer ->
            ActionRow(customer.name, if (customer.lastVisit.isBlank()) "بدون مراجعه ثبت‌شده" else "آخرین مراجعه: ${isoToPersian(customer.lastVisit)}", Icons.Rounded.Refresh, Green) {
                controller.sendSms(customer.phone, "${customer.name} عزیز، مدتی است از شما بی‌خبریم. خوشحال می‌شویم دوباره میزبانتان باشیم.")
            }
        }
    }
}

/** تنظیم و اشتراک لینک رزرو آنلاین خارجی. */
@Composable
private fun OnlineBookingScreen(controller: AppController) {
    val context = LocalContext.current
    var url by remember { mutableStateOf(controller.bookingUrl()) }
    var saved by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InfoCard("رزرو آنلاین", "App-Time اطلاعات داخل گوشی را آفلاین نگه می‌دارد. برای رزرو از بیرون گوشی، آدرس صفحه رزرو وب خود را اینجا ثبت و برای مشتریان اشتراک‌گذاری کنید.", Blue)
        OutlinedTextField(url, { url = it; saved = false }, label = { Text("لینک رزرو") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { controller.setBookingUrl(url); saved = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Icon(Icons.Rounded.Save, null); Spacer(Modifier.width(6.dp)); Text("ذخیره") }
        OutlinedButton(onClick = { shareText(context, url) }, modifier = Modifier.fillMaxWidth(), enabled = url.isNotBlank()) { Icon(Icons.Rounded.Share, null); Spacer(Modifier.width(6.dp)); Text("اشتراک لینک") }
        OutlinedButton(onClick = { openUrl(context, url) }, modifier = Modifier.fillMaxWidth(), enabled = url.isNotBlank()) { Icon(Icons.Rounded.OpenInBrowser, null); Spacer(Modifier.width(6.dp)); Text("باز کردن") }
        if (saved) Text("لینک ذخیره شد.", color = Green)
    }
}

/** تنظیم سایت اختصاصی مجموعه. */
@Composable
private fun WebsiteScreen(controller: AppController) {
    val context = LocalContext.current
    var url by remember { mutableStateOf(controller.websiteUrl()) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        InfoCard("سایت اختصاصی", "اگر سایت یا صفحه معرفی مجموعه دارید، لینک آن را ذخیره کنید تا از داخل برنامه سریع باز یا اشتراک‌گذاری شود.", Cyan)
        OutlinedTextField(url, { url = it }, label = { Text("آدرس سایت") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { controller.setWebsiteUrl(url) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Icon(Icons.Rounded.Save, null); Spacer(Modifier.width(6.dp)); Text("ذخیره") }
        OutlinedButton(onClick = { openUrl(context, url) }, modifier = Modifier.fillMaxWidth(), enabled = url.isNotBlank()) { Icon(Icons.Rounded.Language, null); Spacer(Modifier.width(6.dp)); Text("باز کردن سایت") }
        OutlinedButton(onClick = { shareText(context, url) }, modifier = Modifier.fillMaxWidth(), enabled = url.isNotBlank()) { Icon(Icons.Rounded.Share, null); Spacer(Modifier.width(6.dp)); Text("اشتراک") }
    }
}

/** باشگاه مشتریان و مدیریت امتیاز. */
@Composable
private fun LoyaltyScreen(controller: AppController) {
    val sorted = controller.customers.sortedByDescending { it.points }
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { InfoCard("باشگاه مشتریان", "هر نوبت با وضعیت «انجام شد» 10 امتیاز به مشتری هم‌شماره اضافه می‌کند. امتیاز را دستی نیز می‌توانید اصلاح کنید.", Pink) }
        if (sorted.isEmpty()) item { EmptyCard("مشتری ثبت نشده است.") }
        items(sorted, key = { it.id }) { customer ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Star, null, tint = Yellow)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(customer.name, color = AppText)
                        Text("${toPersianDigits(customer.points.toString())} امتیاز", color = Pink, fontSize = 11.sp)
                    }
                    TextButton(onClick = { controller.changePoints(customer.id, 10) }) { Text("+10", color = Green) }
                    TextButton(onClick = { controller.changePoints(customer.id, -10) }) { Text("-10", color = Red) }
                }
            }
        }
    }
}

/** قرعه‌کشی تصادفی از مشتریان ثبت‌شده. */
@Composable
private fun LotteryScreen(controller: AppController) {
    var winner by remember { mutableStateOf<Customer?>(null) }
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(Modifier.size(92.dp).background(Yellow.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Celebration, null, tint = Yellow, modifier = Modifier.size(46.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("قرعه‌کشی بین ${toPersianDigits(controller.customers.size.toString())} مشتری", color = AppText, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { if (controller.customers.isNotEmpty()) winner = controller.customers[Random.nextInt(controller.customers.size)] },
            enabled = controller.customers.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) { Text("انتخاب برنده") }
        Spacer(Modifier.height(20.dp))
        winner?.let {
            Text("برنده: ${it.name}", color = Yellow, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(it.phone, color = AppMuted)
        }
    }
}

/** مالی و حسابداری با تراکنش واقعی و جمع درآمد/هزینه. */
@Composable
private fun AccountingScreen(controller: AppController) {
    var type by remember { mutableStateOf("income") }
    var showAdd by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<MoneyTransaction?>(null) }
    val income = controller.transactions.filter { it.type == "income" }.sumOf { it.amount }
    val expense = controller.transactions.filter { it.type == "expense" }.sumOf { it.amount }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("درآمد", "${formatMoney(income)}", "تومان", Green, Modifier.weight(1f))
                MetricCard("هزینه", "${formatMoney(expense)}", "تومان", Pink, Modifier.weight(1f))
            }
        }
        item { MetricCard("مانده", "${formatMoney(income - expense)}", "تومان", Blue, Modifier.fillMaxWidth()) }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { type = "income"; showAdd = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("ثبت درآمد") }
                Button(onClick = { type = "expense"; showAdd = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Pink)) { Text("ثبت هزینه") }
            }
        }
        if (controller.transactions.isEmpty()) item { EmptyCard("تراکنش مالی ثبت نشده است.") }
        items(controller.transactions, key = { it.id }) { item ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.AccountBalanceWallet, null, tint = if (item.type == "income") Green else Pink)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.title, color = AppText)
                        Text(isoToPersian(item.date), color = AppMuted, fontSize = 9.sp)
                    }
                    Text("${if (item.type == "income") "+" else "-"}${formatMoney(item.amount)}", color = if (item.type == "income") Green else Pink, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { deleting = item }) { Icon(Icons.Rounded.Delete, null, tint = Red) }
                }
            }
        }
    }
    if (showAdd) TransactionDialog(type, { showAdd = false }) { item -> controller.addTransaction(item); showAdd = false }
    deleting?.let { item -> ConfirmDeleteDialog("حذف تراکنش؟", { deleting = null }) { controller.deleteTransaction(item.id); deleting = null } }
}

/** فرم تراکنش. */
@Composable
private fun TransactionDialog(type: String, onDismiss: () -> Unit, onSave: (MoneyTransaction) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(todayIso()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (type == "income") "ثبت درآمد" else "ثبت هزینه") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("عنوان") })
                OutlinedTextField(amount, { amount = it.filter(Char::isDigit) }, label = { Text("مبلغ") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                if (amount.toLongOrNull() != null) Text("${formatMoney(amount.toLong())} تومان", color = Green, fontSize = 10.sp)
                OutlinedTextField(date, { date = it }, label = { Text("تاریخ yyyy-MM-dd") })
            }
        },
        confirmButton = { TextButton(onClick = { if (title.isNotBlank() && amount.toLongOrNull() != null && isValidIsoDate(date)) onSave(MoneyTransaction(type = type, title = title.trim(), amount = amount.toLong(), date = date.trim())) }) { Text("ذخیره") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** گزارش‌های محاسبه‌شده از داده واقعی. */
@Composable
private fun ReportsScreen(controller: AppController) {
    val completed = controller.appointments.count { it.status == "انجام شد" }
    val cancelled = controller.appointments.count { it.status == "لغو شد" }
    val income = controller.transactions.filter { it.type == "income" }.sumOf { it.amount }
    val expense = controller.transactions.filter { it.type == "expense" }.sumOf { it.amount }
    val avgRating = if (controller.feedback.isEmpty()) 0.0 else controller.feedback.map { it.rating }.average()

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { MetricCard("کل مشتریان", toPersianDigits(controller.customers.size.toString()), "پرونده فعال", Blue, Modifier.fillMaxWidth()) }
        item { MetricCard("نوبت انجام‌شده", toPersianDigits(completed.toString()), "لغو: ${toPersianDigits(cancelled.toString())}", Green, Modifier.fillMaxWidth()) }
        item { MetricCard("خالص مالی", "${formatMoney(income - expense)}", "تومان", Yellow, Modifier.fillMaxWidth()) }
        item { MetricCard("رضایت", String.format("%.1f / 5", avgRating), "${controller.feedback.size} پاسخ", Purple, Modifier.fillMaxWidth()) }
        item { ActionRow("پیام‌های ثبت‌شده", "${controller.messages.size} رکورد", Icons.Rounded.Sms, Pink) }
        item { ActionRow("یادآوری‌های باز", "${controller.reminders.count { !it.done }} مورد", Icons.Rounded.NotificationsActive, Yellow) }
        item { ActionRow("پرسنل فعال", "${controller.staff.count { it.active }} نفر", Icons.Rounded.Badge, Cyan) }
    }
}

/** مدیریت پرسنل و خدمات در یک صفحه. */
@Composable
private fun StaffAndServicesScreen(controller: AppController) {
    var staffEditor by remember { mutableStateOf<StaffMember?>(null) }
    var showStaffEditor by remember { mutableStateOf(false) }
    var serviceEditor by remember { mutableStateOf<ServiceItem?>(null) }
    var showServiceEditor by remember { mutableStateOf(false) }
    var deleteStaff by remember { mutableStateOf<StaffMember?>(null) }
    var deleteService by remember { mutableStateOf<ServiceItem?>(null) }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("پرسنل", color = AppText, fontSize = 17.sp, fontWeight = FontWeight.Bold) }
        item { Button(onClick = { staffEditor = null; showStaffEditor = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("افزودن پرسنل") } }
        if (controller.staff.isEmpty()) item { EmptyCard("پرسنلی ثبت نشده است.") }
        items(controller.staff, key = { "staff-${it.id}" }) { item ->
            EditableActionRow(
                title = item.name,
                subtitle = "${item.role} • ${if (item.active) "فعال" else "غیرفعال"}",
                icon = Icons.Rounded.Badge,
                accent = Purple,
                onEdit = { staffEditor = item; showStaffEditor = true },
                onDelete = { deleteStaff = item }
            )
        }
        item { Spacer(Modifier.height(8.dp)); Divider(color = Color.White.copy(alpha = 0.08f)); Spacer(Modifier.height(8.dp)); Text("خدمات", color = AppText, fontSize = 17.sp, fontWeight = FontWeight.Bold) }
        item { Button(onClick = { serviceEditor = null; showServiceEditor = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Cyan)) { Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("افزودن خدمت") } }
        items(controller.services, key = { "service-${it.id}" }) { item ->
            EditableActionRow(
                title = item.name,
                subtitle = "${toPersianDigits(item.durationMinutes.toString())} دقیقه • ${formatMoney(item.price)} تومان",
                icon = Icons.Rounded.Build,
                accent = Cyan,
                onEdit = { serviceEditor = item; showServiceEditor = true },
                onDelete = { deleteService = item }
            )
        }
    }

    if (showStaffEditor) StaffDialog(staffEditor, { showStaffEditor = false }) { item -> if (item.id == 0L) controller.addStaff(item) else controller.updateStaff(item); showStaffEditor = false }
    if (showServiceEditor) ServiceDialog(serviceEditor, { showServiceEditor = false }) { item -> if (item.id == 0L) controller.addService(item) else controller.updateService(item); showServiceEditor = false }
    deleteStaff?.let { item -> ConfirmDeleteDialog("حذف ${item.name}؟", { deleteStaff = null }) { controller.deleteStaff(item.id); deleteStaff = null } }
    deleteService?.let { item -> ConfirmDeleteDialog("حذف ${item.name}؟", { deleteService = null }) { controller.deleteService(item.id); deleteService = null } }
}

/** فرم پرسنل. */
@Composable
private fun StaffDialog(initial: StaffMember?, onDismiss: () -> Unit, onSave: (StaffMember) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial?.name ?: "") }
    var role by remember(initial) { mutableStateOf(initial?.role ?: "کارشناس") }
    var phone by remember(initial) { mutableStateOf(initial?.phone ?: "") }
    var active by remember(initial) { mutableStateOf(initial?.active ?: true) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "پرسنل جدید" else "ویرایش پرسنل") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("نام") })
                OutlinedTextField(role, { role = it }, label = { Text("نقش") })
                OutlinedTextField(phone, { phone = it }, label = { Text("شماره") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                Row(verticalAlignment = Alignment.CenterVertically) { Text("فعال", modifier = Modifier.weight(1f)); Switch(active, { active = it }) }
            }
        },
        confirmButton = { TextButton(onClick = { if (name.isNotBlank()) onSave(StaffMember(id = initial?.id ?: 0, name = name.trim(), role = role.trim(), phone = phone.trim(), active = active)) }) { Text("ذخیره") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** فرم خدمات. */
@Composable
private fun ServiceDialog(initial: ServiceItem?, onDismiss: () -> Unit, onSave: (ServiceItem) -> Unit) {
    var name by remember(initial) { mutableStateOf(initial?.name ?: "") }
    var duration by remember(initial) { mutableStateOf((initial?.durationMinutes ?: 45).toString()) }
    var price by remember(initial) { mutableStateOf((initial?.price ?: 0).toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "خدمت جدید" else "ویرایش خدمت") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("نام خدمت") })
                OutlinedTextField(duration, { duration = it.filter(Char::isDigit) }, label = { Text("مدت دقیقه") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                OutlinedTextField(price, { price = it.filter(Char::isDigit) }, label = { Text("قیمت") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = { TextButton(onClick = { if (name.isNotBlank()) onSave(ServiceItem(id = initial?.id ?: 0, name = name.trim(), durationMinutes = duration.toIntOrNull()?.coerceAtLeast(1) ?: 45, price = price.toLongOrNull()?.coerceAtLeast(0) ?: 0)) }) { Text("ذخیره") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** تنظیمات پایدار، اعلان‌ها و Backup/Restore. */
@Composable
private fun SettingsScreen(
    controller: AppController,
    profileName: String,
    onProfileNameChanged: (String) -> Unit,
    onRequestNotificationPermission: () -> Unit
) {
    val context = LocalContext.current
    var notifications by remember { mutableStateOf(controller.notificationEnabled()) }
    var appointmentReminder by remember { mutableStateOf(controller.appointmentReminderEnabled()) }
    var name by remember(profileName) { mutableStateOf(profileName) }
    var status by remember { mutableStateOf("") }

    // ساخت فایل JSON نسخه پشتیبان.
    val createBackup = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            status = runCatching {
                context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
                    writer.write(controller.exportBackup())
                } ?: error("امکان نوشتن فایل وجود ندارد")
                "نسخه پشتیبان با موفقیت ذخیره شد."
            }.getOrElse { "خطا در پشتیبان‌گیری: ${it.message.orEmpty()}" }
        }
    }

    // انتخاب و بازیابی فایل JSON.
    val openBackup = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            status = runCatching {
                val text = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                    ?: error("فایل قابل خواندن نیست")
                controller.importBackup(text)
                "اطلاعات با موفقیت بازیابی شد."
            }.getOrElse { "خطا در بازیابی: ${it.message.orEmpty()}" }
        }
    }

    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("پروفایل", color = AppText, fontWeight = FontWeight.Bold)
                    OutlinedTextField(name, { name = it }, label = { Text("نام مدیر/مجموعه") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { if (name.isNotBlank()) onProfileNameChanged(name.trim()) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("ذخیره نام") }
                }
            }
        }
        item {
            ToggleRow("اعلان‌ها", "اعلان نوبت‌ها و یادآوری‌های مهم", Icons.Rounded.Notifications, notifications) { enabled ->
                notifications = enabled
                controller.setNotificationEnabled(enabled)
                if (enabled && Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    onRequestNotificationPermission()
                }
            }
        }
        item {
            ToggleRow("یادآوری نوبت", "یک ساعت قبل از نوبت اعلان محلی", Icons.Rounded.NotificationsActive, appointmentReminder) {
                appointmentReminder = it
                controller.setAppointmentReminderEnabled(it)
            }
        }
        item {
            ActionRow("ساخت نسخه پشتیبان", "خروجی JSON از اطلاعات داخلی", Icons.Rounded.Save, Blue) {
                createBackup.launch("App-Time-backup-${todayIso()}.json")
            }
        }
        item {
            ActionRow("بازیابی نسخه پشتیبان", "جایگزینی اطلاعات با فایل انتخابی", Icons.Rounded.Refresh, Purple) {
                openBackup.launch(arrayOf("application/json", "text/plain"))
            }
        }
        if (status.isNotBlank()) item { Text(status, color = if (status.startsWith("خطا")) Red else Green, fontSize = 11.sp) }
        item { InfoCard("حریم خصوصی", "داده‌های مشتری، نوبت، مالی و تنظیمات در حافظه داخلی برنامه ذخیره می‌شوند. فقط بررسی بروزرسانی و لینک‌های خارجی نیاز به اینترنت دارند.", Purple) }
    }
}

/** ردیف تنظیم Switch. */
@Composable
private fun ToggleRow(title: String, subtitle: String, icon: ImageVector, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Green)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = AppText)
                Text(subtitle, color = AppMuted, fontSize = 10.sp)
            }
            Switch(checked = checked, onCheckedChange = onChecked)
        }
    }
}

/** صفحه درباره نرم‌افزار مطابق قالب جدید پروژه؛ بدون Package Name. */
@Composable
private fun SoftwareInfoScreen() {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(72.dp).background(Green.copy(alpha = 0.12f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Star, null, tint = Green, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text("تایم", color = AppText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("نسخه ${BuildConfig.VERSION_NAME}", color = Green, fontSize = 12.sp)
        Spacer(Modifier.height(18.dp))
        Text(
            "تایم یک نرم‌افزار مدیریت کسب‌وکارهای نوبت‌محور است که پرونده مشتری، نوبت‌ها، پرسنل و خدمات، یادآوری، ارتباط با مشتری، باشگاه مشتریان و امور مالی را در یک محیط یکپارچه مدیریت می‌کند. داده‌های اصلی برنامه به‌صورت محلی در گوشی ذخیره می‌شوند.",
            color = AppMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Divider(color = Color.White.copy(alpha = 0.08f))
        Spacer(Modifier.height(18.dp))
        Text("راه‌های ارتباطی با ما:", color = AppText, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "AS.Developers.Support@Gmail.Com",
            color = Green,
            modifier = Modifier.clickable {
                runCatching {
                    context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:AS.Developers.Support@Gmail.Com")))
                }
            }
        )
        Spacer(Modifier.weight(1f))
        Divider(color = Color.White.copy(alpha = 0.08f))
        Spacer(Modifier.height(10.dp))
        Text("Develop by AS Team Group", color = AppText, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(4.dp))
        Text("App-Time • ${BuildConfig.VERSION_NAME}", color = AppMuted, fontSize = 10.sp)
        Spacer(Modifier.height(18.dp))
    }
}

/** بررسی واقعی نسخه جدید از version.json ریپو. */
@Composable
private fun UpdateScreen() {
    val context = LocalContext.current
    var checking by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("برای مقایسه نسخه نصب‌شده با آخرین انتشار، دکمه زیر را بزنید.") }
    var downloadUrl by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.size(72.dp).background(Green.copy(alpha = 0.13f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Update, null, tint = Green, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("نسخه نصب‌شده: ${BuildConfig.VERSION_NAME}", color = AppText, fontSize = 17.sp)
        Spacer(Modifier.height(10.dp))
        Text(status, color = AppMuted, fontSize = 12.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {
                checking = true
                status = "در حال بررسی..."
                UpdateChecker.check(BuildConfig.VERSION_CODE) { result ->
                    checking = false
                    result.onSuccess { update ->
                        downloadUrl = update.downloadUrl
                        status = if (update.updateAvailable) {
                            "نسخه ${update.latestVersionName} موجود است.\n${update.notes}"
                        } else {
                            "نسخه شما بروز است."
                        }
                    }.onFailure {
                        status = "بررسی نسخه انجام نشد. اتصال اینترنت را بررسی کنید."
                    }
                }
            },
            enabled = !checking,
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) { Text(if (checking) "در حال بررسی" else "بررسی نسخه جدید") }
        if (downloadUrl.isNotBlank()) {
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = { openUrl(context, downloadUrl) }) { Text("صفحه دانلود") }
        }
    }
}

/** ردیف عمومی قابل لمس. */
@Composable
private fun ActionRow(title: String, subtitle: String, icon: ImageVector, accent: Color, onClick: (() -> Unit)? = null) {
    Card(
        modifier = if (onClick != null) Modifier.fillMaxWidth().clickable { onClick() } else Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).background(accent.copy(alpha = 0.14f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = accent)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = AppText)
                Text(subtitle, color = AppMuted, fontSize = 10.sp)
            }
        }
    }
}

/** ردیف دارای دکمه ویرایش/حذف. */
@Composable
private fun EditableActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = accent)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = AppText)
                Text(subtitle, color = AppMuted, fontSize = 10.sp)
            }
            IconButton(onClick = onEdit) { Icon(Icons.Rounded.Edit, "ویرایش", tint = Blue) }
            IconButton(onClick = onDelete) { Icon(Icons.Rounded.Delete, "حذف", tint = Red) }
        }
    }
}

/** کارت توضیحی بالای ماژول‌ها. */
@Composable
private fun InfoCard(title: String, body: String, accent: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Rounded.Info, null, tint = accent)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, color = AppText, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(body, color = AppMuted, fontSize = 10.sp)
            }
        }
    }
}

/** کارت حالت خالی. */
@Composable
private fun EmptyCard(message: String) {
    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Text(message, color = AppMuted, modifier = Modifier.fillMaxWidth().padding(20.dp), textAlign = TextAlign.Center)
    }
}

/** Dialog متنی ساده برای نام پروفایل و موارد مشابه. */
@Composable
private fun SimpleTextDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { OutlinedTextField(value, onValueChange, placeholder = { Text(placeholder) }, modifier = Modifier.fillMaxWidth()) },
        confirmButton = { TextButton(onClick = onSave) { Text("ذخیره") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** تأیید حذف برای جلوگیری از حذف تصادفی اطلاعات. */
@Composable
private fun ConfirmDeleteDialog(title: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text("این عملیات قابل بازگشت نیست؛ در صورت نیاز قبل از حذف نسخه پشتیبان تهیه کنید.") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("حذف", color = Red) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("انصراف") } }
    )
}

/** Share Sheet استاندارد اندروید. */
private fun shareText(context: Context, text: String) {
    if (text.isBlank()) return
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    runCatching { context.startActivity(Intent.createChooser(intent, "اشتراک‌گذاری")) }
}

/** باز کردن URL با Browser سیستم؛ اگر Scheme وارد نشده باشد https اضافه می‌شود. */
private fun openUrl(context: Context, rawUrl: String) {
    if (rawUrl.isBlank()) return
    val normalized = if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) rawUrl else "https://$rawUrl"
    runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(normalized))) }
}
