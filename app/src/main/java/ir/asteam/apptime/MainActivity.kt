// این فایل نقطه ورود اصلی رابط کاربری App-Time است.
// تمام اجزای نسخه فعلی با Jetpack Compose ساخته شده‌اند و توضیح هر بخش کنار همان کد نوشته شده است.
package ir.asteam.apptime

// Intent برای اشتراک‌گذاری برنامه و باز کردن ایمیل پشتیبانی استفاده می‌شود.
import android.content.Intent
// Uri آدرس mailto را برای برنامه ایمیل می‌سازد.
import android.net.Uri
// Bundle داده اولیه Activity را هنگام ساخته‌شدن دریافت می‌کند.
import android.os.Bundle
// ComponentActivity میزبان اصلی رابط Compose است.
import androidx.activity.ComponentActivity
// BackHandler رفتار دکمه Back فیزیکی/سیستمی اندروید را کنترل می‌کند.
import androidx.activity.compose.BackHandler
// setContent محتوای Activity را به درخت Compose متصل می‌کند.
import androidx.activity.compose.setContent
// background برای رنگ پس‌زمینه Boxها و نمودارها استفاده می‌شود.
import androidx.compose.foundation.background
// clickable کارت‌ها و لینک ایمیل را قابل لمس می‌کند.
import androidx.compose.foundation.clickable
// کلاس‌های Layout زیر ساختار صفحه را می‌سازند.
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
// LazyColumn فهرست‌های عمودی را فقط در محدوده قابل مشاهده رندر می‌کند.
import androidx.compose.foundation.lazy.LazyColumn
// Grid برای نمایش کارت‌های امکانات در دو ستون استفاده می‌شود.
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
// items نسخه LazyColumn برای تکرار داده‌ها است.
import androidx.compose.foundation.lazy.items
// شکل‌های گرد کارت‌ها و آواتارها را تعریف می‌کنند.
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// مجموعه آیکون‌های Material مورد استفاده برنامه.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Assessment
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Poll
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Sms
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Update
// کامپوننت‌های Material 3 رابط اصلی را تشکیل می‌دهند.
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDrawerState
// APIهای State در Compose وضعیت صفحه، فرم و تنظیمات را نگهداری می‌کنند.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
// کلاس‌های پایه UI برای Alignment، Modifier و رنگ‌ها.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Coroutine برای باز و بسته کردن Drawer که API تعلیقی دارد استفاده می‌شود.
import kotlinx.coroutines.launch

/**
 * Activity اصلی برنامه.
 * این Activity فقط یک‌بار ساخته می‌شود و کل جابه‌جایی صفحات داخل Compose انجام می‌شود.
 */
class MainActivity : ComponentActivity() {
    // onCreate اولین متد چرخه عمر هنگام باز شدن برنامه است.
    override fun onCreate(savedInstanceState: Bundle?) {
        // اجرای رفتار پایه Activity الزامی است.
        super.onCreate(savedInstanceState)

        // درخت Compose به Activity متصل و Theme عمومی برنامه اعمال می‌شود.
        setContent {
            AppTimeTheme {
                AppTimeRoot()
            }
        }
    }
}

// رنگ اصلی پس‌زمینه تیره برنامه.
private val AppBackground = Color(0xFF111318)
// رنگ کارت‌های سطح اول.
private val AppSurface = Color(0xFF1B1E24)
// رنگ سطح دوم برای کنترل‌های داخلی کارت.
private val AppSurface2 = Color(0xFF242832)
// رنگ متن اصلی.
private val AppText = Color(0xFFF6F7F9)
// رنگ متن‌های توضیحی و کم‌اهمیت‌تر.
private val AppMuted = Color(0xFFAAB0BC)
// رنگ‌های Accent ثابت رابط که برای دسته‌های مختلف استفاده می‌شوند.
private val Green = Color(0xFF2DD4A4)
private val Blue = Color(0xFF4C9CFF)
private val Pink = Color(0xFFFF5A8A)
private val Yellow = Color(0xFFFFC857)
private val Purple = Color(0xFFA978FF)
private val Cyan = Color(0xFF34D5E6)

