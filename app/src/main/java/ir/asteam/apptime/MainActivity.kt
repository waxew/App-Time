package ir.asteam.apptime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalLayoutDirection
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AppTimeTheme { AppTimeRoot() } }
    }
}

private val AppBackground = Color(0xFF111318)
private val AppSurface = Color(0xFF1B1E24)
private val AppSurface2 = Color(0xFF242832)
private val AppText = Color(0xFFF6F7F9)
private val AppMuted = Color(0xFFAAB0BC)
private val Green = Color(0xFF2DD4A4)
private val Blue = Color(0xFF4C9CFF)
private val Pink = Color(0xFFFF5A8A)
private val Yellow = Color(0xFFFFC857)
private val Purple = Color(0xFFA978FF)
private val Cyan = Color(0xFF34D5E6)

private val appColors = darkColorScheme(
    primary = Green,
    secondary = Blue,
    background = AppBackground,
    surface = AppSurface,
    onPrimary = Color(0xFF07120F),
    onBackground = AppText,
    onSurface = AppText
)

@Composable
private fun AppTimeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = appColors, content = content)
}

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
    data class Generic(val name: String) : Screen(name)
}

private data class Feature(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accent: Color,
    val badge: String? = null,
    val target: Screen = Screen.Generic(title)
)

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTimeRoot() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var screen: Screen by remember { mutableStateOf(Screen.Dashboard) }
        val context = LocalContext.current

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.fillMaxHeight().width(310.dp),
                    drawerContainerColor = Color(0xFF171A20)
                ) {
                    Spacer(Modifier.height(30.dp))
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text("TIME", color = Green, fontSize = 27.sp)
                        Text("مدیریت هوشمند کسب‌وکار", color = AppMuted, fontSize = 13.sp)
                    }
                    Spacer(Modifier.height(18.dp))
                    Divider(color = Color.White.copy(alpha = .06f))
                    DrawerItem("خانه", Icons.Rounded.Home, screen is Screen.Dashboard) { screen = Screen.Dashboard; scope.launch { drawerState.close() } }
                    DrawerItem("تنظیمات", Icons.Rounded.Settings, screen is Screen.Settings) { screen = Screen.Settings; scope.launch { drawerState.close() } }
                    DrawerItem("معرفی به دوستان", Icons.Rounded.Share, false) {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "اپلیکیشن تایم - مدیریت نوبت و مشتری")
                        }
                        context.startActivity(Intent.createChooser(intent, "اشتراک گذاری"))
                    }
                    DrawerItem("درباره ما", Icons.Rounded.Info, screen is Screen.About) { screen = Screen.About; scope.launch { drawerState.close() } }
                    DrawerItem("تماس با ما", Icons.Rounded.ContactSupport, screen is Screen.Contact) { screen = Screen.Contact; scope.launch { drawerState.close() } }
                    DrawerItem("درباره نرم افزار", Icons.Rounded.Star, screen is Screen.Software) { screen = Screen.Software; scope.launch { drawerState.close() } }
                    DrawerItem("بررسی بروزرسانی", Icons.Rounded.Update, screen is Screen.Update) { screen = Screen.Update; scope.launch { drawerState.close() } }
                    Spacer(Modifier.weight(1f))
                    Text("نسخه ۱.۰.۰", color = AppMuted, modifier = Modifier.padding(22.dp), fontSize = 12.sp)
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
                                if (screen is Screen.Dashboard) Text("سلام، خوش آمدید", color = AppMuted, fontSize = 11.sp)
                            }
                        },
                        navigationIcon = {
                            if (screen !is Screen.Dashboard) {
                                IconButton(onClick = { screen = Screen.Dashboard }) {
                                    Icon(Icons.Rounded.ArrowBack, contentDescription = "بازگشت", tint = AppText)
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Rounded.Menu, contentDescription = "منو", tint = AppText)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
                    )
                }
            ) { padding ->
                Surface(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    color = AppBackground
                ) {
                    when (val current = screen) {
                        Screen.Dashboard -> DashboardScreen { screen = it }
                        Screen.Customers -> CustomersScreen()
                        Screen.Appointments -> AppointmentScreen()
                        Screen.SmsCenter -> SmsScreen()
                        Screen.Staff -> StaffScreen()
                        Screen.Accounting -> AccountingScreen()
                        Screen.Reports -> ReportsScreen()
                        Screen.Settings -> SettingsScreen()
                        Screen.About -> CenterInfo("گروه توسعه و برنامه نویسی AS Team", "تمامی حقوق مربوط به این برنامه انحصاری میباشد")
                        Screen.Contact -> ContactScreen()
                        Screen.Software -> CenterInfo("درباره نرم افزار تایم", "سامانه مدیریت نوبت، مشتری، پیامک، پرسنل، باشگاه مشتریان و گزارش‌های کسب‌وکار در یک محیط یکپارچه.")
                        Screen.Update -> UpdateScreen()
                        is Screen.Generic -> GenericFeatureScreen(current.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(icon, null) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Green.copy(alpha = .13f),
            unselectedContainerColor = Color.Transparent,
            selectedTextColor = Green,
            selectedIconColor = Green,
            unselectedTextColor = AppText,
            unselectedIconColor = AppMuted
        )
    )
}

@Composable
private fun DashboardScreen(onOpen: (Screen) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("نوبت امروز", "۱۰", "۳ نوبت باقی مانده", Green, Modifier.weight(1f))
                MetricCard("مشتریان", "۲۱۷", "+۱۲ این ماه", Blue, Modifier.weight(1f))
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AppSurface),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
        item {
            Text("امکانات", color = AppText, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
            Spacer(Modifier.height(8.dp))
            FeatureGrid(onOpen)
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SmallStat("موجودی پیامک", "۲۳۳", Green, Modifier.weight(1f))
                SmallStat("بسته اشتراک", "فعال", Blue, Modifier.weight(1f))
            }
        }
        item {
            Text("TIME • ALL RIGHTS RESERVED © 2026", color = Color.White.copy(alpha = .28f), fontSize = 10.sp, modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth())
        }
    }
}

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
            Text(value, color = AppText, fontSize = 27.sp)
            Text(sub, color = accent, fontSize = 11.sp)
        }
    }
}

