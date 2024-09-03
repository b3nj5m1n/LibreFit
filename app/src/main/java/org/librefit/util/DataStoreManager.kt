package org.librefit.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore( name = USER_PREFERENCES_NAME )

class DataStoreManager (private val context: Context) {
    val themeModeKey = intPreferencesKey("theme_mode")
    val materialModeKey = booleanPreferencesKey("material_mode")
    val keepOnWorkoutScreenKey = booleanPreferencesKey("workout_screen_on")

    val themeMode : Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        ThemeMode.entries.find { it.value == preferences[themeModeKey] } ?: ThemeMode.SYSTEM
    }

    val materialMode : Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[materialModeKey] ?: true
    }

    val workoutScreenOn : Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[keepOnWorkoutScreenKey] ?: true
    }

    suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}