// ColorScheme تیره Material 3 از رنگ‌های پروژه ساخته می‌شود.
private val appColors = darkColorScheme(
    primary = Green,
    secondary = Blue,
    background = AppBackground,
    surface = AppSurface,
    onPrimary = Color(0xFF07120F),
    onBackground = AppText,
    onSurface = AppText
)

/** Theme واحد برنامه تا همه صفحات رنگ‌بندی مشترک داشته باشند. */
@Composable
private fun AppTimeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = appColors,
        content = content
    )
}

/**
 * لیست تمام مقصدهای ناوبری نسخه فعلی.
 * استفاده از sealed class اجازه می‌دهد when همه صفحات را به‌صورت type-safe پوشش دهد.
 */
private sealed class Screen(val title: String) {
    data object Dashboard : Screen("داشبورد")
    data object Customers : Screen("پرونده مشتری")
    data object Appointments : Screen("نوبت دهی")
    data object SmsCenter : Screen("سامانه پیامک")
    data object Staff : Screen("پرسنل و خدمات")
    data object Accounting : Screen("مالی و حسابداری")
    data object Reports : Screen("گزارش سیستم")
    data object Settings : Screen("تنظیمات")
    data object About : Screen("درباره ما")
    data object Contact : Screen("تماس با ما")
    data object Software : Screen("درباره نرم افزار")
    data object Update : Screen("بروزرسانی")

    // Generic برای امکاناتی است که رابط پایه دارند اما Backend آن‌ها در نسخه‌های بعد تکمیل می‌شود.
    data class Generic(val name: String) : Screen(name)
}

/** مدل یک کارت امکانات روی داشبورد. */
private data class Feature(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color,
    val badge: String? = null,
    val target: Screen = Screen.Generic(title)
)

// اطلاعات کارت‌های داشبورد در یک لیست ثابت نگهداری می‌شود تا UI تکراری نوشته نشود.
private val features = listOf(
    Feature("پرونده مشتری", "مدیریت اطلاعات و سوابق", Icons.Rounded.Person, Blue, target = Screen.Customers),
    Feature("نوبت دهی", "تقویم و مدیریت نوبت‌ها", Icons.Rounded.CalendarMonth, Green, target = Screen.Appointments),
    Feature("دریافت پیام", "پیام‌های ورودی مشتریان", Icons.Rounded.Chat, Purple, "۱۰"),
    Feature("سامانه پیامک", "ارسال پیام و اطلاع‌رسانی", Icons.Rounded.Sms, Pink, "۲۱۷", Screen.SmsCenter),
    Feature("پیامک منطقه‌ای", "ارسال هدفمند بر اساس منطقه", Icons.Rounded.LocationOn, Cyan, "NEW"),
    Feature("شماره اختصاصی", "مدیریت شماره ارسال پیامک", Icons.Rounded.Phone, Yellow),
    Feature("تبریک تولد", "پیام خودکار برای مشتری", Icons.Rounded.Cake, Pink),
    Feature("رضایت سنجی", "دریافت نظر و امتیاز", Icons.Rounded.Poll, Purple),
    Feature("یادآوری ترمیم", "یادآوری هوشمند خدمات", Icons.Rounded.NotificationsActive, Yellow, "NEW"),
    Feature("بازگشت مشتری", "کمپین بازگشت مشتریان", Icons.Rounded.Refresh, Green),
    Feature("رزرو نوبت آنلاین", "لینک رزرو برای مشتری", Icons.Rounded.EventAvailable, Blue, "NEW", Screen.Appointments),
    Feature("سایت اختصاصی", "صفحه آنلاین مجموعه", Icons.Rounded.Language, Cyan),
    Feature("باشگاه مشتریان", "امتیاز، تعامل و وفاداری", Icons.Rounded.Groups, Pink),
    Feature("قرعه کشی", "کمپین و انتخاب برنده", Icons.Rounded.Celebration, Yellow),
    Feature("مالی حسابداری", "درآمد، هزینه و صندوق", Icons.Rounded.AccountBalanceWallet, Green, target = Screen.Accounting),
    Feature("گزارش سیستم", "آمار و تحلیل عملکرد", Icons.Rounded.Assessment, Blue, target = Screen.Reports),
    Feature("دسترسی پرسنل", "سطح دسترسی و نقش‌ها", Icons.Rounded.Badge, Purple, target = Screen.Staff),
    Feature("پرسنل و خدمات", "تعریف نیرو و خدمات", Icons.Rounded.Build, Cyan, target = Screen.Staff)
)