@Composable
private fun MiniBarChart() {
    val values = listOf(28, 45, 31, 58, 43, 70, 61, 82, 64, 88, 72, 94)
    Row(
        modifier = Modifier.fillMaxWidth().height(84.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { index, value ->
            Box(
                Modifier.weight(1f).height(value.dp)
                    .background(if (index > 8) Green else Blue.copy(alpha = .55f), RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
            )
        }
    }
}

@Composable
private fun FeatureGrid(onOpen: (Screen) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth().height(1010.dp),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(features) { feature -> FeatureCard(feature) { onOpen(feature.target) } }
    }
}

@Composable
private fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(102.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
        shape = RoundedCornerShape(17.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.padding(13.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(38.dp).background(feature.accent.copy(alpha = .14f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(feature.icon, contentDescription = null, tint = feature.accent, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(9.dp))
                    Text(feature.title, color = AppText, fontSize = 13.sp)
                }
                Spacer(Modifier.height(9.dp))
                Text(feature.subtitle, color = AppMuted, fontSize = 10.sp)
            }
            feature.badge?.let {
                Box(
                    Modifier.align(Alignment.TopStart).padding(8.dp).background(feature.accent.copy(alpha = .16f), RoundedCornerShape(50)).padding(horizontal = 7.dp, vertical = 3.dp)
                ) { Text(it, color = feature.accent, fontSize = 9.sp) }
            }
        }
    }
}

@Composable
private fun SmallStat(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(accent.copy(alpha = .15f), CircleShape), contentAlignment = Alignment.Center) {
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

@Composable
private fun CustomersScreen() {
    var query by remember { mutableStateOf("") }
    val customers = listOf("سارا احمدی" to "۰۹۱۲•••۴۲۱۰", "محمد رضایی" to "۰۹۳۵•••۰۸۱۲", "نگار کریمی" to "۰۹۱۹•••۳۵۴۰", "علی محمدی" to "۰۹۹۱•••۷۱۲۱")
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
        item {
            Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) {
                Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("افزودن پرونده مشتری")
            }
        }
        items(customers.filter { it.first.contains(query) || query.isBlank() }) { customer ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(44.dp).background(Blue.copy(alpha = .15f), CircleShape), contentAlignment = Alignment.Center) {
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

@Composable
private fun AppointmentScreen() {
    val slots = listOf("۱۰:۰۰" to "سارا احمدی", "۱۱:۳۰" to "محمد رضایی", "۱۴:۰۰" to "نگار کریمی", "۱۶:۳۰" to "خالی")
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("چهارشنبه ۴ شهریور", color = AppText, fontSize = 18.sp)
                    Text("برنامه امروز مجموعه", color = AppMuted, fontSize = 11.sp)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("۱" to "ش", "۲" to "ی", "۳" to "د", "۴" to "س", "۵" to "چ").forEach { day ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(day.second, color = AppMuted, fontSize = 10.sp)
                                Box(Modifier.size(38.dp).background(if (day.first == "۴") Green else AppSurface2, CircleShape), contentAlignment = Alignment.Center) {
                                    Text(day.first, color = if (day.first == "۴") Color.Black else AppText)
                                }
                            }
                        }
                    }
                }
            }
        }
        items(slots) { slot ->
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(15.dp)) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(slot.first, color = Green, fontSize = 16.sp, modifier = Modifier.width(62.dp))
                    Column(Modifier.weight(1f)) {
                        Text(slot.second, color = AppText)
                        Text(if (slot.second == "خالی") "برای ثبت نوبت لمس کنید" else "خدمات عمومی • ۴۵ دقیقه", color = AppMuted, fontSize = 10.sp)
                    }
                    Icon(if (slot.second == "خالی") Icons.Rounded.Add else Icons.Rounded.CheckCircle, null, tint = if (slot.second == "خالی") Blue else Green)
                }
            }
        }
    }
}

