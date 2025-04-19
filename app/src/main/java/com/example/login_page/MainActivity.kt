package com.example.login_page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp


// Theme implementation (add this to your project)
@Composable
fun LoginPageTheme(
    darkTheme: Boolean = false, // Set to true for dark theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme(),
        content = content
    )
}

// Default color schemes (add to your theme package if not existing)
@Composable
private fun darkColorScheme() = MaterialTheme.colorScheme.copy(
    primary = androidx.compose.ui.graphics.Color(0xFFBB86FC),
    secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6)
)

@Composable
private fun lightColorScheme() = MaterialTheme.colorScheme.copy(
    primary = androidx.compose.ui.graphics.Color(0xFF6200EE),
    secondary = androidx.compose.ui.graphics.Color(0xFF03DAC6)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginPageTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(context)
                )

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            authViewModel = authViewModel,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onNavigateToSignUp = { navController.navigate("signup") },
                            onForgotPassword = { navController.navigate("forgot_password") }
                        )
                    }

                    composable("signup") {
                        SignUpScreen(
                            authViewModel = authViewModel,
                            onSignUpSuccess = {
                                navController.navigate("login") {
                                    popUpTo("signup") { inclusive = true }
                                }
                            },
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    composable("forgot_password") {
                        ForgotPasswordScreen(
                            onBackToLogin = { navController.popBackStack() }
                        )
                    }

                    composable("home") {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column {
        Text(
            text = "Welcome to RakshaSetu",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "You have successfully logged in",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ForgotPasswordScreen(onBackToLogin: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackToLogin) {
            Text("Back to Login")
        }
    }
}