/**
 * ریشه رابط و ناوبری برنامه.
 * مهم‌ترین اصلاح v1.1 در این تابع است: به‌جای یک متغیر screen، تاریخچه واقعی صفحات نگهداری می‌شود.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTimeRoot() {
    // کل رابط فارسی است؛ بنابراین جهت چیدمان در سطح ریشه RTL می‌شود.
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        // وضعیت باز/بسته بودن منوی همبرگری نگهداری می‌شود.
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        // Scope برای اجرای عملیات تعلیقی Drawer استفاده می‌شود.
        val scope = rememberCoroutineScope()

        // Back Stack از داشبورد شروع می‌شود و هر مقصد جدید به انتهای آن اضافه می‌شود.
        val backStack = remember {
            mutableStateListOf<Screen>(Screen.Dashboard)
        }

        // آخرین عضو Back Stack همان صفحه فعلی است.
        val screen = backStack.last()

        // Context برای Intentهای سیستم مثل Share و Email استفاده می‌شود.
        val context = LocalContext.current

        // تابع ناوبری تنها وقتی مقصد جدید با صفحه فعلی متفاوت باشد آن را به تاریخچه اضافه می‌کند.
        val navigateTo: (Screen) -> Unit = { target ->
            if (backStack.last() != target) {
                backStack.add(target)
            }
        }

        // بازگشت داخلی فقط یک صفحه از تاریخچه حذف می‌کند و Activity را نمی‌بندد.
        val navigateBack: () -> Unit = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        }

        // رفتن به خانه کل تاریخچه را پاک می‌کند تا Dashboard ریشه جدید باشد.
        val navigateHome: () -> Unit = {
            backStack.clear()
            backStack.add(Screen.Dashboard)
        }

        // دکمه Back گوشی ابتدا Drawer را می‌بندد؛ در غیر این صورت به صفحه قبلی برمی‌گردد.
        BackHandler(enabled = drawerState.isOpen || backStack.size > 1) {
            if (drawerState.isOpen) {
                scope.launch {
                    drawerState.close()
                }
            } else {
                navigateBack()
            }
        }

        // Drawer اصلی برنامه از سمت راست در UI فارسی باز می‌شود.
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                // ظاهر سطح داخلی Drawer مشخص می‌شود.
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(310.dp),
                    drawerContainerColor = Color(0xFF171A20)
                ) {
                    // فضای بالای برند برنامه.
                    Spacer(Modifier.height(30.dp))

                    // عنوان و توضیح کوتاه بالای منو.
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text("TIME", color = Green, fontSize = 27.sp)
                        Text("مدیریت هوشمند کسب‌وکار", color = AppMuted, fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(18.dp))
                    Divider(color = Color.White.copy(alpha = 0.06f))

                    // گزینه خانه کاربر را به ریشه برنامه برمی‌گرداند.
                    DrawerItem("خانه", Icons.Rounded.Home, screen is Screen.Dashboard) {
                        navigateHome()
                        scope.launch { drawerState.close() }
                    }

                    // صفحه تنظیمات به Back Stack اضافه می‌شود.
                    DrawerItem("تنظیمات", Icons.Rounded.Settings, screen is Screen.Settings) {
                        navigateTo(Screen.Settings)
                        scope.launch { drawerState.close() }
                    }

                    // Share Sheet استاندارد اندروید برای معرفی برنامه استفاده می‌شود.
                    DrawerItem("معرفی به دوستان", Icons.Rounded.Share, false) {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "اپلیکیشن تایم - مدیریت نوبت و مشتری")
                        }

                        // runCatching جلوی Crash در دستگاه‌هایی با Share Handler معیوب را می‌گیرد.
                        runCatching {
                            context.startActivity(Intent.createChooser(shareIntent, "اشتراک گذاری"))
                        }
                    }

                    // صفحات اطلاعاتی برنامه.
                    DrawerItem("درباره ما", Icons.Rounded.Info, screen is Screen.About) {
                        navigateTo(Screen.About)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("تماس با ما", Icons.Rounded.ContactSupport, screen is Screen.Contact) {
                        navigateTo(Screen.Contact)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("درباره نرم افزار", Icons.Rounded.Star, screen is Screen.Software) {
                        navigateTo(Screen.Software)
                        scope.launch { drawerState.close() }
                    }
                    DrawerItem("بررسی بروزرسانی", Icons.Rounded.Update, screen is Screen.Update) {
                        navigateTo(Screen.Update)
                        scope.launch { drawerState.close() }
                    }

                    // Spacer منو را طوری می‌چیند که نسخه در پایین Drawer باقی بماند.
                    Spacer(Modifier.weight(1f))

                    // ورژن به‌صورت خودکار از Gradle/BuildConfig خوانده می‌شود تا با انتشارها هماهنگ باشد.
                    Text(
                        "نسخه ${BuildConfig.VERSION_NAME}",
                        color = AppMuted,
                        modifier = Modifier.padding(22.dp),
                        fontSize = 12.sp
                    )
                }
            }
        ) {
            // Scaffold نوار بالا و محتوای هر صفحه را یکپارچه می‌کند.
            Scaffold(
                containerColor = AppBackground,
                topBar = {
                    TopAppBar(
                        // عنوان صفحه فعلی در App Bar نمایش داده می‌شود.
                        title = {
                            Column {
                                Text(screen.title, color = AppText, fontSize = 18.sp)
                                if (screen is Screen.Dashboard) {
                                    Text("سلام، خوش آمدید", color = AppMuted, fontSize = 11.sp)
                                }
                            }
                        },
                        // طبق استاندارد پروژه، آیکون همبرگری همیشه در سمت راست Top Bar قرار دارد.
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    contentDescription = "منوی اصلی",
                                    tint = AppText
                                )
                            }
                        },
                        // در صفحات داخلی دکمه بازگشت در سمت مقابل همبرگری نمایش داده می‌شود.
                        actions = {
                            if (backStack.size > 1) {
                                IconButton(onClick = navigateBack) {
                                    Icon(
                                        Icons.Rounded.ArrowBack,
                                        contentDescription = "بازگشت به صفحه قبل",
                                        tint = AppText
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
                    )
                }
            ) { padding ->
                // Surface فضای باقی‌مانده بعد از Top Bar را برای صفحه فعلی می‌سازد.
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    color = AppBackground
                ) {
                    // بر اساس مقصد جاری، Composable متناظر نمایش داده می‌شود.
                    when (val current = screen) {
                        Screen.Dashboard -> DashboardScreen(navigateTo)
                        Screen.Customers -> CustomersScreen()
                        Screen.Appointments -> AppointmentScreen()
                        Screen.SmsCenter -> SmsScreen()
                        Screen.Staff -> StaffScreen()
                        Screen.Accounting -> AccountingScreen()
                        Screen.Reports -> ReportsScreen()
                        Screen.Settings -> SettingsScreen()
                        Screen.About -> CenterInfo(
                            "گروه توسعه و برنامه نویسی AS Team",
                            "تمامی حقوق مربوط به این برنامه انحصاری میباشد"
                        )
                        Screen.Contact -> ContactScreen()
                        Screen.Software -> SoftwareInfoScreen()
                        Screen.Update -> UpdateScreen()
                        is Screen.Generic -> GenericFeatureScreen(current.name)
                    }
                }
            }
        }
    }
}

/** یک ردیف قابل انتخاب داخل منوی همبرگری. */
@Composable
private fun DrawerItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
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