@Composable
private fun SmsScreen() {
    var message by remember { mutableStateOf("") }
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { MetricCard("موجودی پیامک", "۲۳۳", "پیامک باقی مانده", Green, Modifier.fillMaxWidth()) }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("ارسال پیامک", color = AppText, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = message, onValueChange = { message = it }, modifier = Modifier.fillMaxWidth().height(140.dp), placeholder = { Text("متن پیام را بنویسید", color = AppMuted) })
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("انتخاب مخاطب و ادامه") }
                }
            }
        }
        item { ActionRow("ارسال گروهی", "ارسال به گروه مشتریان", Icons.Rounded.Groups, Blue) }
        item { ActionRow("پیامک منطقه‌ای", "هدف‌گیری بر اساس محدوده", Icons.Rounded.LocationOn, Cyan) }
        item { ActionRow("شماره اختصاصی", "تنظیم شماره خدماتی", Icons.Rounded.Phone, Yellow) }
    }
}

@Composable
private fun StaffScreen() {
    val staff = listOf("علی احمدی" to "مدیر", "مریم رضایی" to "اپراتور", "سارا کریمی" to "کارشناس")
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Green)) { Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text("افزودن پرسنل") } }
        items(staff) { item -> ActionRow(item.first, item.second, Icons.Rounded.Badge, Purple) }
        item { ActionRow("مدیریت خدمات", "تعریف مدت، قیمت و پرسنل", Icons.Rounded.Build, Cyan) }
        item { ActionRow("سطح دسترسی", "کنترل دسترسی هر کاربر", Icons.Rounded.Settings, Yellow) }
    }
}

@Composable
private fun AccountingScreen() {
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard("درآمد", "۰", "امروز", Green, Modifier.weight(1f))
                MetricCard("هزینه", "۰", "امروز", Pink, Modifier.weight(1f))
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("گردش مالی", color = AppText, fontSize = 16.sp)
                    Text("نمایش نمونه برای نسخه اولیه", color = AppMuted, fontSize = 10.sp)
                    Spacer(Modifier.height(16.dp)); MiniBarChart()
                }
            }
        }
        item { ActionRow("ثبت درآمد", "ثبت تراکنش جدید", Icons.Rounded.Add, Green) }
        item { ActionRow("گزارش صندوق", "جزئیات دریافتی و پرداختی", Icons.Rounded.Assessment, Blue) }
    }
}

