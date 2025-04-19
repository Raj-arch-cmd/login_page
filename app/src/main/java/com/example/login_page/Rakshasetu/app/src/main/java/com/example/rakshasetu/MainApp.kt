// MainApp.kt
package com.example.rakshasetu

import EmergencyAlertScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rakshasetu.ui.components.sidebar.sidebarItems

@Composable
fun MainApp(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainScreen(navController)
        }

        // Add routes for all sidebar items
        sidebarItems.forEach { item ->
            composable(item.route) {
                when(item.route) {
                    "emergency" -> EmergencyAlertScreen(alert = sampleAlert) { navController.popBackStack() }
                    // Add other screens here as you implement them
                    else -> PlaceholderScreen(item.title) { navController.popBackStack() }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Scaffold(  // Fixed typo: Changed "Scarfold" to "Scaffold"
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("$title Screen Content")
        }
    }
}