/** داشبورد اصلی شامل آمار، نمودار و شبکه امکانات. */
@Composable
private fun DashboardScreen(onOpen: (Screen) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // کارت‌های آمار بالای صفحه.
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("نوبت امروز", "۱۰", "۳ نوبت باقی مانده", Green, Modifier.weight(1f))
                MetricCard("مشتریان", "۲۱۷", "+۱۲ این ماه", Blue, Modifier.weight(1f))
            }
        }

        // کارت وضعیت این ماه و نمودار میله‌ای نمونه.
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("وضعیت این ماه", color = AppText, fontSize = 15.sp)
                            Text("روند نوبت‌ها و مشتریان", color = AppMuted, fontSize = 11.sp)
                        }
                        Text("+۱۸٪", color = Green, fontSize = 14.sp)
                    }
                    Spacer(Modifier.height(14.dp))
                    MiniBarChart()
                }
            }
        }

        // شبکه اصلی امکانات.
        item {
            Text("امکانات", color = AppText, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
            Spacer(Modifier.height(8.dp))
            FeatureGrid(onOpen)
        }

        // خلاصه موجودی پیامک و وضعیت اشتراک.
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallStat("موجودی پیامک", "۲۳۳", Green, Modifier.weight(1f))
                SmallStat("بسته اشتراک", "فعال", Blue, Modifier.weight(1f))
            }
        }

        // فوتر برنامه.
        item {
            Text(
                "TIME • ALL RIGHTS RESERVED © 2026",
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

/** کارت آماری قابل استفاده مجدد. */
@Composable
private fun MetricCard(
    title: String,
    value: String,
    sub: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(8.dp).background(accent, CircleShape))
                Spacer(Modifier.width(7.dp))
                Text(title, color = AppMuted, fontSize = 12.sp)
            }
            Spacer(Modifier.height(10.dp))
            Text(value, color = AppText, fontSize = 27.sp)
            Text(sub, color = accent, fontSize = 11.sp)
        }
    }
}