@Composable
private fun ReportsScreen() {
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { MetricCard("مشتری جدید", "۱۲", "+۸٪ نسبت به ماه قبل", Blue, Modifier.fillMaxWidth()) }
        item { MetricCard("نوبت تکمیل شده", "۴۸", "+۱۴٪ نسبت به ماه قبل", Green, Modifier.fillMaxWidth()) }
        item { ActionRow("گزارش مشتریان", "رفتار و نرخ بازگشت", Icons.Rounded.Person, Blue) }
        item { ActionRow("گزارش نوبت‌ها", "لغو، تکمیل و رزرو", Icons.Rounded.CalendarMonth, Green) }
        item { ActionRow("گزارش پیامک", "ارسال و تحویل پیام", Icons.Rounded.Sms, Pink) }
    }
}

@Composable
private fun SettingsScreen() {
    var notifications by remember { mutableStateOf(true) }
    var reminders by remember { mutableStateOf(true) }
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { ToggleRow("اعلان‌ها", "اعلان نوبت‌ها و پیام‌های مهم", Icons.Rounded.Notifications, notifications) { notifications = it } }
        item { ToggleRow("یادآوری نوبت", "یادآوری قبل از شروع نوبت", Icons.Rounded.NotificationsActive, reminders) { reminders = it } }
        item { ActionRow("تنظیمات پیامک", "الگوها و تنظیمات ارسال", Icons.Rounded.Sms, Pink) }
        item { ActionRow("پشتیبان‌گیری", "ساخت نسخه پشتیبان از اطلاعات", Icons.Rounded.Refresh, Blue) }
        item { ActionRow("حریم خصوصی", "مجوزها و داده‌های برنامه", Icons.Rounded.Info, Purple) }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, icon: ImageVector, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Green)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(title, color = AppText); Text(subtitle, color = AppMuted, fontSize = 10.sp) }
            Switch(checked = checked, onCheckedChange = onChecked)
        }
    }
}

@Composable
private fun ActionRow(title: String, subtitle: String, icon: ImageVector, accent: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).background(accent.copy(alpha = .14f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = accent) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(title, color = AppText); Text(subtitle, color = AppMuted, fontSize = 10.sp) }
        }
    }
}

@Composable
private fun GenericFeatureScreen(name: String) {
    LazyColumn(contentPadding = PaddingValues(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = AppSurface), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(64.dp).background(Green.copy(alpha = .12f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.Star, null, tint = Green, modifier = Modifier.size(30.dp)) }
                    Spacer(Modifier.height(14.dp)); Text(name, color = AppText, fontSize = 20.sp); Spacer(Modifier.height(6.dp)); Text("ساختار این ماژول در نسخه پایه آماده شده و در ادامه به سرویس واقعی متصل می‌شود.", color = AppMuted, fontSize = 12.sp)
                }
            }
        }
        item { ActionRow("تنظیمات $name", "مدیریت گزینه‌های این بخش", Icons.Rounded.Settings, Blue) }
        item { ActionRow("گزارش $name", "مشاهده آمار و رویدادها", Icons.Rounded.Assessment, Green) }
    }
}

@Composable
private fun ContactScreen() {
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("گروه توسعه و برنامه نویسی AS Team", color = AppText, fontSize = 18.sp)
        Spacer(Modifier.height(18.dp)); Icon(Icons.Rounded.Email, null, tint = Green, modifier = Modifier.size(36.dp)); Spacer(Modifier.height(8.dp))
        Text("ایمیل پشتیبانی", color = AppMuted); Text("as.team.support@gmail.com", color = Green, modifier = Modifier.clickable {
            context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:as.team.support@gmail.com")))
        })
    }
}

@Composable
private fun UpdateScreen() {
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(Modifier.size(72.dp).background(Green.copy(alpha = .13f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Rounded.Update, null, tint = Green, modifier = Modifier.size(36.dp)) }
        Spacer(Modifier.height(16.dp)); Text("نسخه نصب شده: ۱.۰.۰", color = AppText, fontSize = 17.sp); Spacer(Modifier.height(6.dp)); Text("ساختار بررسی بروزرسانی برای اتصال به منبع انتشار آماده است.", color = AppMuted, fontSize = 12.sp)
        Spacer(Modifier.height(18.dp)); Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Green)) { Text("بررسی نسخه جدید") }
    }
}

@Composable
private fun CenterInfo(title: String, body: String) {
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(title, color = AppText, fontSize = 19.sp); Spacer(Modifier.height(12.dp)); Text(body, color = AppMuted, fontSize = 13.sp)
    }
}
