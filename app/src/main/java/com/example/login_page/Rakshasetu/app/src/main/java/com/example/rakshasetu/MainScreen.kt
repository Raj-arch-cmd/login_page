package com.example.rakshasetu

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * MAIN ENTRY POINT FOR THE APP'S HOME SCREEN
 * Features:
 * - Navigation drawer (sidebar)
 * - Top app bar with menu button
 * - Home content with emergency alerts and quick actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    // State management for the navigation drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Navigation Drawer Setup
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                Sidebar(
                    selectedItem = currentRoute(navController),
                    onItemSelected = { route ->
                        // Handle navigation when items are clicked
                        navController.navigate(route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        // Main app scaffold (layout structure)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("RakshaSetu") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            HomeContent(
                modifier = Modifier.padding(innerPadding),
                onEmergencyClick = { navController.navigate("emergency") },
                onAlertClick = { alertId ->
                    navController.navigate("alertDetails/$alertId")
                }
            )
        }
    }
}

/**
 * MAIN CONTENT AREA OF THE SCREEN
 * Contains:
 * 1. Welcome section with current time
 * 2. Emergency alert card
 * 3. Recent alerts list
 * 4. Quick action buttons
 */
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onEmergencyClick: () -> Unit,
    onAlertClick: (String) -> Unit
) {
    val alerts = listOf(
        Alert("1","EARTHQUAKE", "Earthquake detected in Delhi of 2.8 magnitude", "just now"),
        Alert("2", "FLOOD","Flood warning issued for Yamuna River", "45 min ago"),
        Alert("3", "HEATWAVE","Heatwave alert for next 3 days in Kota, Rajasthan", "2 hours ago"),
        Alert("4", "CYCLONE","A mild cyclone has struck in Chennai", "4 hours ago"),
        Alert("5", "THUNDERSTORM","A thunderstorm has hit the Lajpat Nagar", "5 hours ago"),
        Alert("6", "Tornado"," A small tornado has been spotted near Madhyamgram in Barasat", "1 day ago")
    )

    // Calculate bottom padding to account for QuickActionsRow height
    val quickActionsHeight = 100.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Main scrollable content with bottom padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = quickActionsHeight)
        ) {
            // 1. TIME AND WELCOME SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = getCurrentTime(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome to RakshaSetu",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // 2. EMERGENCY ALERT CARD
            EmergencyAlertCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = onEmergencyClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. RECENT ALERTS SECTION
            Text(
                text = "Recent Alerts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable alerts list
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                items(alerts) { alert ->
                    AlertItem(
                        alert = alert,

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // 4. QUICK ACTIONS ROW (Fixed at bottom)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = Color(0xFFF5F5F5),
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            QuickActionsRow(
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}
/**
 * EMERGENCY ALERT CARD COMPONENT
 * Displays high-priority emergency information
 */
@Composable
private fun EmergencyAlertCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Emergency Alert",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "2.8 magnitude earthquake in Delhi",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "5 high risk zones identified",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * ALERT ITEM COMPONENT
 * Displays individual alert in the Recent Alerts list
 */
@Composable
private fun AlertItem(
    alert: Alert,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = alert.id,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = alert.subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = alert.time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

           
        }
    }
}

/**
 * QUICK ACTIONS ROW COMPONENT
 * Circular buttons for common actions
 */
@Composable
private fun QuickActionsRow(
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        ActionItem("Hub", R.drawable.hub),
        ActionItem("Prepare", R.drawable.prepare),
        ActionItem("Helpline", R.drawable.helpline),
        ActionItem("Account", R.drawable.account)
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White, // White background
        shadowElevation = 4.dp // Optional shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            actions.forEach { action ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { /* Handle action */ }
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp) // Larger size
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = action.iconRes),
                            contentDescription = action.title,
                            tint = Color.Black, // Black icons
                            modifier = Modifier.size(37.dp) // Larger icons
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = action.title,
                        fontSize = 13.sp, // Larger text
                        color = Color.Black, // Black text
                        fontWeight = FontWeight.SemiBold // Bold text
                    )
                }
            }
        }
    }
}
// ======================
// HELPER FUNCTIONS AND DATA CLASSES
// ======================

@SuppressLint("NewApi")
private fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return currentTime.format(formatter)
}

private data class Alert(
    val id: String,
    val title: String,
    val subtitle:String,
    val time: String
)

private data class ActionItem(
    val title: String,
    val iconRes: Int
)

private fun currentRoute(navController: NavController): String {
    val navBackStackEntry = navController.currentBackStackEntry
    return navBackStackEntry?.destination?.route ?: "home"
}

// ======================
// PREVIEW FUNCTIONS
// ======================

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}

@Preview
@Composable
fun HomeContentPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        HomeContent(
            onEmergencyClick = {},
            onAlertClick = {}
        )
    }
}

@Preview
@Composable
fun EmergencyAlertCardPreview() {
    EmergencyAlertCard(onClick = {})
}

@Preview
@Composable
fun AlertItemPreview() {
    AlertItem(
        alert = Alert("1","", "Sample alert message", "Just now")
    )
}

@Preview
@Composable
fun QuickActionsRowPreview() {
    QuickActionsRow()
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
fun MobilePreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}

@Preview(widthDp = 600, heightDp = 1024)
@Composable
fun TabletPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}