/** نمودار سبک برای نمایش وضعیت نمونه داشبورد و مالی. */
@Composable
private fun MiniBarChart() {
    // این داده‌ها در نسخه بعد از Repository/Database خوانده خواهند شد.
    val values = listOf(28, 45, 31, 58, 43, 70, 61, 82, 64, 88, 72, 94)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            Box(
                Modifier
                    .weight(1f)
                    .height(value.dp)
                    .background(
                        if (index > 8) Green else Blue.copy(alpha = 0.55f),
                        RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                    )
            )
        }
    }
}

/** شبکه دو ستونه امکانات داشبورد. */
@Composable
private fun FeatureGrid(onOpen: (Screen) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        // ارتفاع متناسب با ۹ ردیف کارت فعلی است و Scroll اصلی توسط Dashboard انجام می‌شود.
        modifier = Modifier
            .fillMaxWidth()
            .height(1010.dp),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(features) { feature ->
            FeatureCard(feature) {
                onOpen(feature.target)
            }
        }
    }
}

/** کارت یک قابلیت روی داشبورد. */
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
                        Icon(
                            feature.icon,
                            contentDescription = null,
                            tint = feature.accent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(9.dp))
                    Text(feature.title, color = AppText, fontSize = 13.sp)
                }
                Spacer(Modifier.height(9.dp))
                Text(feature.subtitle, color = AppMuted, fontSize = 10.sp)
            }

            // Badge فقط برای قابلیت‌هایی که مقدار badge دارند نمایش داده می‌شود.
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

/** کارت کوچک وضعیت در انتهای داشبورد. */
@Composable
private fun SmallStat(
    title: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(accent.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(value.take(1), color = accent)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(title, color = AppMuted, fontSize = 11.sp)
                Text(value, color = AppText, fontSize = 17.sp)
            }
        }
    }
}

