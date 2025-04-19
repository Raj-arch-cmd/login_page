package com.example.login_page

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

object UserPreferencesKeys {
    val EMAIL = stringPreferencesKey("email")
    val PASSWORD = stringPreferencesKey("password")
}

class UserPreferences(private val context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveCredentials(email: String, password: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.EMAIL] = email
            preferences[UserPreferencesKeys.PASSWORD] = password
        }
    }

    fun getCredentials(): Flow<Pair<String?, String?>> {
        return dataStore.data.map { preferences ->
            Pair(
                preferences[UserPreferencesKeys.EMAIL],
                preferences[UserPreferencesKeys.PASSWORD]
            )
        }
    }
}