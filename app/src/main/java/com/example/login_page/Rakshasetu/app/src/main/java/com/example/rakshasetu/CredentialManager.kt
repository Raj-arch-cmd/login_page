package com.example.rakshasetu

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// CredentialManager.kt
private const val PREFS_NAME = "RakshaSetuAuth"
private const val KEY_EMAIL = "email"
private const val KEY_PASSWORD = "password"

@SuppressLint("ApplySharedPref")
suspend fun saveCredentials(context: Context, email: String, password: String) {
    // Simple encryption (don't use in production - see note below)
    val encryptedEmail = "encrypted_$email"
    val encryptedPassword = "encrypted_$password"

    withContext(Dispatchers.IO) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_EMAIL, encryptedEmail)
            .putString(KEY_PASSWORD, encryptedPassword)
            .commit() // Using commit() for immediate write
    }
}

suspend fun getSavedCredentials(context: Context): Pair<String, String>? {
    return withContext(Dispatchers.IO) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null)
        val password = prefs.getString(KEY_PASSWORD, null)

        if (email != null && password != null) {
            // Simple decryption
            Pair(email.removePrefix("encrypted_"), password.removePrefix("encrypted_"))
        } else {
            null
        }
    }
}