/** صفحه پرونده مشتری با جستجوی زنده روی داده نمونه. */
@Composable
private fun CustomersScreen() {
    // query متن فعلی فیلد جستجو را نگه می‌دارد.
    var query by remember { mutableStateOf("") }

    // داده نمونه تا زمان اتصال Room/Supabase در نسخه بعدی.
    val customers = listOf(
        "سارا احمدی" to "۰۹۱۲•••۴۲۱۰",
        "محمد رضایی" to "۰۹۳۵•••۰۸۱۲",
        "نگار کریمی" to "۰۹۱۹•••۳۵۴۰",
        "علی محمدی" to "۰۹۹۱•••۷۱۲۱"
    )

    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // فیلد جستجو.
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("جستجوی مشتری", color = AppMuted) },
                leadingIcon = { Icon(Icons.Rounded.Search, null, tint = AppMuted) },
                singleLine = true
            )
        }

        // دکمه افزودن پرونده؛ فرم واقعی در نسخه داده‌محور اضافه خواهد شد.
        item {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Icon(Icons.Rounded.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("افزودن پرونده مشتری")
            }
        }

        // فقط مشتریانی که با Query سازگارند نمایش داده می‌شوند.
        items(customers.filter { it.first.contains(query, ignoreCase = true) || query.isBlank() }) { customer ->
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .background(Blue.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Person, null, tint = Blue)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(customer.first, color = AppText)
                        Text(customer.second, color = AppMuted, fontSize = 11.sp)
                    }
                    Text("مشاهده", color = Green, fontSize = 11.sp)
                }
            }
        }
    }
}

/** صفحه برنامه روزانه و نوبت‌ها. */
@Composable
private fun AppointmentScreen() {
    // زمان‌های نمونه برای نمایش ساختار UI.
    val slots = listOf(
        "۱۰:۰۰" to "سارا احمدی",
        "۱۱:۳۰" to "محمد رضایی",
        "۱۴:۰۰" to "نگار کریمی",
        "۱۶:۳۰" to "خالی"
    )

    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // کارت انتخاب روز.
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("چهارشنبه ۴ شهریور", color = AppText, fontSize = 18.sp)
                    Text("برنامه امروز مجموعه", color = AppMuted, fontSize = 11.sp)
                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("۱" to "ش", "۲" to "ی", "۳" to "د", "۴" to "س", "۵" to "چ").forEach { day ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(day.second, color = AppMuted, fontSize = 10.sp)
                                Box(
                                    Modifier
                                        .size(38.dp)
                                        .background(
                                            if (day.first == "۴") Green else AppSurface2,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        day.first,
                                        color = if (day.first == "۴") Color.Black else AppText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // لیست اسلات‌های زمانی روز.
        items(slots) { slot ->
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(15.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        slot.first,
                        color = Green,
                        fontSize = 16.sp,
                        modifier = Modifier.width(62.dp)
                    )
                    Column(Modifier.weight(1f)) {
                        Text(slot.second, color = AppText)
                        Text(
                            if (slot.second == "خالی") {
                                "برای ثبت نوبت لمس کنید"
                            } else {
                                "خدمات عمومی • ۴۵ دقیقه"
                            },
                            color = AppMuted,
                            fontSize = 10.sp
                        )
                    }
                    Icon(
                        if (slot.second == "خالی") Icons.Rounded.Add else Icons.Rounded.CheckCircle,
                        null,
                        tint = if (slot.second == "خالی") Blue else Green
                    )
                }
            }
        }
    }
}

/** صفحه مرکز پیامک و گزینه‌های مرتبط. */
@Composable
private fun SmsScreen() {
    // متن پیام فعلی فرم را نگه می‌دارد.
    var message by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            MetricCard(
                "موجودی پیامک",
                "۲۳۳",
                "پیامک باقی مانده",
                Green,
                Modifier.fillMaxWidth()
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("ارسال پیامک", color = AppText, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        placeholder = { Text("متن پیام را بنویسید", color = AppMuted) }
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    ) {
                        Text("انتخاب مخاطب و ادامه")
                    }
                }
            }
        }

        item { ActionRow("ارسال گروهی", "ارسال به گروه مشتریان", Icons.Rounded.Groups, Blue) }
        item { ActionRow("پیامک منطقه‌ای", "هدف‌گیری بر اساس محدوده", Icons.Rounded.LocationOn, Cyan) }
        item { ActionRow("شماره اختصاصی", "تنظیم شماره خدماتی", Icons.Rounded.Phone, Yellow) }
    }
}

