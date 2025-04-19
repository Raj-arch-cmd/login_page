package com.example.login_page

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(private val context: Context) : ViewModel() {
    private val userPrefs = UserPreferences(context)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object LoginSuccess : AuthState()
        object RegisterSuccess : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val (savedEmail, savedPassword) = userPrefs.getCredentials().first()

                when {
                    email.isEmpty() || password.isEmpty() -> {
                        _errorMessage.value = "Email and password cannot be empty"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    savedEmail == null || savedPassword == null -> {
                        _errorMessage.value = "No saved credentials found"
                        _authState.value = AuthState.Error("No credentials")
                    }
                    email == savedEmail && password == savedPassword -> {
                        _errorMessage.value = null
                        _authState.value = AuthState.LoginSuccess
                    }
                    else -> {
                        _errorMessage.value = "Invalid email or password"
                        _authState.value = AuthState.Error("Invalid credentials")
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.message}"
                _authState.value = AuthState.Error("Login exception")
            }
        }
    }

    fun registerUser(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                when {
                    fullName.isEmpty() -> {
                        _errorMessage.value = "Please enter full name"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    email.isEmpty() -> {
                        _errorMessage.value = "Please enter email"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        _errorMessage.value = "Please enter valid email"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    password.isEmpty() -> {
                        _errorMessage.value = "Please enter password"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    password != confirmPassword -> {
                        _errorMessage.value = "Passwords don't match"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    password.length < 6 -> {
                        _errorMessage.value = "Password must be ≥6 characters"
                        _authState.value = AuthState.Error("Validation failed")
                    }
                    else -> {
                        userPrefs.saveCredentials(email, password)
                        _errorMessage.value = null
                        _authState.value = AuthState.RegisterSuccess
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.message}"
                _authState.value = AuthState.Error("Registration exception")
            }
        }
    }
}

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}