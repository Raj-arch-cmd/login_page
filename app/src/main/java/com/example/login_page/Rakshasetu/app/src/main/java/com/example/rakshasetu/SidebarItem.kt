package com.example.rakshasetu.ui.components.sidebar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rakshasetu.R

data class SidebarItem(
    val title: String,
    val iconVector: ImageVector? = null,  // For Material icons
    val iconResId: Int? = null,          // For drawable resources
    val hasAlert: Boolean = false,
    val route: String
) {
    // Helper property to check if any icon is present
    val hasIcon: Boolean get() = iconVector != null || iconResId != null
}

val sidebarItems = listOf(
    SidebarItem(
        title = "Emergency Alert",
        iconVector = Icons.Default.Warning,
        hasAlert = true,
        route = "emergency"
    ),
    SidebarItem(
        title = "Safety Precautions",
        iconResId = R.drawable.safety_check_24, // Alternative for Security
        route = "safety"
    ),
    SidebarItem(
        title = "Epicentre",
        iconVector = Icons.Default.LocationOn, // Alternative for Place
        route = "epicentre"
    ),
    SidebarItem(
        title = "Safe Route",
        iconResId =R.drawable.assistant_navigation_24, // Alternative for Map
        route = "route"
    ),
    SidebarItem(
        title = "Be a Volunteer",
        iconVector = Icons.Default.Person,
        route = "volunteer"
    ),
    SidebarItem(
        title = "Weather Tracker",
        iconVector = Icons.Default.Star, // Alternative for Cloud
        route = "weather"
    ),
    SidebarItem(
        title = "Disaster Alerts",
        iconVector = Icons.Default.Notifications,
        hasAlert = true,
        route = "alerts"
    ),
    SidebarItem(
        title = "Donation",
        iconResId = R.drawable.back_hand_24,
        route = "Donation"
    )

)