/** صفحه مدیریت پرسنل، خدمات و سطح دسترسی. */
@Composable
private fun StaffScreen() {
    // پرسنل نمونه برای نسخه UI اولیه.
    val staff = listOf(
        "علی احمدی" to "مدیر",
        "مریم رضایی" to "اپراتور",
        "سارا کریمی" to "کارشناس"
    )

    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Icon(Icons.Rounded.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("افزودن پرسنل")
            }
        }
        items(staff) { item ->
            ActionRow(item.first, item.second, Icons.Rounded.Badge, Purple)
        }
        item { ActionRow("مدیریت خدمات", "تعریف مدت، قیمت و پرسنل", Icons.Rounded.Build, Cyan) }
        item { ActionRow("سطح دسترسی", "کنترل دسترسی هر کاربر", Icons.Rounded.Settings, Yellow) }
    }
}

/** صفحه خلاصه مالی و حسابداری. */
@Composable
private fun AccountingScreen() {
    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("درآمد", "۰", "امروز", Green, Modifier.weight(1f))
                MetricCard("هزینه", "۰", "امروز", Pink, Modifier.weight(1f))
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("گردش مالی", color = AppText, fontSize = 16.sp)
                    Text("نمایش نمونه برای نسخه اولیه", color = AppMuted, fontSize = 10.sp)
                    Spacer(Modifier.height(16.dp))
                    MiniBarChart()
                }
            }
        }

        item { ActionRow("ثبت درآمد", "ثبت تراکنش جدید", Icons.Rounded.Add, Green) }
        item { ActionRow("گزارش صندوق", "جزئیات دریافتی و پرداختی", Icons.Rounded.Assessment, Blue) }
    }
}

/** صفحه گزارش‌های آماری مجموعه. */
@Composable
private fun ReportsScreen() {
    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { MetricCard("مشتری جدید", "۱۲", "+۸٪ نسبت به ماه قبل", Blue, Modifier.fillMaxWidth()) }
        item { MetricCard("نوبت تکمیل شده", "۴۸", "+۱۴٪ نسبت به ماه قبل", Green, Modifier.fillMaxWidth()) }
        item { ActionRow("گزارش مشتریان", "رفتار و نرخ بازگشت", Icons.Rounded.Person, Blue) }
        item { ActionRow("گزارش نوبت‌ها", "لغو، تکمیل و رزرو", Icons.Rounded.CalendarMonth, Green) }
        item { ActionRow("گزارش پیامک", "ارسال و تحویل پیام", Icons.Rounded.Sms, Pink) }
    }
}

/** صفحه تنظیمات؛ بخش اعلان‌ها طبق استاندارد ثابت پروژه داخل این صفحه قرار دارد. */
@Composable
private fun SettingsScreen() {
    // State فعلی سوییچ اعلان‌ها.
    var notifications by remember { mutableStateOf(true) }

    // State فعلی سوییچ یادآوری نوبت.
    var reminders by remember { mutableStateOf(true) }

    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ToggleRow(
                "اعلان‌ها",
                "اعلان نوبت‌ها و پیام‌های مهم",
                Icons.Rounded.Notifications,
                notifications
            ) {
                notifications = it
            }
        }
        item {
            ToggleRow(
                "یادآوری نوبت",
                "یادآوری قبل از شروع نوبت",
                Icons.Rounded.NotificationsActive,
                reminders
            ) {
                reminders = it
            }
        }
        item { ActionRow("تنظیمات پیامک", "الگوها و تنظیمات ارسال", Icons.Rounded.Sms, Pink) }
        item { ActionRow("پشتیبان‌گیری", "ساخت نسخه پشتیبان از اطلاعات", Icons.Rounded.Refresh, Blue) }
        item { ActionRow("حریم خصوصی", "مجوزها و داده‌های برنامه", Icons.Rounded.Info, Purple) }
    }
}

/** ردیف تنظیم دارای Switch. */
@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Green)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = AppText)
                Text(subtitle, color = AppMuted, fontSize = 10.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onChecked
            )
        }
    }
}

/** ردیف عمومی برای گزینه‌های صفحات مختلف. */
@Composable
private fun ActionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(42.dp)
                    .background(accent.copy(alpha = 0.14f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
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

/** صفحه موقت امکاناتی که Backend آن‌ها هنوز پیاده نشده است. */
@Composable
private fun GenericFeatureScreen(name: String) {
    LazyColumn(
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .background(Green.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.Star,
                            null,
                            tint = Green,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(name, color = AppText, fontSize = 20.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "ساختار این ماژول در نسخه پایه آماده شده و در ادامه به سرویس واقعی متصل می‌شود.",
                        color = AppMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        item { ActionRow("تنظیمات $name", "مدیریت گزینه‌های این بخش", Icons.Rounded.Settings, Blue) }
        item { ActionRow("گزارش $name", "مشاهده آمار و رویدادها", Icons.Rounded.Assessment, Green) }
    }
}

/** صفحه تماس با تیم پشتیبانی. */
@Composable
private fun ContactScreen() {
    // Context برای اجرای Intent برنامه ایمیل لازم است.
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "گروه توسعه و برنامه نویسی AS Team",
            color = AppText,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Icon(
            Icons.Rounded.Email,
            null,
            tint = Green,
            modifier = Modifier.size(36.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text("ایمیل پشتیبانی", color = AppMuted)

        // کلیک روی ایمیل تلاش می‌کند برنامه Email دستگاه را باز کند.
        Text(
            "as.team.support@gmail.com",
            color = Green,
            modifier = Modifier.clickable {
                // runCatching مانع Crash روی دستگاه بدون برنامه ایمیل می‌شود.
                runCatching {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_SENDTO,
                            Uri.parse("mailto:as.team.support@gmail.com")
                        )
                    )
                }
            }
        )
    }
}

/**
 * صفحه درباره نرم‌افزار.
 * مطابق درخواست پروژه فقط توضیح کوتاه درباره کاربرد برنامه و شماره نسخه نمایش داده می‌شود؛
 * نام Package، Application ID یا اطلاعات فنی داخلی به کاربر نشان داده نمی‌شود.
 */
@Composable
private fun SoftwareInfoScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Rounded.Star,
            contentDescription = null,
            tint = Green,
            modifier = Modifier.size(44.dp)
        )
        Spacer(Modifier.height(14.dp))
        Text(
            "درباره نرم افزار تایم",
            color = AppText,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "تایم برای مدیریت ساده‌تر نوبت‌ها، مشتریان، پرسنل، پیام‌ها و امور روزانه کسب‌وکار طراحی شده است.\n" +
                "هدف برنامه این است که اطلاعات مهم مجموعه در یک محیط منظم و یکپارچه در دسترس باشد.",
            color = AppMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Text(
            "نسخه ${BuildConfig.VERSION_NAME}",
            color = Green,
            fontSize = 13.sp
        )
    }
}

/** صفحه وضعیت بروزرسانی برنامه. */
@Composable
private fun UpdateScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(72.dp)
                .background(Green.copy(alpha = 0.13f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Update,
                null,
                tint = Green,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "نسخه نصب شده: ${BuildConfig.VERSION_NAME}",
            color = AppText,
            fontSize = 17.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "ساختار بررسی بروزرسانی برای اتصال به منبع انتشار آماده است.",
            color = AppMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Green)
        ) {
            Text("بررسی نسخه جدید")
        }
    }
}

/** صفحه اطلاعات متنی ساده برای درباره ما و صفحات مشابه. */
@Composable
private fun CenterInfo(title: String, body: String) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            color = AppText,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Text(
            body,
            color = AppMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}
